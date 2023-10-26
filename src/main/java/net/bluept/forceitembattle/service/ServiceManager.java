package net.bluept.forceitembattle.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ServiceManager {
    private final Map<String, Service> services;

    public ServiceManager() {
        services = new HashMap<>();
    }

    public String getServiceId(Service service) {
        return getServiceId(service.getClass());
    }

    public String getServiceId(Class<? extends Service> clazz) {
        String className = clazz.getSimpleName();
        int lastIndex = className.lastIndexOf("Service");
        return (lastIndex != -1 ? className.substring(0, lastIndex) : className).toLowerCase();
    }

    public void registerService(Service service) {
        String id = getServiceId(service);
        if (services.containsValue(service) || services.containsKey(id)) {
            throw new ServiceException("Services with id '" + id + "' already exists");
        }
        services.put(id, service);
    }

    public boolean unregisterService(String id) {
        return services.remove(id) != null;
    }

    public boolean hasService(String id) {
        return services.containsKey(id);
    }

    public void startService(String id) {
        getServiceF(id).setEnabled(true);
    }

    public void stopService(String id) {
        getServiceF(id).setEnabled(false);
    }

    public List<String> getServices() {
        return services.keySet().stream().toList();
    }

    public <T extends Service> T getService(Class<T> clazz) {
        Service service = getService(getServiceId(clazz));
        return clazz.isInstance(service) ? clazz.cast(service) : null;
    }

    public Service getService(String id) {
        Service service = getServiceF(id);
        if (!service.isEnabled()) {
            return null;
        }
        return service;
    }

    private Service getServiceF(String id) {
        Service service = services.get(id);
        if (service == null) {
            throw new ServiceException("Service with id '" + id + "' not found");
        }
        return service;
    }

    public <T extends Service> void getAndRun(Class<T> clazz, Consumer<T> run) {
        T service = getService(clazz);
        if (service != null) {
            run.accept(service);
        }
    }
}
