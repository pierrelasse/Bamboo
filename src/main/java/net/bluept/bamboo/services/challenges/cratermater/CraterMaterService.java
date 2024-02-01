package net.bluept.bamboo.services.challenges.cratermater;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.dep.display.DisplayController;
import net.bluept.bamboo.services.dep.timer.TimerService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.List;

@ServiceInfo(name = "Crater Mater", description = "POV: Panda pisst in die Welt")
public class CraterMaterService extends Service implements Listener {
    public static final int MAX_LIMIT = 50000 - 1;
    private static final List<Material> BLACKLISTED_BLOCKS = List.of(Material.END_PORTAL_FRAME, Material.END_PORTAL, Material.BEDROCK);
    public boolean ok;
    public int i;
    public float limit;

    @Override
    public void onEnable() {
        ok = false;
        i = 0;
        limit = 2;

        HandlerList.unregisterAll(this);
        Bamboo.INS.getServer().getPluginManager().registerEvents(this, Bamboo.INS);

        DisplayController.push();
    }

    @Override
    public void onDisable() {
        DisplayController.pop();
        HandlerList.unregisterAll(this);
    }

    private long delay() {
//        return Utils.RANDOM.nextInt(10, 20);
//        return Utils.RANDOM.nextBoolean() ? 1 : 2;
//        return 1;
        return 5;
    }

    @EventHandler
    private void event(final BlockBreakEvent event) {
        if (!event.getPlayer().isOnline() || event.getPlayer().isSneaking()) return;
        if (!TimerService.isResumed()) return;
        ok = true;
        i = 0;
        limit = Math.min(limit + 4, MAX_LIMIT);
        cool(event.getBlock().getLocation());
    }

    private void cool(Location location) {
        if (!ok) return;

        final Block block = location.getBlock();
        if (block.isEmpty() || BLACKLISTED_BLOCKS.contains(block.getType())) return;

        if (i > limit) {
            ok = false;
            Bamboo.INS.logDev("[CraterMater] Limit reached: " + i);
            return;
        }

        i++;
        if (!block.breakNaturally()) {
            location.getBlock().setType(Material.AIR);
        }

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
        if (!TimerService.isResumed()) return;
        if (ok && event.getPlayer().isSneaking() && !event.getPlayer().isFlying()) {
            ok = false;
            Bamboo.INS.logDev("[CraterMater] Cancelled by " + event.getPlayer().getName());
        }
    }
}
