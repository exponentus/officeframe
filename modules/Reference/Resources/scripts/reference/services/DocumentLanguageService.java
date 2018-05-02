package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import org.apache.commons.collections4.MapUtils;
import reference.dao.DocumentLanguageDAO;
import reference.model.DocumentLanguage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
            entity.setName(extractAnyNameValue(dto));
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

        if (MapUtils.isEmpty(entity.getLocName()) || entity.getLocName().values().stream().anyMatch(String::isEmpty)) {
            ve.addError("locName", "required", "field_is_empty");
        }

        if (entity.getCode() == null) {
            ve.addError("code", "required", "field_is_empty");
        } else if (entity.getCode() == LanguageCode.UNKNOWN) {
            ve.addError("code", "ne:UNKNOWN", "field_cannot_be_unknown");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
