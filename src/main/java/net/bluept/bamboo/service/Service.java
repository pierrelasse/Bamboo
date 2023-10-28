package net.bluept.bamboo.service;

public abstract class Service {
    protected boolean enabled = false;

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
}
