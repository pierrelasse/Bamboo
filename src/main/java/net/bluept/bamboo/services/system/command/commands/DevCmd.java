package net.bluept.bamboo.services.system.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.challenges.dimtp.DimTPConfig;
import net.bluept.bamboo.services.challenges.dimtp.DimTPService;
import net.bluept.bamboo.services.challenges.randomizer.InvRandomizerService;
import net.bluept.bamboo.services.challenges.randomizer.RandomizerService;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.services.system.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DevCmd extends Command {
    public DevCmd() {
        super("dev");
        setPermission("penis");
    }

    @SuppressWarnings("deprecation")
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
            case "world" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "list" -> {
                        Utils.send(sender, "&dWorlds &8(&7" + Bukkit.getWorlds().size() + "&8):");
                        for (World world : Bukkit.getWorlds()) {
                            Utils.send(sender, "  &8- &d" + world.getName());
                        }
                    }
                    case "create" -> {
                    }
                    case "delete" -> {
                    }
                    default -> Utils.send(sender, usg("world (list|create|delete) ..."));
                }
            }
            case "test" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "server_class" -> Utils.send(sender, Bukkit.getServer().getClass().getName());
                    case "ride" -> {
                        if (sender instanceof Player player) {
                            Player target = Bukkit.getPlayer(Utils.get(args, 2, Utils.EMPTY));
                            if (target == null) {
                                Utils.send(sender, "&cPlayer not found");
                                break;
                            }

                            target.addPassenger(player);
                            Utils.send(sender, "&aYou are now riding &2" + target + "&a!");
                        }
                    }
                    case "arrowsstuck" -> {
                        if (sender instanceof Player player) {
                            Integer amount = Utils.parseInt(Utils.get(args, 2, Utils.EMPTY));
                            if (amount == null || amount < 0) {
                                Utils.send(sender, "&cInvalid number");
                                break;
                            }
                            player.setArrowsInBody(amount);
                            Utils.send(sender, "&aArrows in body set to &2" + amount);
                        }
                    }
                    case "regenchunk" -> {
                        if (sender instanceof Player player) {
                            Chunk chunk = player.getChunk();
                            player.getWorld().regenerateChunk(chunk.getX(), chunk.getZ());
                        }
                    }
                    default -> Utils.send(sender, usg("test (server_class|ride|arrowsstuck|regenchunk) ..."));
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
                TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class, sender);
                if (timerService == null) {
                    break;
                }

                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "info" -> {
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
            case "challenge" -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "dimtp" -> {
                        DimTPService dimTPService = Bamboo.INS.serviceManager.getService(DimTPService.class, sender);
                        if (dimTPService == null) break;
                        switch (Utils.get(args, 2, Utils.EMPTY)) {
                            case "tpall" -> {
                                dimTPService.tick = DimTPConfig.INTERVAL;
                                Utils.send(sender, "&aSet tick to tp soon");
                            }
                            case "settick" -> {
                                Integer tick = null;
                                try {
                                    tick = Integer.parseInt(Utils.get(args, 3));
                                } catch (NumberFormatException ignored) {
                                }
                                if (tick == null || tick > 500000 || tick < 0) {
                                    Utils.send(sender, "&cInvalid number");
                                    return;
                                }
                                dimTPService.tick = tick;
                                Utils.send(sender, "&aSet tick to " + tick);
                            }
                            case "info" -> {
                                Utils.send(sender, "&dDimTP info&8:");
                                Utils.send(sender, "&d  Tick&8: &c" + dimTPService.tick);
                                Utils.send(sender, "&d  Interval&8: &5" + DimTPConfig.INTERVAL);
                                Utils.send(sender, "&d  Interval Min&8: &5" + DimTPConfig.INTERVAL_MIN);
                                Utils.send(sender, "&d  Interval Max&8: &5" + DimTPConfig.INTERVAL_MAX);
                                Utils.send(sender, "&d  Tp in&8: &a" + Utils.convertSecondsToDuration(DimTPConfig.INTERVAL - dimTPService.tick));
                                Utils.send(sender, "&d  Max tries&8: &4" + DimTPConfig.MAX_TRIES);
                                Utils.send(sender, "&d  X_MAX&8: &e" + DimTPConfig.X_MAX);
                                Utils.send(sender, "&d  X_MIN&8: &e" + DimTPConfig.X_MIN);
                                Utils.send(sender, "&d  Z_MAX&8: &e" + DimTPConfig.Z_MAX);
                                Utils.send(sender, "&d  Z_MIN&8: &e" + DimTPConfig.Z_MIN);
                            }
                            case "setenabled" -> {
                                String state = Utils.get(args, 3);
                                switch (state) {
                                    case "false", "true" -> {
                                        DimTPConfig.enabled = state.equalsIgnoreCase("true");
                                        Utils.send(sender, "&aTeleporting is now &2" + (DimTPConfig.enabled ? "enabled" : "disabled"));
                                    }
                                    default -> Utils.send(sender, usg("challenge dimtp setenabled (true|false)"));
                                }
                            }
                            default ->
                                    Utils.send(sender, usg("challenge dimtp (tpmenow|tpall|settick|info|setenabled) ..."));
                        }
                    }
                    case "randomizer" -> {
                        RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class, sender);
                        if (randomizerService == null) break;

                        switch (Utils.get(args, 2, Utils.EMPTY)) {
                            case "info" -> {
                                InvRandomizerService invRandomizerService = Bamboo.INS.serviceManager.getService(InvRandomizerService.class);

                                if (invRandomizerService != null) {
                                    Utils.send(sender, "Tick: " + invRandomizerService.tick);
                                    Utils.send(sender, "Materials: " + invRandomizerService.materials.size());
                                }

                                Utils.send(sender, "&aCock");
                            }
                            case "reload" -> {
                                randomizerService.config.load();
                                Utils.send(sender, "&aConfig reloaded successfully");
                            }
                            default -> Utils.send(sender, usg("randomizer (info|reload)"));
                        }
                    }
                    default -> Utils.send(sender, usg("challenge (dimtp|randomizer) ..."));
                }
            }
            default -> Utils.send(sender, usg("(reload|test|setgameid|timer|challenge) ..."));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        switch (args.size()) {
            case 0, 1 -> Utils.addCompletions(completions, args, 0, "reload", "world", "test", "setgameid", "timer", "challenge");
            case 2 -> {
                switch (Utils.get(args, 0, Utils.EMPTY)) {
                    case "reload" -> Utils.addCompletions(completions, args, 1, "all");
                    case "world" -> Utils.addCompletions(completions, args, 1, "list", "create", "delete");
                    case "setgameid" -> {
                        for (int i = 1; i <= 6; i++) {
                            completions.add(Integer.toString(i));
                        }
                    }
                    case "timer" -> Utils.addCompletions(completions, args, 1, "info");
                    case "challenge" -> Utils.addCompletions(completions, args, 1, "dimtp", "randomizer");
                }
            }
            case 3 -> {
                switch (Utils.get(args, 1, Utils.EMPTY)) {
                    case "dimtp" ->
                            Utils.addCompletions(completions, args, 2, "tpmenow", "tpall", "settick", "info", "setenabled");
                    case "randomizer" -> Utils.addCompletions(completions, args, 2, "info", "reload");
                }
            }
        }

        return completions;
    }
}
