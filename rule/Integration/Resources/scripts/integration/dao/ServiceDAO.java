package integration.dao;

import java.util.ArrayList;
import java.util.List;

import com.exponentus.rest.ResourceLoader;
import com.exponentus.rest.ServiceDescriptor;
import com.exponentus.scripting._Session;

import integration.model.Service;

public class ServiceDAO {
	private List<ServiceDescriptor> services;

	public ServiceDAO(_Session ses) {
		services = ResourceLoader.getLoaded();
	}

	public long getCount() {
		return services.size();
	}

	public List<Service> findAll(int startRec, int pageSize) {
		List<Service> entites = new ArrayList<Service>();

		for (ServiceDescriptor descr : services) {
			entites.add(new Service(descr));
		}

		return entites;
	}

}
