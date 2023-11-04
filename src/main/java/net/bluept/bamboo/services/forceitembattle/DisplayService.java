package net.bluept.bamboo.services.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.services.translation.TranslationService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(name = "forceitembattle/display")
public class DisplayService extends Service {
    public BukkitTask tickTask;

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
    }

    public void tick() {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService != null && timerService.resumed) {
            ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
            if (itemService != null) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() != GameMode.SPECTATOR) {
                        displayActionbar(itemService, player);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void displayActionbar(ItemService itemService, Player player) {
        int items = itemService.getPlayerItems(player.getUniqueId());
        player.sendActionBar(Utils.colorfy("&d" + items + " &8- &d" + TranslationService.translatePlayerItem(itemService, player)));
    }
}
