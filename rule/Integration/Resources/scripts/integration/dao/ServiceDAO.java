package integration.dao;

import com.exponentus.common.ui.ViewPage;
import com.exponentus.dataengine.RuntimeObjUtil;
import com.exponentus.env.EnvConst;
import com.exponentus.rest.ResourceLoader;
import com.exponentus.rest.services.ServiceClass;
import com.exponentus.rest.util.ServicesHelper;
import com.exponentus.scripting.SortParams;
import com.exponentus.scripting._Session;
import integration.model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    private List<ServiceClass> services;

    public ServiceDAO(_Session ses) {
        services = ResourceLoader.getServices();
    }

    public ViewPage<Service> findViewPage(SortParams sortParams, int pageNum, int pageSize) {
        List<Service> entites = new ArrayList<Service>();

        for (ServiceClass entry : ServicesHelper.getAppTasks(EnvConst.MAIN_PACKAGE + ".rest", "system")) {
            entites.add(new Service(entry));
        }

        for (ServiceClass entry : ResourceLoader.getServices()) {
            entites.add(new Service(entry));
        }

        int count = entites.size();
        int maxPage = RuntimeObjUtil.countMaxPage(count, pageSize);
        return new ViewPage<>(entites, count, maxPage, pageNum);
    }
}
