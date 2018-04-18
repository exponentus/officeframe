package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.RealEstateObjPurposeDAO;
import reference.model.RealEstateObjPurpose;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("realestate-purposes")
public class RealEstateObjPurposesService extends ReferenceService<RealEstateObjPurpose> {


}
