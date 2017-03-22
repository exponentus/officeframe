package integration.services;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.exponentus.env.EnvConst;
import com.exponentus.rest.RestProvider;

@Path("server")
public class ServerService extends RestProvider {

	@GET
	@Path("/info")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		HashMap<String, String> info = new HashMap<>();
		info.put("version", EnvConst.SERVER_VERSION);
		return Response.ok().build();
	}

}
