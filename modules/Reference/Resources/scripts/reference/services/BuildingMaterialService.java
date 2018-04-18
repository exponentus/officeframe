package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.BuildingMaterialDAO;
import reference.model.BuildingMaterial;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("building-materials")
public class BuildingMaterialService extends ReferenceService<BuildingMaterial> {

    public Response save(BuildingMaterial dto) {
        _Session session = getSession();


        try {
            validate(dto);

            BuildingMaterialDAO dao = new BuildingMaterialDAO(session);
            BuildingMaterial entity;

            if (dto.isNew()) {
                entity = new BuildingMaterial();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(extractAnyNameValue(dto));
            entity.setAltName(dto.getAltName());
            entity.setLocName(dto.getLocName());

            dao.save(entity);

            Outcome outcome = new Outcome();
            outcome.setModel(entity);

            return Response.ok(outcome).build();
        } catch (SecureException | DAOException e) {
            return responseException(e);
        } catch (DTOException e) {
            return responseValidationError(e);
        }
    }


}
