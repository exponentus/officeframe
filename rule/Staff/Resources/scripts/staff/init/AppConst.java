package staff.init;

import kz.lof.dataengine.jpa.constants.AppCode;

public class AppConst {
	public static String NAME = "Staff";
	public static String NAME_ENG = "Staff";
	public static String NAME_RUS = "Структура";
	public static String NAME_KZ = "Құрылым";
	public static AppCode CODE = AppCode.STAFF;
	public static String DEFAULT_URL = "p?id=organization-view";
	public static String FT_INDEX_SCOPE = "[{\"tableName\":\"orgs\",\"fieldNames\":"
	        + "[\"name\",\"localized_name\"],\"daoImpl\":\"staff.dao.OrganizationDAO\"},{\"tableName\":\"employees\",\"fieldNames\":"
	        + "[\"name\",\"user_id\"],\"daoImpl\":\"staff.dao.EmployeeDAO\"}]";
}
