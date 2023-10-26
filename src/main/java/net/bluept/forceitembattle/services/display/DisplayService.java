package net.bluept.forceitembattle.services.display;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.services.timer.TimerService;
import net.bluept.forceitembattle.services.translation.TranslationService;
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
        tickTask = Bukkit.getScheduler().runTaskTimer(ForceItemBattle.INS, this::tick, 0L, 20L);
        timerBossbar = Bukkit.createBossBar(Utils.colorfy("&8Loading..."), BarColor.GREEN, BarStyle.SOLID);
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
        TimerService timerService = ForceItemBattle.INS.serviceManager.getService(TimerService.class);
        if (timerService != null && timerService.resumed) {
            // Bossbar
            animationTick++;
            timerBossbar.setTitle(Utils.colorfy(genAnimation(false) + " &d&l" + convertSecondsToDuration(timerService.time) + " &r" + genAnimation(true)));
            timerBossbar.setVisible(true);

            // Actionbar
            ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
            if (itemService != null) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePlayer(itemService, player);
                }
            }
        } else {
            timerBossbar.setVisible(false);
        }
    }

    @SuppressWarnings("deprecation")
    public void updatePlayer(ItemService itemService, Player player) {
        int items = itemService.getPlayerItems(player.getUniqueId());
        player.sendActionBar(Utils.colorfy("&d" + items + " &8- &d" + TranslationService.translatePlayerItem(itemService, player)));
        timerBossbar.addPlayer(player);
    }

    public String genAnimation(boolean right) {
        if (animationTick > 6) {
            animationTick = 0;
        }
        if (right) {
            return switch (animationTick) {
                case 1 -> "&d<<<";
                case 2 -> "&d<<&5<";
                case 3 -> "&d<&5<<";
                case 4 -> "&5<<<";
                case 5 -> "&5<<&d<";
                default -> "&5<&d<<";
            };
        } else {
            return switch (animationTick) {
                case 1 -> "&d>>>";
                case 2 -> "&5>&d>>";
                case 3 -> "&5>>&d>";
                case 4 -> "&5>>>";
                case 5 -> "&d>&5>>";
                default -> "&d>>&5>";
            };
        }
    }
}
