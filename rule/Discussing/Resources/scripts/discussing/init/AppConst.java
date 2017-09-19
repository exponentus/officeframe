package discussing.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
    public static final String CODE = "disc";
    public static String NAME = "Discussing";
    public static String NAME_ENG = "Discussing";
    public static String NAME_RUS = "Обсуждения";
    public static String NAME_KAZ = "Обсуждения";
    public static String BASE_URL = "/" + NAME + "/";
    public static final InterfaceType AVAILABLE_MODE[] = {InterfaceType.SPA};
    public static boolean FORCE_DEPLOYING = true;
}
