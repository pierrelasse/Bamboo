package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.timer.TimerService;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StartCmd extends Command {
    public StartCmd() {
        super("start");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        TimerService timerService = ForceItemBattle.INSTANCE.serviceManager.getServiceHandle("timer", TimerService.class);
        if (timerService != null) {
            timerService.resumed = true;
        }
    }
}
