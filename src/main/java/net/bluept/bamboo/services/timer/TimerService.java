package net.bluept.bamboo.services.timer;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.forceitembattle.DisplayService;
import net.bluept.bamboo.services.forceitembattle.ItemService;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TimerService extends Service {
    public BukkitTask tickTask;
    public boolean resumed;
    public long time;
    public boolean countDown = false;

    public TimerService() {
        resumed = false;
    }

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0L, 20L);
        time = Bamboo.INS.getConfig().getLong("timer.time", 0);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
        Bamboo.INS.getConfig().set("timer.time", time);
    }

    public void tick() {
        if (resumed) {
            if (time > 0) {
                if (countDown) {
                    time--;
                    if (time == 5) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:bang", SoundCategory.PLAYERS, 1F, 1F);
                        }
                    }
                } else {
                    time++;
                }
            } else {
                resumed = false;
            }
        }
    }

    public void setTime(long time) {
        this.time = time;

        ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
        DisplayService displayService = Bamboo.INS.serviceManager.getService(DisplayService.class);
        if (itemService != null && displayService != null) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                displayService.updatePlayer(itemService, onlinePlayer);
            }
        }
    }
}
