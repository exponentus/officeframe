package workspace.page;

import com.exponentus.env.Environment;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.user.AnonymousUser;

public class Workspace extends _DoPage {

    private static final long tm = System.currentTimeMillis();

    @Override
    public void doGET(_Session session, WebFormData formData) {
        addValue("logo", Environment.orgLogo);
        addValue("build", "" + tm);

        if (!formData.containsField("skip-auth-error") && session.getUser().getUserID().equalsIgnoreCase(AnonymousUser.USER_NAME)) {
            setUnauthorized();
        }
    }
}
