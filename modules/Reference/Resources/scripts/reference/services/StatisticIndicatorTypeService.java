package reference.services;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting.WebFormData;
import com.exponentus.scripting._Session;
import reference.dao.StatisticIndicatorTypeDAO;
import reference.dao.filter.StatisticIndicatorTypeFilter;
import reference.model.StatisticIndicatorType;
import reference.ui.ViewOptions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("statistic-indicator-types")
public class StatisticIndicatorTypeService extends ReferenceService<StatisticIndicatorType> {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getViewPage() {
        _Session session = getSession();
        WebFormData params = getWebFormData();

        try {
            SortParams sortParams = params.getSortParams(SortParams.asc("name"));
            StatisticIndicatorTypeDAO dao = new StatisticIndicatorTypeDAO(session);
            StatisticIndicatorTypeFilter filter = new StatisticIndicatorTypeFilter(params);
            int pageSize = session.getPageSize();

            ViewPage<StatisticIndicatorType> vp = dao.findViewPage(filter, sortParams, params.getPage(), pageSize);
            ViewOptions vo = new ViewOptions();
            vp.setViewPageOptions(vo.getStatisticIndicatorTypeOptions());

            Outcome outcome = new Outcome();
            outcome.setTitle("statistic_indicator_types");
            outcome.setPayloadTitle("statistic_indicator_types");
            outcome.addPayload(getDefaultViewActionBar(true));
            outcome.addPayload(vp);

            return Response.ok(outcome).build();
        } catch (DAOException e) {
            return responseException(e);
        }
    }

    public Response save(StatisticIndicatorType dto) {
        _Session session = getSession();

        try {
            validate(dto);

            StatisticIndicatorTypeDAO dao = new StatisticIndicatorTypeDAO(session);
            StatisticIndicatorType entity;

            if (dto.isNew()) {
                entity = new StatisticIndicatorType();
            } else {
                entity = dao.findById(dto.getId());
            }

            // fill from dto
            entity.setName(dto.getName());
            entity.setLocName(dto.getLocName());
            entity.setStatisticType(dto.getStatisticType());
            entity.setUnitType(dto.getUnitType());

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

    protected void validate(StatisticIndicatorType entity) throws DTOException {
        DTOException ve = new DTOException();

        if (entity.getName() == null || entity.getName().isEmpty()) {
            ve.addError("name", "required", "field_is_empty");
        }
        if (entity.getStatisticType() == null) {
            ve.addError("statisticType", "required", "field_is_empty");
        }

        if (ve.hasError()) {
            throw ve;
        }
    }
}
