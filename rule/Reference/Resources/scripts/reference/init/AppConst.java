package reference.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
    public static final String CODE = "ref";
    public static String NAME = "Reference";
    public static String NAME_ENG = "Reference";
    public static String NAME_RUS = "Справочники";
    public static String NAME_KAZ = "Анықтамалар";
    public static String NAME_POR = "Referências";
    public static String NAME_SPA = "Referencias";
    public static String BASE_URL = "/" + NAME + "/";
    public static InterfaceType AVAILABLE_MODE[] = {InterfaceType.SPA};
    public static final String ROLE_REFERENCE_ADMIN = "reference_admin";
    public static final String[] ROLES = {ROLE_REFERENCE_ADMIN};
    public static boolean FORCE_DEPLOYING = true;
}
