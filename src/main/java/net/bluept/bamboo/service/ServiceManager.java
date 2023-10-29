package net.bluept.bamboo.service;

import net.bluept.bamboo.Bamboo;

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
        ServiceInfo info = clazz.getAnnotation(ServiceInfo.class);
        if (info != null) {
            return info.name();
        }
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

    public boolean unregisterService(Class<? extends Service> clazz) {
        return unregisterService(getServiceId(clazz));
    }

    public boolean unregisterService(String id) {
        stopService(id);
        return services.remove(id) != null;
    }

    public boolean hasService(String id) {
        return services.containsKey(id);
    }

    public void startService(String id) {
        long startTime = System.nanoTime();
        Service service = getServiceF(id);
        if (service != null && service.setEnabled(true)) {
            Bamboo.INS.getLogger().info("Service '" + id + "' started (" + (System.nanoTime() - startTime) + "ns)");
        }
    }

    public void stopService(String id) {
        long startTime = System.nanoTime();
        Service service = getServiceF(id);
        if (service != null && service.setEnabled(false)) {
            Bamboo.INS.getLogger().info("Service '" + id + "' stopped (" + (System.nanoTime() - startTime) + "ns)");
        }
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
        if (service == null || !service.isEnabled()) {
            return null;
        }
        return service;
    }

    public Service getServiceF(String id) {
        return services.get(id);
    }

    public <T extends Service> void getAndRun(Class<T> clazz, Consumer<T> run) {
        T service = getService(clazz);
        if (service != null) {
            run.accept(service);
        }
    }
}
