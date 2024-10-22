package net.bluept.bamboo.services.system.appearance;

import net.bluept.bamboo.Bamboo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {
    @EventHandler
    private void event(final PlayerJoinEvent event) {
        AppearanceService appearanceService = Bamboo.INS.serviceManager.getService(AppearanceService.class);
        if (appearanceService == null) return;
        event.getPlayer().setResourcePack(appearanceService.pack_url, appearanceService.pack_sha1);
    }
}
