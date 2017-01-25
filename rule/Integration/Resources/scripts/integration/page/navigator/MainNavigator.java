package integration.page.navigator;

import java.util.LinkedList;

import com.exponentus.env.Environment;
import com.exponentus.localization.LanguageCode;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.scripting.outline._Outline;
import com.exponentus.scripting.outline._OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

public class MainNavigator extends _DoPage {
	
	@Override
	public void doGET(_Session session, WebFormData formData) {
		LanguageCode lang = session.getLang();
		LinkedList<IOutcomeObject> list = new LinkedList<IOutcomeObject>();
		
		_Outline outline = new _Outline(getLocalizedWord("all_services", lang), "services");
		outline.addEntry(new _OutlineEntry(getLocalizedWord("services", lang), "service-view"));
		
		list.add(outline);
		
		addValue("outline_current", formData.getValueSilently("id").replace("-form", "-view"));
		addValue("workspaceUrl", Environment.getWorkspaceURL());
		addContent(list);
	}
}
