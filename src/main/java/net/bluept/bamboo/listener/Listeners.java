package net.bluept.bamboo.listener;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.emoji.StaticEmoji;
import net.bluept.bamboo.services.forceitembattle.ItemService;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.services.translation.TranslationService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class Listeners implements Listener {
    @EventHandler
    public void event(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
            ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
            if (timerService != null && timerService.resumed && itemService != null) {
                itemService.handlePickup(event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void event(InventoryClickEvent event) {
        if (!event.isCancelled() && event.getWhoClicked() instanceof Player) {
            TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
            ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
            if (timerService != null && timerService.resumed && itemService != null) {
                itemService.handleClick(event);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void event(PlayerChatEvent event) {
        StaticEmoji.handleChatEvent(event);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void event(PlayerJoinEvent event) {
        TranslationService translationService = Bamboo.INS.serviceManager.getService(TranslationService.class);
        if (translationService != null) {
            String lang = Utils.stringifyLocale(event.getPlayer().locale());
            if (!translationService.loadedTranslations.containsKey(lang)) {
                StringBuilder sb = new StringBuilder();
                sb.append("&<#ED0D4C>Invalid game language detected!\n\n&<#F314AC>Supported languages:\n");

                for (Map.Entry<String, Map<String, String>> entry : translationService.loadedTranslations.entrySet()) {
                    Map<String, String> translations = entry.getValue();
                    sb.append(String.format("&8• &<#FC07B4>%s (%s) &7- &<#610981>%s", translations.get("language.name"), translations.get("language.region"), entry.getKey()));
                }

                event.getPlayer().kickPlayer(Utils.colorfy(sb.toString()));
            }
        }
    }
}
