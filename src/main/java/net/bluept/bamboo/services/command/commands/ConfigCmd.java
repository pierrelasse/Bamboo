package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.services.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ConfigCmd extends Command {
    public ConfigCmd() {
        super("config");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void executePlayer(Player player, List<String> args) {
        Inventory inventory = Bukkit.createInventory(player, InventoryType.CHISELED_BOOKSHELF, "§f七七七七七七七七ㇺ");
        player.openInventory(inventory);
    }
}
