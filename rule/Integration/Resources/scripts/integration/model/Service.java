package integration.model;

import com.exponentus.rest.services.ServiceClassDescriptor;
import integration.init.ModuleConst;

public class Service {

    private ServiceClassDescriptor descr;

    public Service(ServiceClassDescriptor descr) {
        this.descr = descr;
    }

    public String getKind() {
        return "service";
    }

    public ServiceClassDescriptor getDescr() {
        return descr;
    }

    public void setDescr(ServiceClassDescriptor descr) {
        this.descr = descr;
    }

    public String getURL() {
        return ModuleConst.BASE_URL + "services/" + descr.getName();
    }
}
