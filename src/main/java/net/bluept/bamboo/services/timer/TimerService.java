package net.bluept.bamboo.services.timer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

@ServiceInfo(description = "Handles the timer")
public class TimerService extends Service {
    public BukkitTask tickTask;
    public boolean resumed;
    public long time;
    public boolean countDown = false;
    public Config config;

    public TimerService() {
        resumed = false;
    }

    public static boolean isResumed() {
        final TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        return timerService != null && timerService.resumed;
    }

    @Override
    public void onEnable() {
        config = new Config(new File(Bamboo.INS.configRoot, "timer.yml"));
        config.load();

        time = config.get().getLong("time", 0);

        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
        config.get().set("time", time);
        config.saveSafe();
    }

    public void tick() {
        if (!resumed) {
            return;
        }

        if (!countDown) {
            time++;
            return;
        }

        if (time <= 0) {
            resumed = false;
            return;
        }

        time--;

        if (time == 5) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:bang", SoundCategory.VOICE, 1F, 1F);
            }
        }
    }
}
