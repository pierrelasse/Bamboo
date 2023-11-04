package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
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

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        if (args.size() <= 1) {
            String arg0 = Utils.get(args, 0, "");
            for (String s : List.of("list", "start", "stop")) {
                if (s.startsWith(arg0)) {
                    completions.add(s);
                }
            }

        } else if (args.size() == 2) {
            String arg0 = Utils.get(args, 0, "");

            if (!"list".equals(arg0)) {
                ServiceManager serviceManager = Bamboo.INS.serviceManager;

                String arg1 = Utils.get(args, 1, "");

                for (String s : serviceManager.getServices()) {
                    if (s.startsWith(arg1)) {
                        boolean enabled = serviceManager.getService(s) != null;
                        if (("start".equals(arg0) && !enabled) || ("stop".equals(arg0) && enabled)) {
                            completions.add(s);
                        }
                    }
                }
            }
        }

        return completions;
    }
}
