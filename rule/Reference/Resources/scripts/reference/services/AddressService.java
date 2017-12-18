package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.CountryDAO;
import reference.model.Country;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("address")
public class AddressService extends RestProvider {

    /*
    private Country country;
	private Region region;
	private CityDistrict cityDistrict;
	private Locality locality;
	private Street street;
	private String houseNumber;
	private String coordinates;
	private String additionalInfo = "";
     */

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search() {
        _Session session = getSession();
        WebFormData params = getWebFormData();
        int pageSize = session.getPageSize();

        try {
            CountryDAO dao = new CountryDAO(session);
            ViewPage<Country> vp = dao.findViewPage(SortParams.desc("regDate"), params.getPage(), pageSize);

            Outcome outcome = new Outcome();
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }
}
