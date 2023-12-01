package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.randomizer.RandomizerService;
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
        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        if (serviceManager == null) {
            Utils.send(sender, "&cCould not connect to the service manager");
            return;
        }

        switch (Utils.get(args, 0, Utils.EMPTY)) {
            case "reload" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "all" -> {
                        Bukkit.getServer().reload();
                        Utils.send(sender, "&aSuccess!");
                    }
                    default -> Utils.send(sender, usg("reload all"));
                }
            }
            case "test" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "server_class" -> {
                        Utils.send(sender, Bukkit.getServer().getClass().getName());
                    }
                    default -> Utils.send(sender, usg("test ?"));
                }
            }
            case "setgameid" -> {
                Integer gameId = null;
                try {
                    gameId = Integer.parseInt(Utils.get(args, 1));
                } catch (NumberFormatException ignored) {
                }

                if (gameId == null || gameId < 1 || gameId > 5) {
                    Utils.send(sender, "&cInvalid number");
                    return;
                }

                if (Bamboo.INS.getConfig().get("gameId") == gameId) {
                    Utils.send(sender, "&6&oGameId was already this value");
                }

                Bamboo.INS.getConfig().set("gameId", gameId);
                Bamboo.INS.saveConfig();

                Utils.send(sender, "&aGameId set to &2" + gameId + "&a. Use &2/dev reload all &ato apply changes");
            }
            case "timer" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "info" -> {
                        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
                        if (timerService == null) {
                            Utils.send(sender, "&cUnable to connect to the timer service");
                            break;
                        }
                        Utils.send(sender, "&dTimer info&8:");
                        Utils.send(sender, "&d  Resumed&8: &6" + timerService.resumed);
                        Utils.send(sender, "&d  Time&8: &9" + timerService.time);
                        Utils.send(sender, "&d  Formatted time&8: &9" + Utils.convertSecondsToDuration(timerService.time));
                        Utils.send(sender, "&d  Count down&8: &6" + timerService.countDown);
                        Utils.send(sender, "&d  Task&8: &9" + timerService.tickTask.getTaskId());
                        Utils.send(sender, "&d  Task cancelled&8: &6" + timerService.tickTask.isCancelled());
                    }
                    default -> Utils.send(sender, usg("timer info"));
                }
            }
            case "randomizer" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "reload_config" -> {
                        RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
                        if (randomizerService == null) {
                            Utils.send(sender, "&cUnable to connect to the randomizer service");
                            break;
                        }
                        randomizerService.config.load();
                        Utils.send(sender, "&aConfig reloaded successfully");
                    }
                    default -> Utils.send(sender, usg("randomizer reload_config"));
                }
            }
            default -> Utils.send(sender, usg("(reload|test|setgameid|timer|randomizer) ..."));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        switch (args.size()) {
            case 0, 1 -> Utils.addCompletions(completions, args, 0, "reload", "test", "setgameid", "timer", "randomizer");
            case 2 -> {
                switch (Utils.get(args, 0, Utils.EMPTY)) {
                    case "reload" -> {
                        Utils.addCompletions(completions, args, 1, "all");
                    }
                    case "setgameid" -> {
                        for (int i = 1; i <= 5; i++) {
                            completions.add(Integer.toString(i));
                        }
                    }
                    case "timer" -> {
                        Utils.addCompletions(completions, args, 1, "info");
                    }
                    case "randomizer" -> {
                        Utils.addCompletions(completions, args, 1, "reload_config");
                    }
                }
            }
        }

        return completions;
    }
}
