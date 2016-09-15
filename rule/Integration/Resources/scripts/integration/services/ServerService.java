package integration.services;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.exponentus.env.Environment;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.ServiceDescriptor;
import com.exponentus.rest.ServiceMethod;
import com.exponentus.server.Server;

@Path("server")
public class ServerService extends RestProvider {

	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		HashMap<String, String> info = new HashMap<String, String>();
		info.put("version", Server.serverVersion);
		return Response.ok(info).build();
	}

	@Override
	public ServiceDescriptor getDescription() {
		ServiceDescriptor sd = new ServiceDescriptor();
		sd.setName(getClass().getName());
		ServiceMethod m = new ServiceMethod();
		m.setMethod(HttpMethod.GET);
		m.setExample(Environment.getFullHostName());
		sd.addMethod(m);
		return sd;
	}
}
