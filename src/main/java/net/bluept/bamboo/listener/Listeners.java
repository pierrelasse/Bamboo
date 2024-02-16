package net.bluept.bamboo.listener;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.challenges.forceitembattle.ItemService;
import net.bluept.bamboo.services.dep.backpack.BackpackService;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.services.system.emoji.StaticEmoji;
import net.bluept.bamboo.util.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

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

        switch (event.getMessage()) {
            case "!bp" -> {
                BackpackService backpackService = Bamboo.INS.serviceManager.getService(BackpackService.class);
                if (backpackService != null && backpackService.inventory != null) {
                    event.getPlayer().openInventory(backpackService.inventory);
                    return;
                }
            }
            case "!tengoku", "!tengoku2" -> {
                final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                Objective objective;
                if ((objective = scoreboard.getObjective("tengoku")) == null) {
                    objective = scoreboard.registerNewObjective("tengoku", "dummy");
                }
                objective.displayName(Component.text(event.getMessage().equals("!tengoku") ? "\u7013" : "\u7012"));
                objective.getScore(" ").setScore(1);
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.setScoreboard(scoreboard);
                }

                return;
            }
        }

        event.setMessage(StaticEmoji.translateEmojis(event.getMessage()));

        final String message = Utils.colorfy(event.getPlayer().getName() + "&8: &f" + event.getMessage());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }

    @EventHandler
    private void event(final PlayerJoinEvent event) {
        event.joinMessage(null);
        Bamboo.INS.logDev(event.getPlayer().getName() + " joined");
    }

    @EventHandler
    private void event(final PlayerQuitEvent event) {
        event.quitMessage(null);
        Bamboo.INS.logDev(event.getPlayer().getName() + " left");
    }
}
