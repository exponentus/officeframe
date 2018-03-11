package reference.services;

import administrator.dao.ModuleDAO;
import administrator.model.Application;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import com.exponentus.util.ReflectionUtil;
import com.exponentus.util.StringUtil;
import reference.dao.UnitTypeDAO;
import reference.init.ModuleConst;
import reference.model.UnitType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("unit-types")
public class UnitTypeService extends ReferenceService<UnitType> {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            _Session session = getSession();
            UnitType entity;
            boolean isNew = "new".equals(id);

            if (isNew) {
                entity = new UnitType();
                entity.setName("");
                entity.setAuthor(session.getUser());
            } else {
                UnitTypeDAO dao = new UnitTypeDAO(session);
                entity = dao.findByIdentifier(id);
            }

            Set<String> allCategories = new HashSet<String>();
            ModuleDAO dao = new ModuleDAO();
            allCategories.addAll(Arrays.asList(ModuleConst.UNIT_CATEGORIES));
            List<Application> modules = dao.findAll().getResult();
            for (Application module : modules) {
                if (module.isOn()) {
                    String[] categories = (String[]) ReflectionUtil.getAppConstValue(module.getName(), "UNIT_CATEGORIES");
                    allCategories.addAll(Arrays.asList(categories));
                }
            }

            Outcome outcome = new Outcome();
            outcome.setModel(entity);
            outcome.setPayloadTitle("unit_type");
            outcome.setFSID(getWebFormData().getFormSesId());
            outcome.addPayload(getDefaultFormActionBar(entity));
            outcome.addPayload("unitCategories", allCategories);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    @Override
    public Response save(UnitType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            UnitTypeDAO dao = new UnitTypeDAO(session);
            UnitType entity;

            if (dto.isNew()) {
                entity = new UnitType();
            } else {
                entity = dao.findById(dto.getId());
            }

            if (entity.getName() == null || entity.getName().isEmpty()) {
                entity.setName(StringUtil.convertStringToURL(dto.getLocName()));
            }
            entity.setTitle(entity.getName());
            entity.setLocName(dto.getLocName());
            entity.setCategory(dto.getCategory());
            entity.setFactor(dto.getFactor());

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

    private void validate(UnitType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getLocName().size() == 0) {
            ve.addError("locName", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
