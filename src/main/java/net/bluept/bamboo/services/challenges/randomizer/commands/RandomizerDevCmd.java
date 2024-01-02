package net.bluept.bamboo.services.challenges.randomizer.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.system.command.Command;
import net.bluept.bamboo.services.challenges.randomizer.RandomizerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class RandomizerDevCmd extends Command {
    public RandomizerDevCmd() {
        super("RandomizerDev");
        usage("(reload)");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        switch (Utils.get(args, 0)) {
            case "reload" -> {
                RandomizerService randomizerService = Bamboo.INS.serviceManager.getService(RandomizerService.class);
                if (randomizerService != null) {
                    randomizerService.config.load();
                    Utils.send(sender, "&aReloaded successfully!");
                }
            }
            default -> Utils.send(sender, usage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        if (args.size() <= 1) {
            String arg0 = Utils.get(args, 0, "");
            for (String s : List.of("reload")) {
                if (s.startsWith(arg0)) {
                    completions.add(s);
                }
            }
        }

        return completions;
    }
}
