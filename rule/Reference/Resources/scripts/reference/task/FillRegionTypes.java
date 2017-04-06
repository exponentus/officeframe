package reference.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;

import reference.dao.RegionTypeDAO;
import reference.model.RegionType;
import reference.model.constants.RegionCode;

@Command(name = "fill_region_types")
public class FillRegionTypes extends _Do {
	
	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<RegionType> entities = new ArrayList<>();

		String[] dataEng = { "Federation", "Region", "Urban agglomeration" };
		String[] data = { "Федерация", "Область", "Городская агломерация" };
		String[] dataKZ = { "Федерация", "Область", "Городская агломерация" };
		RegionCode[] code = { RegionCode.FEDERATION, RegionCode.REGION, RegionCode.URBAN_AGGLOMERATION };
		
		for (int i = 0; i < data.length; i++) {
			RegionType entity = new RegionType();
			entity.setName(data[i]);
			Map<LanguageCode, String> name = new HashMap<LanguageCode, String>();
			name.put(LanguageCode.ENG, dataEng[i]);
			name.put(LanguageCode.KAZ, dataKZ[i]);
			name.put(LanguageCode.RUS, data[i]);
			entity.setLocName(name);
			entity.setCode(code[i]);
			entities.add(entity);
		}

		try {
			RegionTypeDAO dao = new RegionTypeDAO(ses);
			for (RegionType entry : entities) {
				try {
					if (dao.add(entry) != null) {
						logger.infoLogEntry(entry.getName() + " added");
					}
				} catch (DAOException e) {
					if (e.getType() == DAOExceptionType.UNIQUE_VIOLATION) {
						logger.warningLogEntry("a data is already exists (" + e.getAddInfo() + "), record was skipped");
					} else if (e.getType() == DAOExceptionType.NOT_NULL_VIOLATION) {
						logger.warningLogEntry("a value is null (" + e.getAddInfo() + "), record was skipped");
					} else {
						logger.errorLogEntry(e);
					}
				} catch (SecureException e) {
					logger.errorLogEntry(e);
				}
			}
		} catch (DAOException e) {
			logger.errorLogEntry(e);
		}
		logger.infoLogEntry("done...");
	}
	
}
