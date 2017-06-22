package reference.services;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;

import reference.domain.NationalityDomain;
import reference.model.Nationality;

@Path("nationalities")
public class NationalityService extends ReferenceService<Nationality, NationalityDomain> {

	@Override
	public Response saveForm(Nationality dto) {
		try {
			NationalityDomain domain = new NationalityDomain(getSession());
			Nationality entity = domain.fillFromDto(dto, new Validation(), getWebFormData().getFormSesId());
			Outcome outcome = domain.getOutcome(domain.save(entity));
			return Response.ok(outcome).build();
		} catch (DTOException e) {
			return responseValidationError(e);
		} catch (DAOException | SecureException e) {
			return responseException(e);
		}
	}

	public class Validation implements IValidation<Nationality> {

		@Override
		public void check(Nationality dto) throws DTOException {
			DTOException e = new DTOException();

			if (dto.getName() == null) {
				e.addError("name", "required", "field_is_empty");
			}

			if (e.hasError()) {
				throw e;
			}
		}
	}

}
