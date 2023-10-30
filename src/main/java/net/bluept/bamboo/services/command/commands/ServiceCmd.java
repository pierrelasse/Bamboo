package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.util.Utils;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ServiceCmd extends Command {
    public ServiceCmd() {
        super("service");
        usage("/service (list|start|stop) ...");
        setPermission("penis");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        ServiceManager serviceManager = Bamboo.INS.serviceManager;

        if (args.size() == 0) {
            Utils.send(sender, usage());

        } else if ("list".equals(Utils.get(args, 0))) {
            Utils.send(sender, "&7Available services &8(&7" + serviceManager.getServices().size() + "&8)");
            for (String id : serviceManager.getServices()) {
                Utils.send(sender, "  &8- &" + (serviceManager.getServiceF(id).isEnabled() ? "a" : "c") + id);
            }

        } else if (args.size() >= 2) {
            String service = args.get(1);
            if (!serviceManager.hasService(service)) {
                Utils.send(sender, "&cService not found");
            }

            if ("start".equals(Utils.get(args, 0))) {
                Bukkit.getScheduler().runTaskLater(Bamboo.INS, () -> {
                    try {
                        serviceManager.startService(service);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Bamboo.INS.getLogger().info("Error while starting service '" + service + "'");
                    }
                    Utils.send(sender, "&aService '" + service + "' started");
                }, 2L);

            } else if ("stop".equals(Utils.get(args, 0))) {
                serviceManager.stopService(service);
                Utils.send(sender, "&aService '" + service + "' stopped");

            } else {
                Utils.send(sender, "&cUsage: /service (start|stop) <service>");
            }
        }
    }
}
