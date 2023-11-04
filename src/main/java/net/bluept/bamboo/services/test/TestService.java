package net.bluept.bamboo.services.test;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(name = "Testing", description = "Tests if the resourcepack is working for all players")
public class TestService extends Service {
    private BukkitTask tickTask;
    private int stage = 0;
    private BossBar timerBossbar;

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        if (tickTask != null) {
            tickTask.cancel();
            tickTask = null;
        }
        clearBossbar();
    }

    @Override
    public void onTest() {
        startTest();
    }

    public void startTest() {
        if (tickTask == null) {
            onDisable();
            stage = 0;
            tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 1);
        }
    }

    private void tick() {
        switch (++stage) {
            case 1 -> {
                sendToAll("&7&oSystem test started");
            }
            case 5 -> {
                sendToAll("&8&oStarting bossbar test");
                timerBossbar = Bukkit.createBossBar(Utils.colorfy("&4&k## &c&lDU SOLLTEST NUR DIESEN TEXT SEHEN! &4&k##"), BarColor.GREEN, BarStyle.SOLID);
                timerBossbar.setProgress(0);
                Bukkit.getOnlinePlayers().forEach(timerBossbar::addPlayer);
            }
            case 60 -> {
                clearBossbar();
                sendToAll("&2Bossbar test done");
            }
            case 70 -> {
                sendToAll("&8&oStarting char test");
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Utils.title(onlinePlayer, "\u7004", Utils.colorfy("&cEtwas ist schiefgelaufen!"), 5, 20, 5);
                }
            }
            case 100 -> {
                sendToAll("&2Char test done\n&8&oStarting sound test");
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:test", SoundCategory.VOICE, 1F, 1F);
                }
            }
            case 120 -> {
                sendToAll("&2Sound test done");
            }
            case 140 -> {
                onDisable();
                sendToAll("&aTest done");
            }
        }
    }

    private void sendToAll(String s) {
        String message = Utils.colorfy(s);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }

    private void clearBossbar() {
        if (timerBossbar != null) {
            timerBossbar.removeAll();
            timerBossbar = null;
        }
    }
}
