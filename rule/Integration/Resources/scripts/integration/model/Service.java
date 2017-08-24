package integration.model;

import com.exponentus.rest.services.ServiceClass;

public class Service {

    private ServiceClass descr;

    public Service(ServiceClass descr2) {
        this.descr = descr2;
    }

    public ServiceClass getDescr() {
        return descr;
    }

    public void setDescr(ServiceClass descr) {
        this.descr = descr;
    }
}
