package net.bluept.bamboo.services.forcemobbattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.display.DisplayController;
import org.bukkit.event.HandlerList;

public class ForceMobBattleService extends Service {
    private Listeners listeners;

    @Override
    public void onEnable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }
        Bamboo.INS.getServer().getPluginManager().registerEvents(listeners = new Listeners(), Bamboo.INS);

        DisplayController.push();
    }

    @Override
    public void onDisable() {
        if (listeners != null) {
            HandlerList.unregisterAll(listeners);
            listeners = null;
        }

        DisplayController.pop();
    }
}
