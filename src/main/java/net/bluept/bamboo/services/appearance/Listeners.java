package net.bluept.bamboo.services.appearance;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.bluept.bamboo.Bamboo;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {
    @EventHandler
    private void event(final PaperServerListPingEvent event) {
        AppearanceService appearanceService = Bamboo.INS.serviceManager.getService(AppearanceService.class);
        if (appearanceService == null) return;

        event.getPlayerSample().clear();
        event.setVersion("Bamboo @ " + Bukkit.getVersion());
        if (appearanceService.motd != null) event.motd(appearanceService.motd);
        if (appearanceService.serverIcon != null) event.setServerIcon(appearanceService.serverIcon);
    }

    @EventHandler
    private void event(final PlayerJoinEvent event) {
        AppearanceService appearanceService = Bamboo.INS.serviceManager.getService(AppearanceService.class);
        if (appearanceService == null) return;
        event.getPlayer().setResourcePack(appearanceService.pack_url, appearanceService.pack_sha1);
    }
}
