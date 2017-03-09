package staff.init;

import com.exponentus.webserver.constants.ApplicationMode;

public class AppConst {
	public static String MODULE_VERSION = "1.0";
	public static String NAME = "Staff";
	public static String NAME_ENG = "Staff";
	public static String NAME_RUS = "Структура";
	public static String NAME_KAZ = "Құрылым";
	public static String NAME_POR = "Funcionários";
	public static String NAME_SPA = "Personal";
	public static String DEFAULT_PAGE = "organization-view";
	public static final ApplicationMode AVAILABLE_MODE[] = { ApplicationMode.XML_XSLT };
	public static final String[] ORG_LABELS = { "primary", "inactive" };
	public static final String[] ROLES = { "staff_admin", "fired", "senior_manager" };

}
