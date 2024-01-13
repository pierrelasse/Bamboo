package net.bluept.bamboo.services.dep.backpack;

import net.bluept.bamboo.Bamboo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class Listeners implements Listener {
    @EventHandler
    private void event(final PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
        BackpackService backpackService = Bamboo.INS.serviceManager.getService(BackpackService.class);
        if (backpackService != null) {
            event.getPlayer().openInventory(backpackService.inventory);
        }
    }
}
