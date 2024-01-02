package net.bluept.bamboo.services.system.command.commands;

import net.bluept.bamboo.services.system.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
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
            Utils.send(player, usg("<player: player>"));
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

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        String arg0 = Utils.get(args, 0, "");
        if (args.size() <= 1) {
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(i -> i.startsWith(arg0)).toList());
        }

        return completions;
    }
}
