package staff.tasks;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.exception.SecureException;
import com.exponentus.scripting._Session;
import com.exponentus.scripting.event._DoPatch;
import com.exponentus.scriptprocessor.tasks.Command;

import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;
import staff.dao.OrganizationDAO;
import staff.model.Organization;

@Command(name = "load_orgs_nsf")
public class LoadOrgsFromNSF extends _DoPatch {
	private static final String DOMINO_HOST = "localhost";
	private static final String DOMINO_USER = "developer";
	private static final String DOMINO_USER_PWD = "123";
	private static final String STRUCTURE_DATABASE = "test\\struct.nsf";

	@Override
	public void doTask(_Session ses) {
		List<Organization> entities = new ArrayList<>();
		try {
			Session dominoSession = NotesFactory.createSession(DOMINO_HOST, DOMINO_USER, DOMINO_USER_PWD);
		} catch (NotesException e) {
			logger.errorLogEntry(e);
		}

		OrganizationDAO oDao = new OrganizationDAO(ses);
		for (Organization org : entities) {
			try {
				oDao.add(org);
				System.out.println(org.getName() + " added");
			} catch (SecureException e) {
				logger.errorLogEntry(e);
			}
		}
		System.out.println("done...");
	}

}
