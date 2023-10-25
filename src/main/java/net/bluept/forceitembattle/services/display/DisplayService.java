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

public class DisplayService extends Service {
    public BossBar timerBossbar;

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
        timerBossbar = Bukkit.createBossBar(Utils.colorfy("&8Loading..."), BarColor.PURPLE, BarStyle.SOLID);
        timerBossbar.setVisible(true);
    }

    @Override
    public void onDisable() {
        timerBossbar.removeAll();
        timerBossbar = null;
    }

    @SuppressWarnings("deprecation")
    public void update(long time) {
        int itemCount = -1;

        String timer;
        TimerService timerService = ForceItemBattle.INSTANCE.serviceManager.getServiceHandle("timer", TimerService.class);
        if (timerService != null) {
            timer = convertSecondsToDuration(timerService.time);
        } else {
            timer = "(failed to connect to timer service)";
        }
        timerBossbar.setTitle(Utils.colorfy("\n" + timer));

        String item = "(failed to connect to item service)";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(Utils.colorfy("&5x" + itemCount + " &d" + item));
            timerBossbar.addPlayer(player);
        }
    }
}
