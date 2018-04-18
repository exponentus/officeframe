package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.ProductTypeDAO;
import reference.model.ProductType;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("product-types")
public class ProductTypeService extends ReferenceService<ProductType> {


}
