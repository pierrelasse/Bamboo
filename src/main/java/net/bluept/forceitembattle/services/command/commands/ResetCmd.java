package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ResetCmd extends Command {
    public ResetCmd() {
        super("reset");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        ForceItemBattle.INSTANCE.getConfig().set("reset_world", true);
    }
}
