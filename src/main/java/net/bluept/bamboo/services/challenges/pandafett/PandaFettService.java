package net.bluept.bamboo.services.challenges.pandafett;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.dep.timer.TimerService;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class PandaFettService extends Service {
    private int tick;
    private BukkitTask tickTask;

    @Override
    public void onEnable() {
        tick = 0;
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
    }

    @Override
    public void onDisable() {
        if (tickTask != null) {
            tickTask.cancel();
            tickTask = null;
        }
    }

    public void tick() {
        if (!TimerService.isResumed()) {
            return;
        }

        tick++;
        if (tick >= 2) {
            tick = 0;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getGameMode() != GameMode.SURVIVAL) continue;
                fett(onlinePlayer.getLocation().add(1, 0, 0));
                fett(onlinePlayer.getLocation().add(0, 0, 1));
                fett(onlinePlayer.getLocation());
                fett(onlinePlayer.getLocation().add(-1, 0, 0));
                fett(onlinePlayer.getLocation().add(0, 0, -1));
            }
        }
    }

    private void fett(Location loc) {
        Block block1 = loc.add(0, -1, 0).getBlock();
        if (block1.getType().isAir()) return;
        Block block2 = loc.add(0, -2, 0).getBlock();

        block2.setType(block1.getType());
        block2.setBlockData(block1.getBlockData());
        block1.setType(Material.AIR);

    }
}
