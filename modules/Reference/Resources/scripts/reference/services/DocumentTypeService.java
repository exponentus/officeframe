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
import reference.dao.DocumentTypeDAO;
import reference.model.DocumentType;
import reference.ui.ViewOptions;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("document-types")
public class DocumentTypeService extends ReferenceService<DocumentType> {


    public Response save(DocumentType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            DocumentTypeDAO dao = new DocumentTypeDAO(session);
            DocumentType entity;

            if (dto.isNew()) {
                entity = new DocumentType();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setCategory(dto.getCategory());
            entity.setPrefix(dto.getPrefix());
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


    protected void validate(DocumentType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
