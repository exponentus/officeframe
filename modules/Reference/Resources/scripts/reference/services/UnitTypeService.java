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
import org.apache.commons.collections4.MapUtils;
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
            Set<String> allCategories = new HashSet<String>();
            ModuleDAO dao = new ModuleDAO();
            allCategories.addAll(Arrays.asList(ModuleConst.UNIT_CATEGORIES));
            List<Application> modules = dao.findAll();
            for (Application module : modules) {
                if (module.isOn()) {
                    String[] categories = (String[]) ReflectionUtil.getAppConstValue(module.getName(), "UNIT_CATEGORIES");
                    allCategories.addAll(Arrays.asList(categories));
                }
            }
            Outcome outcome = getDefaultRefFormOutcome(id);
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

    protected void validate(UnitType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getCategory() == null || entity.getCategory().isEmpty()) {
            ve.addError("category", "required", "field_is_empty");
        }

        if (MapUtils.isEmpty(entity.getLocName()) || entity.getLocName().values().stream().anyMatch(String::isEmpty)) {
            ve.addError("locName", "required:all", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
