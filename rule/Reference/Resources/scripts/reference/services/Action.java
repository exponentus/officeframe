package reference.services;

import com.exponentus.scripting.actions._Action;
import com.exponentus.scripting.actions._ActionType;

public class Action {
    public static final _Action addNew = new _Action(_ActionType.CUSTOM_ACTION).id("new_").caption("new_");
    public static final _Action refreshVew = new _Action(_ActionType.RELOAD).id("refresh").icon("fa fa-refresh");
    public static final _Action close = new _Action(_ActionType.CLOSE).caption("close").icon("fa fa-chevron-left").cls("btn-back");
    public static final _Action saveAndClose = new _Action(_ActionType.SAVE_AND_CLOSE).caption("save_close").cls("btn-primary");
    public static final _Action deleteDocument = new _Action(_ActionType.DELETE_DOCUMENT).caption("del_document");
}
