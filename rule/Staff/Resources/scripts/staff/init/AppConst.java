package staff.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
    public static final String CODE = "staff";
    public static final String[] ORG_LABELS = {"primary", "inactive"};
    public static final String ROLE_STAFF_ADMIN = "staff_admin";
    public static final String[] ROLES = {"acting", ROLE_STAFF_ADMIN, "fired", "senior_manager"};
    public static final String NAME = "Staff";
    public static String NAME_ENG = "Staff";
    public static String NAME_RUS = "Структура";
    public static String NAME_KAZ = "Құрылым";
    public static String NAME_POR = "Funcionários";
    public static String NAME_SPA = "Personal";
    public static String BASE_URL = "/" + NAME + "/";
    public static InterfaceType AVAILABLE_MODE[] = {InterfaceType.SPA};
    public static boolean FORCE_DEPLOYING = true;

    public static String[][] NOT_NULL = {{"staff__orgs", "orgcategory_id"}};

}
