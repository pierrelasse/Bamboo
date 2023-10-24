package net.bluept.forceitembattle.services.timer;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class TimerService extends Service {
    public BukkitTask tickTask;
    public boolean resumed;
    public long time;

    public TimerService() {
        resumed = false;
        time = 0;
    }

    @Override
    public void start() {
        tickTask = Bukkit.getScheduler().runTaskTimer(ForceItemBattle.INSTANCE, this::tick, 0L, 20L);
    }

    @Override
    public void stop() {
        tickTask.cancel();
    }

    public void tick() {
        time++;
    }
}
