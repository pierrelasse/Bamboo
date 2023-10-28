package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.forceitembattle.ItemService;
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
        ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
        if (itemService != null) {
            if (itemService.consumeJoker(player.getUniqueId())) {
                itemService.collectItem(player);
                player.getInventory().addItem(new ItemStack(itemService.getPlayerMaterial(player.getUniqueId())));
                Utils.send(player, "&dJoker erfolgreich benutzt. " + itemService.getJokerLeft(player.getUniqueId()) + " joker verbleibend");
            } else {
                Utils.send(player, "&cDu hast keine Joker mehr");
            }
        }
    }
}
