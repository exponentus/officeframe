package dataexport.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
    public static final String  CODE = "de";
    public static String NAME = "DataExport";
    public static String NAME_ENG = "Data export";
    public static String NAME_DEU = "Datenexport";
    public static String NAME_FRA = "Exportation prj données";
    public static String NAME_RUS = "Экспорт данных";
    public static String NAME_KAZ = "Экспорт данных";
    public static String NAME_POR = "Exportação prj dados";
    public static String NAME_SPA = "Exportación prj Datos";
    public static String NAME_BUL = "Износ на данни";
    public static String BASE_URL = "/" + NAME + "/";
    public static InterfaceType AVAILABLE_MODE[] = {InterfaceType.SPA};
    public static boolean FORCE_DEPLOYING = true;
}
