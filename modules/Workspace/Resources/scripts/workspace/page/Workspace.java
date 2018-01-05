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
        addValue("orgLogo", Environment.logo);
        addValue("build", "" + tm);
        addValue("locale", session.getLang().getAlternateCode());
        addValue("googleMapApiKey", Environment.mapsApiKey);

        if (!formData.containsField("skip-auth-error") && session.getUser().getUserID().equalsIgnoreCase(AnonymousUser.USER_NAME)) {
            setUnauthorized();
        }
    }
}
