package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class CloneInvCmd extends Command {
    public CloneInvCmd() {
        super("cloneinv");
        setPermission("penis");
    }

    @Override
    public void executePlayer(Player player, List<String> args) {
        String targetName = Utils.get(args, 0);
        Player target = (targetName == null) ? null : Bukkit.getPlayer(targetName);

        if (target == null) {
            Utils.send(player, "&cUsage: /cloneinv <player: player>");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        PlayerInventory targetInventory = target.getInventory();

        targetInventory.setContents(inventory.getContents());
        targetInventory.setHelmet(inventory.getHelmet());
        targetInventory.setChestplate(inventory.getChestplate());
        targetInventory.setLeggings(inventory.getLeggings());
        targetInventory.setBoots(inventory.getBoots());
        targetInventory.setItemInOffHand(inventory.getItemInOffHand());

        Utils.send(player, "&aSet " + target.getName() + "'s inventory to yours");
    }
}
