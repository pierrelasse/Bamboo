package net.bluept.forceitembattle.services.timer;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.display.DisplayService;
import net.bluept.forceitembattle.services.item.ItemService;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
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
        if (resumed) {
            if (time > 0) {
                time--;
                if (time == 5) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:bang", SoundCategory.PLAYERS, 1F, 1F);
                    }
                }
            } else {
                resumed = false;
            }
        }
    }

    public void setTime(long time) {
        this.time = time;

        ItemService itemService = ForceItemBattle.INSTANCE.serviceManager.getService(ItemService.class);
        DisplayService displayService = ForceItemBattle.INSTANCE.serviceManager.getService(DisplayService.class);
        if (itemService != null && displayService != null) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                displayService.updatePlayer(itemService, onlinePlayer);
            }
        }
    }
}
