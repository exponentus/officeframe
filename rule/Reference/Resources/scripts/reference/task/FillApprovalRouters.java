package reference.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exponentus.appenv.AppEnv;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.dataengine.exception.DAOExceptionType;
import com.exponentus.env.Environment;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._Do;
import com.exponentus.scriptprocessor.tasks.Command;
import com.exponentus.util.EnumUtil;
import com.exponentus.util.NumberUtil;

import reference.dao.ApprovalRouteDAO;
import reference.model.ApprovalRoute;
import reference.model.constants.ApprovalSchemaType;
import reference.model.constants.ApprovalType;
import reference.model.embedded.RouteBlock;
import staff.dao.EmployeeDAO;
import staff.model.Employee;

@Command(name = "fill_approval_routes")
public class FillApprovalRouters extends _Do {

	@Override
	public void doTask(AppEnv appEnv, _Session ses) {
		List<ApprovalRoute> entities = new ArrayList<>();

		String names[] = { "r1", "r2", "r3", "r4" };
		String namesEng[] = { "Route 1", "Route 2", "Route 3", "Route 4" };
		String namesRus[] = { "Маршрут 1", "Маршрут 2", "Маршрут 3", "Маршрут 4" };
		String namesKaz[] = { "Маршрут 1", "Маршрут 2", "Маршрут 3", "Маршрут 4" };

		for (int i = 0; i < names.length; i++) {
			ApprovalRoute entity = new ApprovalRoute();
			entity.setName(names[i]);
			entity.setCategory("");
			entity.setSchema(ApprovalSchemaType.REJECT_IF_NO);
			List<RouteBlock> routeBlocks = new ArrayList<RouteBlock>();
			RouteBlock block = getMockBlock();
			block.setPosition(i + 1);
			routeBlocks.add(block);
			entity.setRouteBlocks(routeBlocks);
			Map<LanguageCode, String> name = new HashMap<>();
			name.put(LanguageCode.RUS, namesRus[i]);
			name.put(LanguageCode.ENG, namesEng[i]);
			name.put(LanguageCode.KAZ, namesKaz[i]);
			entity.setLocName(name);
			entities.add(entity);
		}

		try {
			ApprovalRouteDAO dao = new ApprovalRouteDAO(ses);
			for (ApprovalRoute entry : entities) {
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

	private RouteBlock getMockBlock() {
		EmployeeDAO uDao = (EmployeeDAO) Environment.getExtUserDAO();
		RouteBlock bl = new RouteBlock();
		bl.setType(EnumUtil.getRndElement(ApprovalType.values()));
		List<Employee> approvers = new ArrayList<Employee>();
		int apprCount = NumberUtil.getRandomNumber(1, 5);
		for (int i = 1; i <= apprCount; i++) {
			approvers.add(uDao.getRandomEntity());
		}
		bl.setApprovers(approvers);
		return bl;
	}

}
