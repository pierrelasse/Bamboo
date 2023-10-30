package net.bluept.bamboo.services.kmswitch;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class KMSwitchService extends Service {
    public int tick;
    public int interval;
    private BukkitTask tickTask;

    @Override
    public void onEnable() {
        generateInterval();

        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
    }

    public void generateInterval() {
        interval = Bamboo.INS.random.nextInt(1, 5) * 60;
    }

    public void tick() {
        tick++;
        if (tick >= interval) {
            tick = 0;
            generateInterval();

            String message = "\n&8>>> &d&lJetzt wird die " + (Bamboo.INS.random.nextBoolean() ? "Maus" : "Tastatur") + " gewechselt! :)\n";
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Utils.send(onlinePlayer, message);
            }
        }
    }
}
