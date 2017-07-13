package monitoring.page.navigator;

import java.util.LinkedList;

import com.exponentus.env.Environment;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting.event._DoPage;
import com.exponentus.scripting.outline.Outline;
import com.exponentus.scripting.outline.OutlineEntry;
import com.exponentus.scriptprocessor.page.IOutcomeObject;

public class MainNavigator extends _DoPage {

	@Override
	public void doGET(_Session session, WebFormData formData) {
		LanguageCode lang = session.getLang();
		LinkedList<IOutcomeObject> list = new LinkedList<IOutcomeObject>();

		Outline outline = new Outline(getLocalizedWord("activities", lang), "activities");
		outline.addEntry(new OutlineEntry(getLocalizedWord("activities", lang), "activity-view"));

		list.add(outline);

		addValue("outline_current", formData.getValueSilently("id").replace("-form", "-view"));
		addValue("workspaceUrl", Environment.getWorkspaceURL());
		addContent(list);
	}
}
