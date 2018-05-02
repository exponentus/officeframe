package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.TextTemplateDAO;
import reference.model.TextTemplate;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("text-templates")
public class TextTemplateService extends ReferenceService<TextTemplate> {

    public Response save(TextTemplate dto) {
        _Session session = getSession();

        try {
            validate(dto);

            TextTemplateDAO dao = new TextTemplateDAO(session);
            TextTemplate entity;

            if (dto.isNew()) {
                entity = new TextTemplate();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setCategory(dto.getCategory());

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
