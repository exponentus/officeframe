package integration.dao;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.env.EnvConst;
import com.exponentus.rest.ResourceLoader;
import com.exponentus.rest.services.ServiceClassDescriptor;
import com.exponentus.rest.util.ServicesHelper;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import integration.model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private List<ServiceClassDescriptor> services;

    public ServiceDAO(_Session ses) {
        // services = ResourceLoader.getServices();
    }

    public ViewPage<Service> findViewPage(SortParams sortParams, int pageNum, int pageSize) {
        List<Service> entites = new ArrayList<Service>();

        for (ServiceClassDescriptor entry : ServicesHelper.getModuleServiceMethods(EnvConst.MAIN_PACKAGE + ".rest", "system")) {
            entites.add(new Service(entry));
        }

        for (ServiceClassDescriptor entry : ResourceLoader.getServices()) {
            entites.add(new Service(entry));
        }

        int count = entites.size();
        int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
        List<Service> page = new ArrayList<Service>();

        int firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
        int lastRec = firstRec + pageSize;
        if (lastRec > count) {
            lastRec = count;
        }

        for (int i1 = firstRec; i1 < lastRec; i1++) {
            page.add(entites.get(i1));
        }

        return new ViewPage<>(page, count, maxPage, pageNum);
    }

    public ViewPage<Service> findExternal(int pageNum, int pageSize) {
        List<Service> entites = new ArrayList<Service>();


        for (ServiceClassDescriptor entry : ResourceLoader.getServices()) {
            if (entry.isExternalService) {
                entites.add(new Service(entry));
            }
        }

        int count = entites.size();
        List<Service> page = new ArrayList<Service>();
        int firstRec = 0;
        int lastRec = count;
        int maxPage = 0;
        if (pageNum > 0 || pageSize > 0) {
            maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
            firstRec = RuntimeObjUtil.calcStartEntry(pageNum, pageSize);
            lastRec = firstRec + pageSize;
            if (lastRec > count) {
                lastRec = count;
            }
        }

        for (int i1 = firstRec; i1 < lastRec; i1++) {
            page.add(entites.get(i1));
        }

        return new ViewPage<>(page, count, maxPage, pageNum);
    }


    public Service findByClassName(String className) {

        for (ServiceClassDescriptor entry : ServicesHelper.getModuleServiceMethods(EnvConst.MAIN_PACKAGE + ".rest", "system")) {
            if (entry.getName().equals(className)) {
                return new Service(entry);
            }
        }

        for (ServiceClassDescriptor entry : ResourceLoader.getServices()) {
            if (entry.getName().equals(className)) {
                return new Service(entry);
            }
        }

        return null;
    }
}
