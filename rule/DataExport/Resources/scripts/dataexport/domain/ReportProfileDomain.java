package dataexport.domain;

import administrator.dao.LanguageDAO;
import administrator.model.Language;
import com.exponentus.common.domain.CommonDomain;
import com.exponentus.common.domain.IValidation;
import com.exponentus.dataengine.exception.DAOException;
import com.exponentus.env.Environment;
import com.exponentus.localization.constants.LanguageCode;
import com.exponentus.rest.validation.exception.DTOException;
import com.exponentus.scripting._Session;
import com.exponentus.user.IUser;
import dataexport.dao.ReportProfileDAO;
import dataexport.model.ReportProfile;
import dataexport.model.constants.ReportQueryType;
import staff.dao.EmployeeDAO;
import staff.model.embedded.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportProfileDomain extends CommonDomain<ReportProfile> {
    private static final String REPORT_NAME_KEYWORD = "report";

    public ReportProfileDomain(_Session ses) throws DAOException {
        super(ses);
        dao = new ReportProfileDAO(ses);
    }

    public ReportProfile composeNew(IUser user) throws DAOException {
        ReportProfile entity = new ReportProfile();
        entity.setAuthor(user);
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


        entity.setName(dto.getName());
        entity.setTitle(dto.getTitle());
        entity.setReportQueryType(dto.getReportQueryType());
        if (entity.getReportQueryType() == ReportQueryType.ENTITY_REQUEST || entity.getReportQueryType() == ReportQueryType.CUSTOM_CLASS) {
            entity.setClassName(dto.getClassName());
        } else {
            entity.setClassName("");
        }
        entity.setOutputFormat(dto.getOutputFormat());
        entity.setStartFrom(dto.getStartFrom());
        entity.setEndUntil(dto.getEndUntil());
        entity.setTags(dto.getTags());
        Map<LanguageCode, String> locNames = dto.getLocName();
        if (locNames.size() > 0) {
            entity.setLocName(dto.getLocName());
        } else {
            Map<LanguageCode, String> name = new HashMap<>();
            for (Language language : new LanguageDAO(ses).findAllActivated()) {
                name.put(language.getCode(), Environment.vocabulary.getWord(REPORT_NAME_KEYWORD, language.getCode()) + "-" + entity.getTitle());
            }
            entity.setLocName(name);
        }
        entity.setLocalizedDescr(dto.getLocalizedDescr());
        entity.setObservers(dto.getObservers());

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
        // calculateReadersEditors(entity);
        return entity;
    }
}
