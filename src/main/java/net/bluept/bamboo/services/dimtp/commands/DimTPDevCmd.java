package net.bluept.bamboo.services.dimtp.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.dimtp.DimTPConfig;
import net.bluept.bamboo.services.dimtp.DimTPService;
import net.bluept.bamboo.services.dimtp.Generator;
import net.bluept.bamboo.util.DisplayHelper;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DimTPDevCmd extends Command {
    public DimTPDevCmd() {
        super("dimtpdev");
        setPermission("penis");
        usage("/dimtp (tpmenow|tpallnow|tpallsoon|settick|info) ...");
    }

    @Override
    public void executePlayer(Player player, List<String> args) {
        DimTPService dimTPService = Bamboo.INS.serviceManager.getService(DimTPService.class);
        if (dimTPService == null) {
            Utils.send(player, "&cUnable to connect to the dimtp service");
            return;
        }

        String subCommand = Utils.get(args, 0);

        if ("tpmenow".equals(subCommand)) {
            Object[] data = Generator.getRandomLocation(Generator.randomDim(), 0);
            if (data[0] == null) {
                Utils.send(player, "&cCould not find a location");
                return;
            }
            player.teleport((Location)data[0]);
            Utils.send(player, "&aTeleported randomly. After " + data[1] + " tries");

        } else if ("tpallnow".equals(subCommand)) {
            dimTPService.tick = DimTPConfig.INTERVAL;
            Utils.send(player, "&aSet tick");

        } else if ("tpallsoon".equals(subCommand)) {
            dimTPService.tick = DimTPConfig.INTERVAL - 3;
            Utils.send(player, "&aSet tick to tp soon");

        } else if ("settick".equals(subCommand)) {
            Integer tick = null;
            try {
                tick = Integer.parseInt(Utils.get(args, 1));
            } catch (NumberFormatException ignored) {
            }

            if (tick == null || tick > 500000 || tick < 0) {
                Utils.send(player, "&cInvalid number");
                return;
            }

            dimTPService.tick = tick;

            Utils.send(player, "&aSet tick to " + tick);

        } else if ("info".equals(subCommand)) {
            Utils.send(player, "&dDimTP info&8:");
            Utils.send(player, "&d  Tick&8: &c" + dimTPService.tick);
            Utils.send(player, "&d  Interval&8: &5" + DimTPConfig.INTERVAL);
            Utils.send(player, "&d  Interval Min&8: &5" + DimTPConfig.INTERVAL_MIN);
            Utils.send(player, "&d  Interval Max&8: &5" + DimTPConfig.INTERVAL_MAX);
            Utils.send(player, "&d  Tp in&8: &a" + DisplayHelper.convertSecondsToDuration(DimTPConfig.INTERVAL - dimTPService.tick));
            Utils.send(player, "&d  Max tries&8: &4" + DimTPConfig.MAX_TRIES);
            Utils.send(player, "&d  X_MAX&8: &e" + DimTPConfig.X_MAX);
            Utils.send(player, "&d  X_MIN&8: &e" + DimTPConfig.X_MIN);
            Utils.send(player, "&d  Z_MAX&8: &e" + DimTPConfig.Z_MAX);
            Utils.send(player, "&d  Z_MIN&8: &e" + DimTPConfig.Z_MIN);

        } else if ("dis".equals(subCommand)) {
            DimTPConfig.enabled = false;
            Utils.send(player, "OK!");

        } else {
            Utils.send(player, usage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();
        String arg0 = Utils.get(args, 0, "");

        if (args.size() < 2) {
            for (String s : List.of("tpmenow", "tpallnow", "tpallsoon", "settick", "info")) {
                if (s.startsWith(arg0)) {
                    completions.add(s);
                }
            }
        }

        return completions;
    }
}
