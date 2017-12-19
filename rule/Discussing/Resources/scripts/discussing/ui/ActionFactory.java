package discussing.ui;

import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.actions.Action;
import com.exponentus.common.ui.actions.constants.ActionType;
import discussing.init.ModuleConst;

public class ActionFactory extends ConventionalActionFactory {
    public Action newTopic = new Action(ActionType.LINK).id("new_topic").caption("new_topic").url(ModuleConst.BASE_URL + "topics/new");
}
