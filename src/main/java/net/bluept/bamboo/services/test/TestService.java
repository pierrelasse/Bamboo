package net.bluept.bamboo.services.test;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TestService extends Service {
    private BukkitTask tickTask;
    private int stage = 0;

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        if (tickTask != null) {
            tickTask.cancel();
        }
    }

    @Override
    public void onTest() {
        startTest();
    }

    public void startTest() {
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 1);
    }

    private void tick() {
        switch (++stage) {
            case 1:
                sendToAll("&7&oSystem test started");
                break;

            case 2:
                onDisable();
                sendToAll("&aTest done");
                break;
        }
    }

    private void sendToAll(String s) {
        String message = Utils.colorfy(s);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }
}
