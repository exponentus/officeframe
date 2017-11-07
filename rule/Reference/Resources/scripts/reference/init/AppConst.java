package reference.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;
import com.exponentus.common.init.DefaultDataConst;

public class AppConst extends DefaultAppConst {
    public static final String CODE = "ref";
    public static final String[] ROLES = {DefaultDataConst.ROLE_REFERENCE_ADMIN};
    public static final String NAME = "Reference";
    public static String NAME_ENG = "Reference";
    public static String NAME_RUS = "Справочники";
    public static String NAME_KAZ = "Анықтамалар";
    public static String NAME_POR = "Referências";
    public static String NAME_SPA = "Referencias";
    public static String BASE_URL = "/" + NAME + "/";
    public static InterfaceType AVAILABLE_MODE[] = {InterfaceType.SPA};
    public static boolean FORCE_DEPLOYING = true;

    public static String[][] NOT_NULL = {{"ref__city_districts", "locality_id"}, {"ref__industry_types", "category_id"}};
}
