package net.bluept.forceitembattle.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ServiceManager {
    private final Map<String, Service> services;

    public ServiceManager() {
        services = new HashMap<>();
    }

    public void registerService(String id, Service service) {
        if (services.containsKey((id))) {
            throw new ServiceException("Services with id '" + id + "' already exists");
        }
        services.put(id, service);
    }

    public Service getService(String id) {
        Service service = services.get(id);
        if (service == null) {
            throw new ServiceException("Service with id '" + id + "' not found");
        }
        return service;
    }

    public boolean unregisterService(String id) {
        return services.remove(id) != null;
    }

    public boolean hasService(String id) {
        return services.containsKey(id);
    }

    public void startService(String id) {
        getService(id).startService();
    }

    public void stopService(String id) {
        getService(id).stopService();
    }

    public List<String> getServices() {
        return services.keySet().stream().toList();
    }

    public <T> T getServiceHandle(String id, Class<T> clazz) {
        Service service = services.get(id);
        if (clazz.isInstance(service)) {
            return clazz.cast(service);
        }
        return null;
    }
}
