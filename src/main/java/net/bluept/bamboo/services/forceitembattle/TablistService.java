package net.bluept.bamboo.services.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.timer.TimerService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        if (itemService == null || !TimerService.isResumed()) {
            resetPlayerNames();
            return;
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            updatePlayer(itemService, onlinePlayer);
        }
    }

    public void resetPlayerNames() {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playerListName(Component.text(onlinePlayer.getName()));
        }
    }

    public void updatePlayer(ItemService itemService, Player player) {
        Material material = itemService.getPlayerMaterial(player.getUniqueId());
        if (material != null) {
            String key = material.getItemTranslationKey();
            if (key != null) {
                player.playerListName(MiniMessage.miniMessage().deserialize(player.getName() + " <dark_gray>[</dark_gray><light_purple><lang:%k></light_purple><dark_gray>]</dark_gray>".replace("%k", key)));
            }
        }
    }
}
