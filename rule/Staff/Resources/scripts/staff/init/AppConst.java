package staff.init;

import com.exponentus.dataengine.jpa.constants.AppCode;

public class AppConst {
	public static String MODULE_VERSION = "1.0";
	public static String NAME = "Staff";
	public static String NAME_ENG = "Staff";
	public static String NAME_RUS = "Структура";
	public static String NAME_KAZ = "Құрылым";
	public static String NAME_POR = "Funcionários";
	public static String NAME_SPA = "Personal";
	public static AppCode CODE = AppCode.STAFF;
	public static String DEFAULT_PAGE = "organization-view";
	public static String FT_INDEX_SCOPE = "[{\"tableName\":\"orgs\",\"fieldNames\":"
	        + "[\"name\",\"localized_name\"],\"daoImpl\":\"staff.dao.OrganizationDAO\"},{\"tableName\":\"employees\",\"fieldNames\":"
	        + "[\"name\",\"user_id\"],\"daoImpl\":\"staff.dao.EmployeeDAO\"}]";
}
