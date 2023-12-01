package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class DevCmd extends Command {
    public DevCmd() {
        super("dev");
        setPermission("penis");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        switch (Utils.get(args, 0, Utils.EMPTY)) {
            case "timer" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "get" -> {
                        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
                        if (timerService == null) {
                            Utils.send(sender, "&cUnable to connect to the timer service");
                            break;
                        }

                        Utils.send(sender, "&dTime&8: &f" + timerService.time);
                    }
                    default -> Utils.send(sender, usg("timer get"));
                }
            }
            case "reload" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "all" -> {
                        Bukkit.getServer().reload();
                        Utils.send(sender, "&aSuccess!");
                    }
                    default -> Utils.send(sender, usg("reload all"));
                }
            }
            default -> Utils.send(sender, usg("(timer|reload) ..."));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        switch (args.size()) {
            case 0, 1 -> Utils.addCompletions(completions, args, 0, List.of("timer", "reload"));
            case 2 -> {
                switch (Utils.get(args, 0, Utils.EMPTY)) {
                    case "timer" -> {
                        Utils.addCompletions(completions, args, 1, List.of("get"));
                    }
                    case "reload" -> {
                        Utils.addCompletions(completions, args, 1, List.of("all"));
                    }
                }
            }
        }

        return completions;
    }
}
