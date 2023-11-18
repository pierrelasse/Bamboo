package net.bluept.bamboo.services.multiplier.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.multiplier.MultiplierService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.List;

public class MultiplierDevCmd extends Command {
    public MultiplierDevCmd() {
        super("MultiplierDev");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        String arg0 = Utils.get(args, 0);

        if ("reload".equals(arg0)) {
            MultiplierService multiplierService = Bamboo.INS.serviceManager.getService(MultiplierService.class);
            if (multiplierService != null) {
                multiplierService.config.load();
            }
        }
    }
}
