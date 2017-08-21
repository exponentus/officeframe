package reference.init;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.jpa.deploying.InitialDataAdapter;
import com.exponentus.env.EnvConst;
import com.exponentus.localization.Vocabulary;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.util.StringUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import reference.dao.LocalityDAO;
import reference.dao.StreetDAO;
import reference.model.Locality;
import reference.model.Street;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kayra on 24/01/16.
 */

public class FillStreets extends InitialDataAdapter<Street, StreetDAO> {
	private static final String STREETS_LIST = EnvConst.RESOURCES_DIR + File.separator + "streets_almaty.xls";
	
	@Override
	public List<Street> getData(_Session ses, LanguageCode lang, Vocabulary vocabulary) {

		List<Street> entities = new ArrayList<>();
		try {
			LocalityDAO cDao = new LocalityDAO(ses);
			Locality d = null;
			
			d = cDao.findByName("Алматы");
			
			if (d != null) {
				File xf = new File(STREETS_LIST);
				if (xf.exists()) {
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
					System.out.println(
							"There is no appropriate file (" + STREETS_LIST + "). It will be loaded default data");
					String[] data = { "Champs Elysées", "La Rambla", "Fifth Avenue", "Via Appia", "Zeil", "Abbey Road",
							"Khao San", "Rua Augusta" };
					int count = 1;
					for (String val : data) {
						Street entity = new Street();
						entity.setLocality(d);
						entity.setName(val);
						entity.setStreetId(count);
						entities.add(entity);
						count++;
					}
				}
				
				Street entity = new Street();
				entity.setLocality(d);
				entity.setName("unknown");
				entity.setStreetId(0);
				entities.add(entity);
			}
		} catch (DAOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return entities;
		
	}
	
}
