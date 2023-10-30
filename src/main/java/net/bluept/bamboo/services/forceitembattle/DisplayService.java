package net.bluept.bamboo.services.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.util.DisplayHelper;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.services.translation.TranslationService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(name = "forceitembattle/display")
public class DisplayService extends Service {
    public BossBar timerBossbar;
    public BukkitTask tickTask;
    public int animationTick;

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
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
        timerBossbar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SOLID);
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

            // Actionbar
            ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
            if (itemService != null) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() != GameMode.SPECTATOR) {
                        displayActionbar(itemService, player);
                    }
                    timerBossbar.addPlayer(player);
                }
            }
        } else {
            timerBossbar.setVisible(false);
        }
    }

    @SuppressWarnings("deprecation")
    public void displayActionbar(ItemService itemService, Player player) {
        int items = itemService.getPlayerItems(player.getUniqueId());
        player.sendActionBar(Utils.colorfy("&d" + items + " &8- &d" + TranslationService.translatePlayerItem(itemService, player)));
    }
}
