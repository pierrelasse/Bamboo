package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.timer.TimerService;
import org.bukkit.command.CommandSender;

import java.util.List;

public class IdleCmd extends Command {
    public IdleCmd() {
        super("idle");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        ForceItemBattle.INSTANCE.serviceManager.getAndRun("timer", TimerService.class, serv -> {
            serv.resumed = !serv.resumed;
        });
    }
}
