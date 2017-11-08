package workspace.init;

import administrator.model.constants.InterfaceType;
import administrator.model.constants.VisibiltyMode;
import com.exponentus.common.init.DefaultAppConst;

public class AppConst extends DefaultAppConst {
    public static final String CODE = "ws";
    public static final InterfaceType AVAILABLE_MODE[] = {InterfaceType.SPA};
    public static VisibiltyMode VISIBILITY = VisibiltyMode.HIDDEN;
    public static final String NAME = "Workspace";
    public static String NAME_ENG = "Workspace";
    public static String NAME_RUS = "Рабочая область";
    public static String NAME_KAZ = "Жұмыс аумағы";
    public static String NAME_POR = "Área de trabalho";
    public static String NAME_SPA = "Espacio de trabajo";
    public static boolean FORCE_DEPLOYING = true;

}
