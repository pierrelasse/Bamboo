package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StartCmd extends Command {
    public StartCmd() {
        super("start");
        usage("/start <time: number>");
        setPermission("penis");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService == null) {
            Utils.send(sender, "&cCould not connect to timer service");
            return;
        }

        if (timerService.countDown) {
            if (args.size() == 0) {
                Utils.send(sender, usage());
                return;
            }

            long time;
            try {
                time = Long.parseLong(Utils.get(args, 0));
                if (time >= 6000000000000000000L || time <= 0) {
                    throw new NumberFormatException();
                }
                timerService.setTime(time * 60);
            } catch (NumberFormatException ex) {
                Utils.send(sender, "&cInvalid number");
                return;
            }
        }

        timerService.resumed = true;
    }
}
