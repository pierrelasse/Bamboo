package net.bluept.forceitembattle.services.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Command extends org.bukkit.command.Command {
    public Command(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        List<String> argsList = Arrays.stream(args).toList();

        try {
            this.execute(sender, argsList);
            if (sender instanceof Player) {
                this.executePlayer((Player) sender, argsList);
            } else if (sender instanceof ConsoleCommandSender) {
                this.executeConsole((ConsoleCommandSender) sender, argsList);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            sender.sendMessage("§c§lThere was an error executing this command: " + ex.getMessage());
        }

        return true;
    }

    public void execute(CommandSender sender, List<String> args) {
    }

    public void executePlayer(Player sender, List<String> args) {
    }

    public void executeConsole(ConsoleCommandSender sender, List<String> args) {
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        return this.onTabComplete(sender, alias, Arrays.stream(args).toList());
    }

    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        return Collections.emptyList();
    }
}
