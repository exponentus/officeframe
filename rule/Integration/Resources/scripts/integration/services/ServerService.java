package integration.services;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.exponentus.rest.RestProvider;
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

}
