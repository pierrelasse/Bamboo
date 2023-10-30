package net.bluept.bamboo.services.kmswitch.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.kmswitch.KMSwitchService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class KMSwitchDevCmd extends Command {
    public KMSwitchDevCmd() {
        super("kmswitchdev");
        setPermission("penis");
        usage("/kmswitchdev (settick) ...");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        KMSwitchService kmSwitchService = Bamboo.INS.serviceManager.getService(KMSwitchService.class);
        if (kmSwitchService == null) {
            Utils.send(sender, "&cUnable to connect to the dimtp service");
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

        } else {
            Utils.send(sender, usage());
        }
    }
}
