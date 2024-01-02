package net.bluept.bamboo.services.challenges.cratermater;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CraterMaterService extends Service implements Listener {
    private boolean ok = false;
    private int i = 0;
    private float limit = 2;

    private static long delay() {
//        return Utils.RANDOM.nextInt(10, 20);
//        return Utils.RANDOM.nextBoolean() ? 1 : 2;
//        return 1;
        return 5;
    }

    @Override
    public void onEnable() {
        HandlerList.unregisterAll(this);
        Bamboo.INS.getServer().getPluginManager().registerEvents(this, Bamboo.INS);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
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

        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(-1, 0, 0)), delay());
            Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(0, -1, 0)), delay());
            Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(0, 0, -1)), delay());
            Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(1, 0, 0)), delay());
            Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(0, 1, 0)), delay());
            Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> cool(location.clone().add(0, 0, 1)), delay());
        }
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
