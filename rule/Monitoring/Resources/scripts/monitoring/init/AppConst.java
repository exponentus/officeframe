package monitoring.init;

import com.exponentus.common.init.DefaultAppConst;
import com.exponentus.webserver.constants.ApplicationMode;

public class AppConst extends DefaultAppConst {
	public static String MODULE_VERSION = "1.0";
	public static String MODULE_ID = "Monitoring";
	public static String NAME_ENG = "Monitoring";
	public static String NAME_RUS = "Мониторинг";
	public static String NAME_KAZ = "Мониторинг";
	public static String DEFAULT_PAGE = "useractivity-view";
	public static final ApplicationMode AVAILABLE_MODE[] = { ApplicationMode.XML_XSLT };
}
