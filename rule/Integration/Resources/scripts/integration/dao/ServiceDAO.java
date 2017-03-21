package integration.dao;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.rest.ResourceLoader;
import com.exponentus.rest.services.ServiceClass;
import com.exponentus.scripting._Session;

import integration.model.Service;

public class ServiceDAO {
	private List<ServiceClass> services;

	public ServiceDAO(_Session ses) {
		services = ResourceLoader.getServices();
	}

	public long getCount() {
		return services.size();
	}

	public List<Service> findAll(int startRec, int pageSize) {
		List<Service> entites = new ArrayList<Service>();

		for (ServiceClass descr : services) {
			entites.add(new Service(descr));
		}

		return entites;
	}

}
