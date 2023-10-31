package net.bluept.bamboo.services.health;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.services.emoji.StaticEmoji;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class HealthService extends Service {
    public static final int INTERVAL = 20 * 5;
    public static final String MESSAGE =
            Utils.colorfy("\n" +
                    "&bZeit etwas zu trinken!" +
                    "\n&fWaterGlass Denk daran: Ein Erwachsener braucht ca 2,5 Liter Wasser am Tag!" +
                    "\n&f???? TeaTime Und wo wir doch gleich dabei sind: Setz dich ordentlich hin!" +
                    "\n&fChair Denn R\u00FCckenschmerzen sind nicht gut PepoSad" +
                    "\n");
    private BukkitTask tickTask;
    private int tick;

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
    }

    private void tick() {
        tick++;
        if (tick >= INTERVAL) {
            tick = 0;

            String message = StaticEmoji.translateEmojis(MESSAGE);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(message);
            }
        }
    }
}
