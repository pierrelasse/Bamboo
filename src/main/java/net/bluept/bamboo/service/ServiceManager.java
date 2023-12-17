package net.bluept.bamboo.service;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.util.Utils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

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
        return Service.getId(clazz);
    }

    public String registerService(Service service) {
        String id = getServiceId(service);
        if (services.containsValue(service) || services.containsKey(id)) {
            throw new ServiceException("Services with id '" + id + "' already exists");
        }
        services.put(id, service);
        return id;
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

    public boolean startService(String id) {
        long startTime = System.nanoTime();
        Service service = getServiceF(id);
        if (service != null && service.setEnabled(true)) {
            Bamboo.INS.logDev("Service '" + id + "' started (" + (System.nanoTime() - startTime) + "ns)");
            return true;
        }
        return false;
    }

    public boolean stopService(String id) {
        long startTime = System.nanoTime();
        Service service = getServiceF(id);
        if (service != null && service.setEnabled(false)) {
            Bamboo.INS.logDev("Service '" + id + "' stopped (" + (System.nanoTime() - startTime) + "ns)");
            return true;
        }
        return false;
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

    public <T extends Service> T getService(Class<T> clazz, Audience audience) {
        String id = getServiceId(clazz);
        Service service = getService(id);
        if (clazz.isInstance(service)) {
            return clazz.cast(service);
        } else {
            audience.sendMessage(Component.text(Utils.colorfy("&cUnable to connect to service &4" + id)));
            return null;
        }
    }
}
