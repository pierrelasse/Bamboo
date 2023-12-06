package net.bluept.bamboo.services.forceitembattle.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.forceitembattle.ItemService;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JokerCmd extends Command {
    public JokerCmd() {
        super("joker");
    }

    @Override
    public void executePlayer(Player player, List<String> args) {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class, player);
        if (timerService == null) {
            return;
        }

        ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class, player);
        if (itemService == null) {
            return;
        }

        if (itemService.consumeJoker(player.getUniqueId())) {
            player.getInventory().addItem(new ItemStack(itemService.getPlayerMaterial(player.getUniqueId())));
            itemService.collectItem(player);
            Utils.send(player, "&dJoker erfolgreich benutzt. " + itemService.getJokerLeft(player.getUniqueId()) + " joker verbleibend");
        } else {
            Utils.send(player, "&cDu hast keine Joker mehr");
        }
    }
}
