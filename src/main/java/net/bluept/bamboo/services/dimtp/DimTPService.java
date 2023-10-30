package net.bluept.bamboo.services.dimtp;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.dimtp.commands.DimTPDevCmd;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class DimTPService extends Service {
    public BukkitTask tickTask;
    public int tick;
    public DimTPDevCmd dimTPDevCmd;

    @Override
    public void onEnable() {
        Bamboo.INS.random.setSeed(System.currentTimeMillis());

        Generator.newInterval();

        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registerCommand(dimTPDevCmd = new DimTPDevCmd());
        }

        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.registerService(new DisplayService());
        for (String id : serviceManager.getServices()) {
            if (id.startsWith("dimtp/")) {
                serviceManager.startService(id);
            }
        }
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.unregisterCommand(dimTPDevCmd);
        }

        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.unregisterService(DisplayService.class);
    }

    @SuppressWarnings("deprecation")
    public void tick() {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService == null || !timerService.resumed) {
            return;
        }

        tick++;
        if (tick == DimTPConfig.INTERVAL - 1) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendTitle(DimTPConfig.BLACKSCREEN_CHAR, DimTPConfig.EMPTY_STR, 10, 55, 23);
                onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:beam", SoundCategory.VOICE, 1F, 1F);
            }

        } else if (tick >= DimTPConfig.INTERVAL) {
            tick = 0;

            Generator.newInterval();

            if (Bukkit.getOnlinePlayers().size() == 0) {
                return;
            }

            long startTime = System.currentTimeMillis();

            Object[] data = Generator.getRandomLocation(Generator.randomDim(), 0);
            if (data[0] == null) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Utils.send(onlinePlayer, "\n&cEs konnte nach " + data[1] + " Versuche/n immer noch kein zuf\u00E4lliger Ort gefunden werden!\n");
                }
                Bamboo.INS.getLogger().warning("DimTP: Unable to find random location after " + (System.currentTimeMillis() - startTime) + "ms and " + data[1] + " tries");
                return;
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.teleport((Location)data[0]);
            }
            Bamboo.INS.getLogger().info("DimTP: Done teleporting after " + (System.currentTimeMillis() - startTime) + "ms and " + data[1] + " tries");
        }
    }
}
