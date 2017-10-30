package integration.model;

import com.exponentus.rest.services.ServiceClass;
import integration.init.AppConst;

public class Service {

    private ServiceClass descr;

    public Service(ServiceClass descr) {
        this.descr = descr;
    }

    public String getKind() {
        return "service";
    }

    public ServiceClass getDescr() {
        return descr;
    }

    public void setDescr(ServiceClass descr) {
        this.descr = descr;
    }

    public String getURL() {
        return AppConst.BASE_URL + "services/" + descr.getName();
    }
}
