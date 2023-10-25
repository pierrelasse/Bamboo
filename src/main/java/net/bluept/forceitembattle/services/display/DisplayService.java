package net.bluept.forceitembattle.services.display;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.timer.TimerService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class DisplayService extends Service {
    public BossBar timerBossbar;
    public BukkitTask tickTask;

    public static String convertSecondsToDuration(long seconds) {
        long days = seconds / (24 * 3600);
        long hours = (seconds % (24 * 3600)) / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        StringBuilder duration = new StringBuilder();

        if (days > 0) {
            duration.append(days).append("d ");
        }

        if (hours > 0 || days > 0) {
            duration.append(hours).append("h ");
        }

        if (minutes > 0 || hours > 0 || days > 0) {
            duration.append(minutes).append("m ");
        }

        duration.append(remainingSeconds).append("s");

        return duration.toString();
    }

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(ForceItemBattle.INSTANCE, this::tick, 0L, 20L);
        timerBossbar = Bukkit.createBossBar(Utils.colorfy("&8Loading..."), BarColor.PURPLE, BarStyle.SOLID);
        timerBossbar.setVisible(true);
    }

    @Override
    public void onDisable() {
        timerBossbar.removeAll();
        timerBossbar = null;
    }

    @SuppressWarnings("deprecation")
    public void tick() {
        // Bossbar
        TimerService timerService = ForceItemBattle.INSTANCE.serviceManager.getServiceHandle("timer", TimerService.class);
        if (timerService != null) {
            timerBossbar.setTitle(Utils.colorfy(convertSecondsToDuration(timerService.time)));
        } else {
            timerBossbar.setVisible(false);
        }

        // Actionbar
        int itemCount = -1;
        String item = "???";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(Utils.colorfy("&d" + itemCount + " &8- &d" + item));
            timerBossbar.addPlayer(player);
        }
    }
}
