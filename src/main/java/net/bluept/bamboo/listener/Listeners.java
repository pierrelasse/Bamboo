package net.bluept.bamboo.listener;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.emoji.StaticEmoji;
import net.bluept.bamboo.services.forceitembattle.ItemService;
import net.bluept.bamboo.services.timer.TimerService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;

public class Listeners implements Listener {
    @EventHandler
    public void event(final EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
            if (itemService != null && TimerService.isResumed()) {
                itemService.handlePickup(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(final InventoryClickEvent event) {
        if (!event.isCancelled() && event.getWhoClicked() instanceof Player) {
            ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
            if (itemService != null && TimerService.isResumed()) {
                itemService.handleClick(event);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void event(final PlayerChatEvent event) {
        StaticEmoji.handleChatEvent(event);
    }
}
