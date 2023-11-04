package net.bluept.bamboo.services.display;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.DisplayHelper;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(description = "Handles the bossbar timer")
public class DisplayService extends Service {
    public BossBar timerBossbar;
    public BukkitTask tickTask;
    public int animationTick;

    @Override
    public void onEnable() {
        timerBossbar = Bukkit.createBossBar(" ", BarColor.GREEN, BarStyle.SOLID);
        timerBossbar.setProgress(0);
        animationTick = 0;
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
        timerBossbar.removeAll();
        timerBossbar = null;
    }

    public void tick() {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService != null && timerService.resumed) {
            animationTick = (animationTick + 1) % 6;
            timerBossbar.setTitle(Utils.colorfy(DisplayHelper.timerAnimation(animationTick, false) + "&d&l" + DisplayHelper.convertSecondsToDuration(timerService.time) + DisplayHelper.timerAnimation(animationTick, true)));
            timerBossbar.setVisible(true);
            Bukkit.getOnlinePlayers().forEach(timerBossbar::addPlayer);
        } else {
            timerBossbar.setVisible(false);
        }
    }
}
