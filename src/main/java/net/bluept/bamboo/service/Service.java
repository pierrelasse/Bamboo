package net.bluept.bamboo.service;

public abstract class Service {
    protected boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        if (enabled != state) {
            enabled = state;
            if (enabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
