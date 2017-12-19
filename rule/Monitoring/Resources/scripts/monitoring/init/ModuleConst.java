package monitoring.init;

import administrator.model.constants.InterfaceType;
import com.exponentus.common.init.DefaultAppConst;

public class ModuleConst extends DefaultAppConst {
    public static final String CODE = "monit";
    public static final String NAME = "Monitoring";
    public static String NAME_ENG = "Monitoring";
    public static String NAME_DEU = "Überwachung";
    public static String NAME_FRA = "Surveillance";
    public static String NAME_RUS = "Мониторинг";
    public static String NAME_POR = "Monitoramento";
    public static String NAME_SPA = "Supervisión";
    public static String NAME_ITA = "Monitoraggio";
    public static String NAME_CHI = "监控";
    public static String NAME_ARA = "مراقبة";
    public static String NAME_BUL = "Мониторинг";
    public static String NAME_KAZ = "Мониторинг";
    public static String BASE_URL = "/" + NAME + "/";
    public static InterfaceType AVAILABLE_MODE[] = {InterfaceType.SPA};
    public static boolean FORCE_DEPLOYING = true;
}
