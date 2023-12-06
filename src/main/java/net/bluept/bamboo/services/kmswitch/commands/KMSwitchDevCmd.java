package net.bluept.bamboo.services.kmswitch.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.kmswitch.KMSwitchService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class KMSwitchDevCmd extends Command {
    public KMSwitchDevCmd() {
        super("kmswitchdev");
        setPermission("penis");
        usage("(settick|info) ...");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        KMSwitchService kmSwitchService = Bamboo.INS.serviceManager.getService(KMSwitchService.class, sender);
        if (kmSwitchService == null) {
            return;
        }

        String subCommand = Utils.get(args, 0);

        if ("settick".equals(subCommand)) {
            Integer tick = null;
            try {
                tick = Integer.parseInt(Utils.get(args, 1));
            } catch (NumberFormatException ignored) {
            }

            if (tick == null || tick > 500000 || tick < 0) {
                Utils.send(sender, "&cInvalid number");
                return;
            }

            kmSwitchService.tick = tick;

            Utils.send(sender, "&aSet tick to " + tick);

        } else if ("info".equals(subCommand)) {
            Utils.send(sender, "&dDimTP info&8:");
            Utils.send(sender, "&d  Tick&8: &c" + kmSwitchService.tick);
            Utils.send(sender, "&d  Interval&8: &5" + kmSwitchService.interval);
            Utils.send(sender, "&d  Last&8: &5" + (kmSwitchService.isMouse ? "Mouse" : "Keyboard"));

        } else {
            Utils.send(sender, usage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();
        String arg0 = Utils.get(args, 0, "");

        if (args.size() < 2) {
            for (String s : List.of("settick", "info")) {
                if (s.startsWith(arg0)) {
                    completions.add(s);
                }
            }
        }

        return completions;
    }
}
