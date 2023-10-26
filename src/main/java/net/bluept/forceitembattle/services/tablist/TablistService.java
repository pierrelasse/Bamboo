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

    public void tick() {
        ItemService itemService = ForceItemBattle.INSTANCE.serviceManager.getServiceHandle("item", ItemService.class);
        if (itemService == null) {
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
        setPlayerListName(player, player.getName() + " &8[&d" + itemService.getPlayerMaterial(player.getUniqueId()).name() + "&8]");
    }

    @SuppressWarnings("deprecation")
    public void setPlayerListName(Player player, String text) {
        player.setPlayerListName(Utils.colorfy(text));
    }
}
