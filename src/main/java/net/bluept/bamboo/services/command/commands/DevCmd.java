package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DevCmd extends Command {
    public DevCmd() {
        super("dev");
        setPermission("penis");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        switch (Utils.get(args, 0, "")) {
            case "timer" -> {
                switch (Utils.get(args, 1, "")) {
                    case "get" -> {
                        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
                        if (timerService == null) {
                            Utils.send(sender, "&cUnable to connect to the timer service");
                            break;
                        }

                        Utils.send(sender, "&dTime&8: &f" + timerService.time);
                    }
                }
            }
        }
    }
}
