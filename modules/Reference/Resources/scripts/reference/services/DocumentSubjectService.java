package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.DocumentSubjectDAO;
import reference.model.DocumentSubject;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("document-subjects")
public class DocumentSubjectService extends ReferenceService<DocumentSubject> {

    public Response save(DocumentSubject dto) {
        _Session session = getSession();

        try {
            validate(dto);

            DocumentSubjectDAO dao = new DocumentSubjectDAO(session);
            DocumentSubject entity;

            if (dto.isNew()) {
                entity = new DocumentSubject();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setCategory(dto.getCategory());
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
