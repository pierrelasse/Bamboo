package net.bluept.forceitembattle.services.timer;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TimerService extends Service {
    public BukkitTask tickTask;
    public boolean resumed;
    public long time;
    private List<Consumer<Long>> timerTickListeners;

    public TimerService() {
        resumed = false;
        time = 0;
        timerTickListeners = new ArrayList<>();
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
        time--;
        if (time <= 0) {
            resumed = false;
        }
        for (Consumer<Long> listener : timerTickListeners) {
            listener.accept(time);
        }
    }

    public void registerTimerTickListener(Consumer<Long> consumer) {
        timerTickListeners.add(consumer);
    }
    public void unregisterTimerTickListener(Consumer<Long> consumer) {
        timerTickListeners.remove(consumer);
    }
}
