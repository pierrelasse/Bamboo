package net.bluept.bamboo.listener;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.dep.backpack.BackpackService;
import net.bluept.bamboo.services.system.emoji.StaticEmoji;
import net.bluept.bamboo.services.challenges.forceitembattle.ItemService;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
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

    private static boolean ok = false;
    private static int i = 0;
    private static float limit = 2;

    private static long delay() {
//        return Utils.RANDOM.nextInt(10, 20);
//        return Utils.RANDOM.nextBoolean() ? 1 : 2;
        return 1;
    }

    @EventHandler
    private void event(final BlockBreakEvent event) {
        if (!event.getPlayer().isOnline()) return;

        ok = true;

        i = 0;
        limit *= 1.2;
        limit = Math.min(limit, 1000000);
        cool(event.getBlock().getLocation());
    }

    @SuppressWarnings("deprecation")
    private void cool(Location location) {
        if (!ok) return;

        final Block block = location.getBlock();
        if (block.isEmpty() || block.getType() == Material.END_STONE) return;

        if (i > limit) {
            ok = false;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendActionBar(Utils.colorfy("&bLimit reached: " + i));
            }
            return;
        }

        i++;
        block.breakNaturally();
//        location.getBlock().setType(Material.AIR);

        Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(-1, 0, 0)), delay());
        Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(0, -1, 0)), delay());
        Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(0, 0, -1)), delay());
        Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(1, 0, 0)), delay());
        Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(0, 1, 0)), delay());
        Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(0, 0, 1)), delay());
    }

    @EventHandler
    private void event(final PlayerToggleSneakEvent event) {
        if (ok && event.getPlayer().isSneaking() && !event.getPlayer().isFlying()) {
            ok = false;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Utils.send(onlinePlayer, "&cCancelled by &4" + event.getPlayer().getName());
            }
        }
    }
}
