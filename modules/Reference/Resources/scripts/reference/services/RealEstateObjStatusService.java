package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.RealEstateObjStatusDAO;
import reference.model.RealEstateObjStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("real-estate-obj-statuses")
public class RealEstateObjStatusService extends ReferenceService<RealEstateObjStatus> {


}
