package dataexport.ui;

import com.exponentus.common.ui.ConventionalActionFactory;
import com.exponentus.scripting.actions.Action;
import com.exponentus.scripting.actions.ActionType;

public class ActionFactory extends ConventionalActionFactory {
    public Action toForm = new Action(ActionType.API_ACTION).id("to_form").caption("to_form").url("toForm");
}
