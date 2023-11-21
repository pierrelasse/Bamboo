package net.bluept.bamboo.services.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.services.translation.TranslationService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(id = "forceitembattle/tablist")
public class TablistService extends Service {
    public BukkitTask tickTask;

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
        resetPlayerNames();
    }

    public void tick() {
        ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
        if (itemService == null || TimerService.isResumed()) {
            resetPlayerNames();
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            updatePlayer(itemService, onlinePlayer);
        }
    }

    public void resetPlayerNames() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            setPlayerListName(onlinePlayer, onlinePlayer.getName());
        }
    }

    public void updatePlayer(ItemService itemService, Player player) {
        setPlayerListName(player, player.getName() + " &8[&d" + TranslationService.translatePlayerItem(itemService, player) + "&8]");
    }

    @SuppressWarnings("deprecation")
    public void setPlayerListName(Player player, String text) {
        player.setPlayerListName(Utils.colorfy(text));
    }
}
