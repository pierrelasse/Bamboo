package net.bluept.forceitembattle.service;

public abstract class Service {
    protected boolean running = false;

    public void startService() {
        start();
        running = true;
    }

    public void stopService() {
        stop();
        running = false;
    }

    public abstract void start();

    public abstract void stop();
}
