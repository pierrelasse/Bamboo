package net.bluept.bamboo.services.dep.display;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(description = "Handles the bossbar timer")
public class DisplayService extends Service {
    public final String timerFormat = "<color:#FF38D0>>>> </color> <transition:#FF38D0:#FF3033:%p>%t</transition> <color:#FF38D0> <<<</color>";
    public BossBar timerBossbar;
    public BukkitTask tickTask;
    public double phase = 0;

    @Override
    public void onEnable() {
        timerBossbar = BossBar.bossBar(Component.text(), 0, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 2);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hideBossBar(timerBossbar);
        }
        timerBossbar = null;
    }

    public void tick() {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService != null && timerService.resumed) {
            phase += .02;
            if (phase > 1) {
                phase = -1;
            }
            timerBossbar.name(MiniMessage.miniMessage().deserialize(
                    timerFormat
                            .replace("%p", Double.toString(phase))
                            .replace("%-p", Double.toString(-phase))
                            .replace("%t", Utils.convertSecondsToDuration(timerService.time))
            ));

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showBossBar(timerBossbar);
            }

        } else {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.hideBossBar(timerBossbar);
            }
        }
    }
}
