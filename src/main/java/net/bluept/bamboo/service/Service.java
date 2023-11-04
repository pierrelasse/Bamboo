package net.bluept.bamboo.service;

import java.util.Objects;

public abstract class Service {
    public final String name;
    public final String description;
    protected boolean enabled = false;

    public Service() {
        ServiceInfo info = getClass().getAnnotation(ServiceInfo.class);

        String name = null;
        String description = null;

        if (info != null) {
            name = info.name();
            description = info.description();
            if (Objects.equals(description, "")) {
                description = null;
            }
        }

        if (name == null || name.equals("")) {
            name = getId(getClass());
        }

        this.name = name;
        this.description = description;
    }

    public static String getId(Class<? extends Service> clazz) {
        ServiceInfo info = clazz.getAnnotation(ServiceInfo.class);
        if (info != null) {
            String id = info.id();
            if (!Objects.equals(id, "")) {
                return id;
            }
        }
        String className = clazz.getSimpleName();
        int lastIndex = className.lastIndexOf("Service");
        return (lastIndex != -1 ? className.substring(0, lastIndex) : className).toLowerCase();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean setEnabled(boolean state) {
        if (enabled != state) {
            enabled = state;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
            return true;
        }
        return false;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public void onTest() {
    }
}
