package reference.task;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.env.EnvConst;
import com.exponentus.exception.SecureException;
import com.exponentus.log.Lg;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event.Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.util.StringUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import reference.dao.LocalityDAO;
import reference.dao.StreetDAO;
import reference.init.AppConst;
import reference.model.Locality;
import reference.model.Street;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Command(name = AppConst.CODE + "_fill_almaty_streets")
public class FillAlmatyStreets extends Do {
    private static final String STREETS_LIST = EnvConst.RESOURCES_DIR + File.separator + "streets_almaty.xls";

    @Override
    public void doTask(AppEnv appEnv, _Session ses) {
        List<Street> entities = new ArrayList<Street>();

        try {
            File xf = new File(STREETS_LIST);
            if (xf.exists()) {
                LocalityDAO cDao = new LocalityDAO(ses);
                Locality d = cDao.findByName("Almaty");
                if (d != null) {
                    WorkbookSettings ws = new WorkbookSettings();
                    ws.setEncoding("Cp1252");
                    Workbook workbook = null;
                    try {
                        workbook = Workbook.getWorkbook(xf, ws);
                    } catch (BiffException | IOException e) {
                        System.out.println(e);
                    }
                    Sheet sheet = workbook.getSheet(0);
                    int rCount = sheet.getRows();
                    for (int i = 2; i < rCount; i++) {
                        int id = StringUtil.stringToInt(sheet.getCell(0, i).getContents(), -1);
                        String name = sheet.getCell(1, i).getContents();
                        if (!name.equals("") && !name.equals("''") & id != 0) {
                            Street entity = new Street();
                            entity.setLocality(d);
                            entity.setName(name);
                            entity.setStreetId(id);
                            entities.add(entity);
                        }

                    }
                } else {
                    Lg.error("There is no Almaty Locality");

                }
            }
        } catch (DAOException e) {
            e.printStackTrace();
        }

        try {
            StreetDAO dao = new StreetDAO(ses);
            for (Street entry : entities) {
                try {
                    if (dao.add(entry) != null) {
                        logger.info(entry.getName() + " added");
                    }
                } catch (DAOException e) {
                    if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
                        logger.warning("a data is already exists (" + entry.getTitle() + "), record was skipped");
                    } else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
                        logger.warning("a value is null (" + e.getAddInfo() + "), record was skipped");
                    } else {
                        logger.exception(e);
                    }
                } catch (SecureException e) {
                    logger.exception(e);
                }
            }
        } catch (DAOException e) {
            logger.exception(e);
        }
        logger.info("done...");
    }

}
