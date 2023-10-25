package net.bluept.forceitembattle.service;

public abstract class Service {
    protected boolean isEnabled = false;

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        if (isEnabled != enabled) {
            isEnabled = enabled;

            if (isEnabled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
