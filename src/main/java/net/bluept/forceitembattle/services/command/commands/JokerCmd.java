package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class JokerCmd extends Command {
    public JokerCmd() {
        super("joker");
    }

    @Override
    public void executePlayer(Player player, List<String> args) {
        ItemService itemService = ForceItemBattle.INS.serviceManager.getService(ItemService.class);
        if (itemService != null && itemService.consumeJoker(player.getUniqueId())) {
            player.getInventory().addItem(new ItemStack(itemService.getPlayerMaterial(player.getUniqueId())));
            Utils.send(player, "&aJoker erfolgreich benutzt. " + itemService.getJokerLeft(player.getUniqueId()) + " joker verbleibend");
        }
    }
}
