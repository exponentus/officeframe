package integration.model;

import java.util.List;

import com.exponentus.common.model.Attachment;
import com.exponentus.rest.ServiceDescriptor;
import com.exponentus.scripting.IPOJOObject;
import com.exponentus.scripting._Session;

public class Service implements IPOJOObject {
	private ServiceDescriptor descr;

	public Service(ServiceDescriptor descr) {
		this.descr = descr;
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
		chunk.append("<id>" + descr.getName() + "</id>");
		chunk.append("<name>" + descr.getName() + "</name>");
		chunk.append("<isloaded>" + descr.isLoaded() + "</isloaded>");
		chunk.append("<method></method>");
		chunk.append("<testurl></testurl>");
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
