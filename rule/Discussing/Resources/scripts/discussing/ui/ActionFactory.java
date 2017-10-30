package discussing.ui;

import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.common.ui.actions.Action;
import com.exponentus.common.ui.actions.ActionType;
import discussing.init.AppConst;

public class ActionFactory extends ConventionalActionFactory {
    public Action newTopic = new Action(ActionType.LINK).id("new_topic").caption("new_topic").url(AppConst.BASE_URL + "topics/new");
}
