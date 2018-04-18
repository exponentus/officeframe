package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import reference.dao.DocumentLanguageDAO;
import reference.model.DocumentLanguage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("document-languages")
public class DocumentLanguageService extends ReferenceService<DocumentLanguage> {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            Outcome outcome = getDefaultRefFormOutcome(id);
            outcome.addPayload("languageCodes", LanguageCode.values());
            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }


    public Response save(DocumentLanguage dto) {
        _Session session = getSession();

        try {
            validate(dto);

            DocumentLanguageDAO dao = new DocumentLanguageDAO(session);
            DocumentLanguage entity;

            if (dto.isNew()) {
                entity = new DocumentLanguage();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setCode(dto.getCode());

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


    protected void validate(DocumentLanguage entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }

        if (entity.getCode() == null) {
            ve.addError("code", "required", "field_is_empty");
        } else if (entity.getCode().toString().equalsIgnoreCase("unknown")) {
            ve.addError("code", "ne_unknown", "field_cannot_be_unknown");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
