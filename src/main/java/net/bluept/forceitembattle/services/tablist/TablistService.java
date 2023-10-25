package net.bluept.forceitembattle.services.tablist;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class TablistService extends Service {
    public BukkitTask tickTask;

    @Override
    public void onEnable() {
        tickTask = Bukkit.getScheduler().runTaskTimer(ForceItemBattle.INSTANCE, this::tick, 0L, 20L);
    }

    @Override
    public void onDisable() {
        tickTask.cancel();
        resetPlayerNames();
    }

    @SuppressWarnings("deprecation")
    public void tick() {
        ItemService itemService = ForceItemBattle.INSTANCE.serviceManager.getServiceHandle("item", ItemService.class);
        if (itemService == null) {
            resetPlayerNames();
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            String item = itemService.getPlayerMaterial(onlinePlayer.getUniqueId()).toString();
            onlinePlayer.setPlayerListName(Utils.colorfy(onlinePlayer.getName() + " &8[&d" + item + "&8]"));
        }
    }

    public void resetPlayerNames() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.setPlayerListName(onlinePlayer.getName());
        }
    }
}
