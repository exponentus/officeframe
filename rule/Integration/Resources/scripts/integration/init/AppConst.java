package integration.init;

import com.exponentus.common.init.DefaultAppConst;
import com.exponentus.webserver.constants.ApplicationMode;

public class AppConst extends DefaultAppConst {
	public static String MODULE_VERSION = "1.0";
	public static String MODULE_ID = "Integration";
	public static String NAME_ENG = "Integration";
	public static String NAME_RUS = "Интеграция данных";
	public static String NAME_KAZ = "Интеграция данных";
	public static String DEFAULT_PAGE = "service-view";
	public static final ApplicationMode AVAILABLE_MODE[] = { ApplicationMode.XML_XSLT };
}
