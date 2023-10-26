package net.bluept.forceitembattle.listener;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.item.ItemService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickupListener implements Listener {
    @EventHandler
    public void event(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            ItemService itemService = ForceItemBattle.INSTANCE.serviceManager.getService(ItemService.class);
            if (itemService != null) {
                itemService.handlePickup(event);
            }
        }
    }
}
