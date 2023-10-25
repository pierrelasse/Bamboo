package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.Utils;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.service.ServiceManager;
import net.bluept.forceitembattle.services.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ServiceCmd extends Command {
    public ServiceCmd() {
        super("service");
        usage("&cUsage: /service (list|start|stop) ...");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        ServiceManager serviceManager = ForceItemBattle.INSTANCE.serviceManager;
        if (serviceManager == null) {
            Utils.send(sender, "&cUnable to connect to ServiceManager");
            return;
        }

        if (args.size() == 0) {
            Utils.send(sender, usage());

        } else if (args.size() == 1 && "list".equals(args.get(0))) {
            Utils.send(sender, "&7Available services &8(&7" + serviceManager.getServices().size() + "&8)");
            for (String id : serviceManager.getServices()) {
                Service service = serviceManager.getService(id);
                Utils.send(sender, "  &8- &" + (service.isEnabled() ? "a" : "c") + id);
            }

        } else {
            String service = args.get(1);
            if (!serviceManager.hasService(service)) {
                Utils.send(sender, "&cService not found");
            }

            if ("start".equals(args.get(0))) {
                try {
                    serviceManager.startService(service);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ForceItemBattle.INSTANCE.getLogger().info("Error while starting service '" + service + "'");
                }
                Utils.send(sender, "&aService '" + service + "' started");

            } else if ("stop".equals(args.get(0))) {
                serviceManager.stopService(service);
                Utils.send(sender, "&aService '" + service + "' stopped");

            } else {
                Utils.send(sender, "&cUsage: /service (start|stop) <service>");
            }
        }
    }
}
