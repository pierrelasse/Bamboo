package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.timer.TimerService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StartCmd extends Command {
    public StartCmd() {
        super("start");
        usage("/start <time: number>");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            Utils.send(sender, usage());
            return;
        }

        TimerService timerService = ForceItemBattle.INSTANCE.serviceManager.getService(TimerService.class);
        if (timerService == null) {
            Utils.send(sender, "&cError while connecting to the timer service");
            return;
        }

        long time;
        try {
            time = Long.parseLong(args.get(0));
            if (time >= 6000000000000000000L || time <= 0) {
                throw new NumberFormatException();
            }
            timerService.setTime(time * 60);
        } catch (NumberFormatException ex) {
            Utils.send(sender, "&cInvalid number");
            return;
        }

        timerService.resumed = true;
    }
}
