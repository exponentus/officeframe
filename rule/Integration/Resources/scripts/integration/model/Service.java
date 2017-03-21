package integration.model;

import java.util.List;

import com.exponentus.common.model.Attachment;
import com.exponentus.rest.services.ServiceClass;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._Session;

public class Service implements IPOJOObject {
	private ServiceClass descr;

	public Service(ServiceClass descr2) {
		this.descr = descr2;
	}

	@Override
	public String getEntityKind() {
		return "service";
	}

	@Override
	public String getIdentifier() {
		return descr.getName();
	}

	@Override
	public String getURL() {
		return "p?id=service-form&amp;docid=" + descr.getName();
	}

	@Override
	public List<Attachment> getAttachments() {
		return null;
	}

	@Override
	public void setAttachments(List<Attachment> attachments) {

	}

	@Override
	public String getFullXMLChunk(_Session ses) {
		return getShortXMLChunk(ses);
	}

	@Override
	public String getShortXMLChunk(_Session ses) {
		StringBuilder chunk = new StringBuilder(1000);
		chunk.append("<name>" + descr.getName() + "</name>");
		chunk.append("<status>" + descr.status + "</status>");
		try {
			String asText = "";
			for (com.exponentus.rest.ServiceMethod a : descr.getServiceMethods()) {
				asText += "<servicemethod id=\"\">";
				asText += "<method>" + a.getMethod() + "</method>";
				asText += "<isanonymous>" + a.isAnonymous() + "</isanonymous>";
				asText += "<url>" + a.getURL() + "</url>";
				asText += "</servicemethod>";
			}
			chunk.append("<servicemethods>" + asText + "</servicemethods>");
		} catch (NullPointerException e) {
			chunk.append("<servicemethods></servicemethods>");
		}

		return chunk.toString();
	}

	@Override
	public boolean isWasRead() {
		return true;
	}

	@Override
	public Object getJSONObj(_Session ses) {
		return this;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

}
