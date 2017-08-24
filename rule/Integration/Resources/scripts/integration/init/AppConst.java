package integration.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
	public static String CODE = "integ";
	public static String NAME = "Integration";
	public static String NAME_ENG = "Integration";
	public static String NAME_RUS = "Интеграция данных";
	public static String NAME_KAZ = "Интеграция данных";
	public static String BASE_URL = "/" + NAME + "/";
	public static InterfaceType AVAILABLE_MODE[] = { InterfaceType.SPA };
	public static boolean FORCE_DEPLOYING = true;

}
