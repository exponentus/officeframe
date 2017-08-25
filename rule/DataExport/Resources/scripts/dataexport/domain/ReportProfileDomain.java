package dataexport.domain;

import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import dataexport.dao.ReportProfileDAO;
import dataexport.model.ReportProfile;
import reference.dao.DocumentTypeDAO;
import staff.dao.EmployeeDAO;
import staff.model.embedded.Observer;

import java.util.ArrayList;
import java.util.List;

public class ReportProfileDomain extends CommonDomain<ReportProfile> {

    public ReportProfileDomain(_Session ses) throws DAOException {
        super(ses);
        dao = new ReportProfileDAO(ses);
    }

    public ReportProfile composeNew(IUser user) throws DAOException {
        ReportProfile entity = new ReportProfile();
        entity.setAuthor(user);
        DocumentTypeDAO dao = new DocumentTypeDAO(ses);
        return entity;
    }

    @Override
    public ReportProfile fillFromDto(ReportProfile dto, IValidation<ReportProfile> validation, String fsid) throws DTOException, DAOException {
        validation.check(dto);

        ReportProfile entity;

        if (dto.isNew()) {
            entity = new ReportProfile();
        } else {
            entity = dao.findById(dto.getId());
        }

        entity.setTitle(dto.getTitle());
        EmployeeDAO eDao = new EmployeeDAO(ses);

        List<Observer> observers = new ArrayList<Observer>();
        for (Observer o : dto.getObservers()) {
            Observer observer = new Observer();
            observer.setEmployee(eDao.findById(o.getEmployee().getId()));
            observers.add(observer);
        }
        entity.setObservers(observers);

        if (entity.isNew()) {
            entity.setAuthor(ses.getUser());
        }
        entity.setAttachments(getActualAttachments(entity.getAttachments(), dto.getAttachments(), fsid));
        calculateReadersEditors(entity);
        return entity;
    }


}
