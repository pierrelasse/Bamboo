package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InvseeCmd extends Command {
    public InvseeCmd() {
        super("invsee");
    }

    @Override
    public void executePlayer(Player player, List<String> args) {
        Player target = Bukkit.getPlayer(Utils.get(args, 0));
        if (target == null) {
            Utils.send(player, "&cPlayer not found");
            return;
        }
        player.openInventory(target.getInventory());
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
