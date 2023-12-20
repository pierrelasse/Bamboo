package net.bluept.bamboo.listener;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.backpack.BackpackService;
import net.bluept.bamboo.services.emoji.StaticEmoji;
import net.bluept.bamboo.services.forceitembattle.ItemService;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
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
    @EventHandler(priority = EventPriority.HIGHEST)
    public void event(final PlayerChatEvent event) {
        if (event.isCancelled()) {
            event.getPlayer().sendMessage("Cancelled");
            return;
        }
        event.setCancelled(true);

        if (event.getMessage().equalsIgnoreCase("bp")) {
            BackpackService backpackService = Bamboo.INS.serviceManager.getService(BackpackService.class);
            if (backpackService != null && backpackService.inventory != null) {
                event.getPlayer().openInventory(backpackService.inventory);
                return;
            }
        }

        event.setMessage(StaticEmoji.translateEmojis(event.getMessage()));

        final String message = Utils.colorfy(event.getPlayer().getName() + "&8: &f" + event.getMessage());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }
}
