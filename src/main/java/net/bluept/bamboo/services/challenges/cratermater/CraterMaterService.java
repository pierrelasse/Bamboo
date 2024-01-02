package net.bluept.bamboo.services.challenges.cratermater;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import org.bukkit.event.HandlerList;

public class CraterMaterService extends Service {
    private Listeners listeners;

    @Override
    public void onEnable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners = new Listeners(), Bamboo.INS);
    }

    @Override
    public void onDisable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
    }
}
