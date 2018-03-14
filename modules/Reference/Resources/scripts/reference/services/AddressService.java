package reference.services;

import com.exponentus.common.ui.TreeNode;
import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.RestProvider;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.CountryDAO;
import reference.dto.converter.AddressDtoConverter;
import reference.model.Country;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("address")
public class AddressService extends RestProvider {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

        try {
            CountryDAO dao = new CountryDAO(session);
            ViewPage<Country> vp = dao.findViewPage(SortParams.desc("name"), 0, 0);

            AddressDtoConverter addressConverter = new AddressDtoConverter();
            TreeNode treeNode = null;

            for (Country country : vp.getResult()) {
                if ("kazakhstan".equals(country.getName())) {
                    treeNode = addressConverter.apply(country);
                }
            }

            Outcome outcome = new Outcome();
            outcome.addPayload(vp);
            outcome.addPayload(treeNode);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }
}
