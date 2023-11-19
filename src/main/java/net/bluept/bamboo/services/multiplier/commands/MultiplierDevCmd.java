package net.bluept.bamboo.services.multiplier.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.multiplier.MultiplierService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MultiplierDevCmd extends Command {
    public MultiplierDevCmd() {
        super("MultiplierDev");
        usage("(reload|info)");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        switch (Utils.get(args, 0)) {
            case "reload" -> {
                MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
                if (multiplierService != null) {
                    multiplierService.config.load();
                    Utils.send(sender, "&aReloaded successfully!");
                }
            }
            case "info" -> {
                MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
                if (multiplierService == null) {
                    Utils.send(sender, "&cUnable to connect to the multiplier service");
                    break;
                }

                Utils.send(sender, "&dMultiplier info&8:");
                Utils.send(sender, "&d  Current multiplier&8: &c" + multiplierService.multiplier);
            }
            default -> Utils.send(sender, usage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        if (args.size() <= 1) {
            String arg0 = Utils.get(args, 0, "");
            for (String s : List.of("reload", "info")) {
                if (s.startsWith(arg0)) {
                    completions.add(s);
                }
            }
        }

        return completions;
    }
}
