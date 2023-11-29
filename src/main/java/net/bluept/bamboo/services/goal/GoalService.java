package net.bluept.bamboo.services.goal;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import org.bukkit.event.HandlerList;

public class GoalService extends Service {
    private Listeners listeners;

    @Override
    public void onEnable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
        }
        listeners = new Listeners();
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners, Bamboo.INS);
    }

    @Override
    public void onDisable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
    }
}
