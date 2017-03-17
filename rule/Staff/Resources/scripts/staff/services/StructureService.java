package staff.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.exponentus.common.dao.ViewEntryDAO;
import com.exponentus.common.model.ViewEntry;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.ServiceDescriptor;
import com.exponentus.rest.ServiceMethod;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting._Session;

import staff.dao.OrganizationDAO;
import staff.model.Organization;

@Path("structures")
public class StructureService extends RestProvider {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getViewPage() {
		_Session session = getSession();

		try {
			Outcome outcome = new Outcome();

			ViewEntryDAO veDao = new ViewEntryDAO(session);
			OrganizationDAO dao = new OrganizationDAO(session);
			Organization org = dao.findPrimaryOrg().get(0);

			if (org != null) {
				List<ViewEntry> descendants = veDao.findAllDescendants(org.getIdentifier());

				outcome.setTitle("structures");
				outcome.addPayload("structures", descendants);
			}

			return Response.ok(outcome).build();
		} catch (DAOException e) {
			return responseException(e);
		}
	}

	@Override
	public ServiceDescriptor updateDescription(ServiceDescriptor sd) {
		sd.setName(getClass().getName());
		ServiceMethod m = new ServiceMethod();
		m.setMethod(HttpMethod.GET);
		m.setURL("/" + sd.getAppName() + sd.getUrlMapping());
		sd.addMethod(m);
		return sd;
	}
}
