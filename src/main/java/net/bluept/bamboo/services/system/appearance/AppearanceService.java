package net.bluept.bamboo.services.system.appearance;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import org.bukkit.event.HandlerList;

public class AppearanceService extends Service {
    public final String pack_url = "https://bluept.net/cdn2/Bamboo-Resources.zip";
    public final String pack_sha1 = "79bceeb09259117a744fb45a7d77a4b0d5ccb493";

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
