package reference.services;

import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.exception.SecureException;
import com.exponentus.rest.outgoingdto.Outcome;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import reference.dao.ProjectStageDAO;
import reference.model.ProjectStage;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("project-stages")
public class ProjectStageService extends ReferenceService<ProjectStage> {


}
