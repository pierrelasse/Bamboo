package net.bluept.bamboo.services.dimtp;

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

@ServiceInfo(name = "dimtp/display")
public class DisplayService extends Service {
    public BossBar timerBossbar;
    public BukkitTask tickTask;
    public int animationTick;

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
        timerBossbar = Bukkit.createBossBar("a", BarColor.GREEN, BarStyle.SOLID);
        timerBossbar.setProgress(0);
        animationTick = 0;
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
            // Bossbar
            animationTick = (animationTick + 1) % 6;
            timerBossbar.setTitle(Utils.colorfy(DisplayHelper.timerAnimation(animationTick, false) + "&d&l" + DisplayHelper.convertSecondsToDuration(timerService.time) + DisplayHelper.timerAnimation(animationTick, true)));
            timerBossbar.setVisible(true);

            Bukkit.getOnlinePlayers().forEach(timerBossbar::addPlayer);
        } else {
            timerBossbar.setVisible(false);
        }
    }
}
