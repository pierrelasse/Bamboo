package net.bluept.forceitembattle.listener;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.services.timer.TimerService;
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
            TimerService timerService = ForceItemBattle.INS.serviceManager.getService(TimerService.class);
            ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
            if (timerService != null && timerService.resumed && itemService != null) {
                itemService.handlePickup(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(InventoryClickEvent event) {
        if (!event.isCancelled() && event.getWhoClicked() instanceof Player) {
            TimerService timerService = ForceItemBattle.INS.serviceManager.getService(TimerService.class);
            ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
            if (timerService != null && timerService.resumed && itemService != null) {
                itemService.handleClick(event);
            }
        }
    }
}
