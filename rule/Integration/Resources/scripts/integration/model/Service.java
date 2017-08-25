package integration.model;

import com.exponentus.rest.services.ServiceClass;
import integration.init.AppConst;

public class Service {

    private ServiceClass descr;

    public String getKind() {
        return "service";
    }

    public Service(ServiceClass descr) {
        this.descr = descr;
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
