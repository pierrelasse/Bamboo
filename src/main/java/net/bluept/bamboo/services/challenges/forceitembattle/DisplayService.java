package net.bluept.bamboo.services.challenges.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(id = "forceitembattle/display")
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
        if (TimerService.isResumed()) {
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

    public void displayActionbar(ItemService itemService, Player player) {
        int items = itemService.getPlayerItems(player.getUniqueId());
        Material material = itemService.getPlayerMaterial(player.getUniqueId());
        if (material != null) {
            String key = material.getItemTranslationKey();
            if (key != null) {
                player.sendActionBar(MiniMessage.miniMessage().deserialize("<light_purple>%i</light_purple> <dark_gray>- <light_purple><lang:%k></light_purple>".replace("%k", key).replace("%i", Integer.toString(items))));
            }
        }
    }
}
