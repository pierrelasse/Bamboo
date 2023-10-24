package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.services.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StartCmd extends Command {
    public StartCmd() {
        super("start");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (sender instanceof Player) {
            sender.sendMessage("Hola!");
        }
    }
}
