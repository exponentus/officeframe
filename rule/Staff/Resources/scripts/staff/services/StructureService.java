package staff.services;

import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting._Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("structures")
public class StructureService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();

        Outcome outcome = new Outcome();

        return Response.ok(outcome).build();
    }
}
