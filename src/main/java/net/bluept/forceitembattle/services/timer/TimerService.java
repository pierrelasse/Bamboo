package net.bluept.forceitembattle.services.timer;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.actionbar.ActionbarService;
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
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(ForceItemBattle.INSTANCE, this::tick, 0L, 20L);
        ForceItemBattle.INSTANCE.getConfig().getLong("timer.time", 0);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
        ForceItemBattle.INSTANCE.getConfig().set("timer.time", time);
    }

    public void tick() {
        if (time > 0) {
            time--;
        } else {
            resumed = false;
        }
        ForceItemBattle.INSTANCE.serviceManager.getAndRun("actionbar", ActionbarService.class, serv -> serv.update(time));
    }
}
