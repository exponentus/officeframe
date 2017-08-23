package dataexport.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
	public static String CODE = "de";
	public static String NAME = "DataExport";
	public static String NAME_ENG = "DataExport";
	public static String NAME_RUS = "Экспорт данных";
	public static String NAME_KAZ = "Экспорт данных";
	public static String BASE_URL = "/" + NAME + "/";
	public static InterfaceType AVAILABLE_MODE[] = { InterfaceType.SPA };

}
