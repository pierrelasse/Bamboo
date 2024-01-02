package net.bluept.bamboo.services.challenges.dimtp;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.dep.display.DisplayController;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(name = "DimensionTeleport")
public class DimTPService extends Service {
    public BukkitTask tickTask;
    public int tick = 0;
    public int genState = -1;
    public Location nextLocation;

    @Override
    public void onEnable() {
        Generator.newInterval();

        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);

        DisplayController.push();
    }

    @Override
    public void onDisable() {
        if (tickTask != null) {
            tickTask.cancel();
            tickTask = null;
        }

        DisplayController.pop();
    }

    public void tick() {
        if (!DimTPConfig.enabled || !TimerService.isResumed()) {
            return;
        }

        if (genState == -1) {
            tick++;
            if (tick > DimTPConfig.INTERVAL) {
                tick = 0;
                Generator.newInterval();
                if (Bukkit.getOnlinePlayers().size() > 0) {
                    genState = 0;
                }
            }
        } else {
            switch (genState) {
                case 0 -> {
                    genState = 1;
                    Bamboo.INS.logDev("[DimTP] Generating coords");
                    Bukkit.getScheduler().runTask(Bamboo.INS, () -> {
                        long startTime = System.currentTimeMillis();
                        Object[] data = Generator.getRandomLocation(Generator.randomDim(), 0);
                        if (data[0] instanceof Location location) {
                            nextLocation = location;
                            genState = 2;
                            Bamboo.INS.logDev("[DimTP] Location found after " + (System.currentTimeMillis() - startTime) + "ms and " + data[1] + " tries");
                            Bamboo.INS.logDev("[DimTP] Location: " + nextLocation.getWorld().getName() + " @ " + nextLocation.getBlockX() + "," + nextLocation.getBlockY() + "," + nextLocation.getBlockZ());
                        } else {
                            genState = -1;
                            Bamboo.INS.logDev("[DimTP] &cUnable to find random location after " + (System.currentTimeMillis() - startTime) + "ms and " + data[1] + " tries");
                        }
                    });
                }
                case 2 -> {
                    genState = 3;
                    final int stay = 5 + (20 * switch (nextLocation.getWorld().getName()) {
                        case "world" -> 12;
                        case "world_nether" -> 6;
                        case "world_the_end" -> 4;
                        default -> 0;
                    });
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        Utils.title(onlinePlayer, DimTPConfig.BLACKSCREEN_CHAR, DimTPConfig.EMPTY_STR, 10, stay, 23);
                        onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:beam", SoundCategory.VOICE, 1F, 1F);
                    }
                }
                case 3, 4, 5 -> {
                    if (genState != 5) {
                        genState++;
                        break;
                    }
                    genState = 6;
                    Bamboo.INS.logDev("[DimTP] Teleporting players");
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.teleport(nextLocation);
                    }
                }
                case 6 -> {
                    genState = -1;
                    Bamboo.INS.logDev("[DimTP] Done");
                }
            }
        }
    }
}
