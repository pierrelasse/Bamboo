package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ConfigCmd extends Command {
    public ConfigCmd() {
        super("config");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void executePlayer(Player player, List<String> args) {
        Inventory inventory = Bukkit.createInventory(player, 9, Utils.colorfy("&f\u4e03\u4e03\u4e03\u4e03\u4e03\u4e03\u4e03\u31fa"));
        player.openInventory(inventory);
    }
}
