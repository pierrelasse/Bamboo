package net.bluept.bamboo.services.dimtp;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.animprovider.AnimProviderService;
import net.bluept.bamboo.services.timer.TimerService;
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
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0L, 20L);
        timerBossbar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
        timerBossbar.setProgress(0);
        timerBossbar.setVisible(true);
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
            String leftAnim = null;
            String rightAnim = null;
            AnimProviderService animProviderService = Bamboo.INS.serviceManager.getService(AnimProviderService.class);
            if (animProviderService != null) {
                animationTick = (animationTick + 1) % 6;
                leftAnim = animProviderService.timerAnimation(animationTick, false);
                rightAnim = animProviderService.timerAnimation(animationTick, true);
            }
            String time = (animProviderService != null) ? animProviderService.convertSecondsToDuration(timerService.time) : String.valueOf(timerService.time);
            timerBossbar.setTitle(Utils.colorfy(leftAnim + "&d&l" + time + rightAnim));

            Bukkit.getOnlinePlayers().forEach(timerBossbar::addPlayer);
        } else {
            timerBossbar.setVisible(false);
        }
    }
}
