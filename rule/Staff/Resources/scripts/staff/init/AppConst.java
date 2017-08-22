package staff.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
	public static String CODE = "staff";
	public static String NAME = "Staff";
	public static String NAME_ENG = "Staff";
	public static String NAME_RUS = "Структура";
	public static String NAME_KAZ = "Құрылым";
	public static String NAME_POR = "Funcionários";
	public static String NAME_SPA = "Personal";
	public static String BASE_URL = "/" + NAME + "/";
	public static InterfaceType AVAILABLE_MODE[] = { InterfaceType.HTML, InterfaceType.SPA };
	public static String DEFAULT_PAGE = "organization-view";
	public static final String[] ORG_LABELS = { "primary", "inactive" };
	public static final String[] ROLES = { "acting", "staff_admin", "fired", "senior_manager" };
	public static boolean FORCE_DEPLOYING = true;
}
