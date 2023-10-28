package net.bluept.bamboo.listener;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.forceitembattle.ItemService;
import net.bluept.bamboo.services.timer.TimerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Listeners implements Listener {
    @EventHandler
    public void event(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
            ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
            if (timerService != null && timerService.resumed && itemService != null) {
                itemService.handlePickup(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(InventoryClickEvent event) {
        if (!event.isCancelled() && event.getWhoClicked() instanceof Player) {
            TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
            ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
            if (timerService != null && timerService.resumed && itemService != null) {
                itemService.handleClick(event);
            }
        }
    }
}
