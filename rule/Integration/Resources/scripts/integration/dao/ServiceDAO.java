package integration.dao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.exponentus.rest.IRestService;
import com.exponentus.rest.ResourceLoader;
import com.exponentus.rest.ServiceDescriptor;
import com.exponentus.scripting._Session;
import com.exponentus.server.Server;

import integration.model.Service;

public class ServiceDAO {
	private List<String> services;

	public ServiceDAO(_Session ses) {
		services = ResourceLoader.getLoaded();
	}

	public long getCount() {
		return services.size();
	}

	public List<Service> findAll(int startRec, int pageSize) {
		List<Service> entites = new ArrayList<Service>();

		for (String className : services) {
			try {
				Class<?> clazz = Class.forName(className);
				Constructor<?> contructor = clazz.getConstructor();
				IRestService serv = (IRestService) contructor.newInstance();
				ServiceDescriptor descr = serv.getDescription();
				entites.add(new Service(descr));
			} catch (ClassNotFoundException e) {
				Server.logger.warningLogEntry("Service has not been initialized (" + className + ")");
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
			        | SecurityException e) {
				Server.logger.errorLogEntry(e);
			}
		}

		return entites;
	}

}
