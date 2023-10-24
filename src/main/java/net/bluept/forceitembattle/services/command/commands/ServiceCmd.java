package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.ServiceManager;
import net.bluept.forceitembattle.services.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ServiceCmd extends Command {
    public ServiceCmd() {
        super("service");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (args.size() <= 2) {
            sender.sendMessage("§cUsage: /service (list|start|stop) <service>");
            return;
        }

        ServiceManager serviceManager = ForceItemBattle.INSTANCE.serviceManager;

        String service = args.get(1);
        if (!serviceManager.hasService(service)) {
            sender.sendMessage("§cService not found");
            return;
        }

        if ("list".equals(args.get(0))) {
            sender.sendMessage("§7Available services§8: §e" + String.join("§8, §e", serviceManager.getServices()));

        } else if ("start".equals(args.get(0))) {
            serviceManager.startService(service);
            sender.sendMessage("§aService started");

        } else if ("stop".equals(args.get(0))) {
            serviceManager.stopService(service);
            sender.sendMessage("§aService stopped");

        } else {
            sender.sendMessage("§cUsage: /service (list|start|stop)");
        }
    }
}
