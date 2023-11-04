package net.bluept.bamboo.services.health;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.emoji.StaticEmoji;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(description = "Gluck gluck :D")
public class HealthService extends Service {
    public static final String MESSAGE =
            Utils.colorfy(
                    "\n" +
                    "&bZeit etwas zu trinken!" +
                    "\n&3Ein Erwachsener braucht ca 2,5 Liter Wasser am Tag!" +
                    "\n&6Und wo wir doch gleich dabei sind; Setz dich ordentlich hin!" +
                    "\n&cDenn R\u00FCckenschmerzen sind nicht gut" +
                    "\n");
    private BukkitTask tickTask;

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 20 * 60 * 15, 20 * 60 * 10);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
    }

    private void tick() {
        String message = StaticEmoji.translateEmojis(MESSAGE);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(message);
        }
    }
}
