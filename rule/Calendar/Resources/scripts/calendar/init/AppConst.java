package calendar.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
    public static final String CODE = "cal";
    public static final String NAME = "Calendar";
    public static String NAME_ENG = "Calendar";
    public static String NAME_DEU = "Kalender";
    public static String NAME_FRA = "Calendrier";
    public static String NAME_RUS = "Календарь";
    public static String NAME_KAZ = "Күнтізбе";
    public static String NAME_POR = "Calendário";
    public static String NAME_SPA = "Calendario";
    public static String NAME_BUL = "Календар";
    public static String BASE_URL = "/" + NAME + "/";
    public static InterfaceType AVAILABLE_MODE[] = {InterfaceType.SPA};
    public static boolean FORCE_DEPLOYING = true;

    public static String[][] NOT_NULL = {{CODE + "__events", "reminder_id"}   };
}
