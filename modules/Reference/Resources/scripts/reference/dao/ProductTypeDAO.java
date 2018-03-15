package reference.dao;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.scripting._Session;
import reference.model.ProductType;
import reference.model.Vehicle;

import java.util.UUID;

public class ProductTypeDAO extends ReferenceDAO<ProductType, UUID> {

    public ProductTypeDAO(_Session session) throws DAOException {
        super(ProductType.class, session);
    }
}
