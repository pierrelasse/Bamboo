package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServiceCmd extends Command {
    public ServiceCmd() {
        super("service");
        usage("/service (list|start|stop|test) ...");
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
            String id = args.get(1);
            if (!serviceManager.hasService(id)) {
                Utils.send(sender, "&cService not found");
            }

            String arg0 = Utils.get(args, 0);

            switch (arg0) {
                case "start" -> {
                    try {
                        if (serviceManager.startService(id)) {
                            Utils.send(sender, "&aService '" + id + "' started");
                        } else {
                            Utils.send(sender, "&aService '" + id + "' is already started");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Bamboo.INS.getLogger().info("Error while starting service '" + id + "'");
                    }
                }
                case "stop" -> {
                    try {
                        if (serviceManager.stopService(id)) {
                            Utils.send(sender, "&aService '" + id + "' stopped");
                        } else {
                            Utils.send(sender, "&aService '" + id + "' is already stopped");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Bamboo.INS.getLogger().info("Error while stopping service '" + id + "'");
                    }
                }
                case "test" -> {
                    Service service = serviceManager.getService(id);
                    if (service == null) {
                        Utils.send(sender, "&cService not found");
                        break;
                    }

                    service.onTest();
                    Utils.send(sender, "&aService '" + id + "' tested");
                }
                case "info" -> {
                    Service service = serviceManager.getServiceF(id);
                    if (service == null) {
                        Utils.send(sender, "&cService not found");
                        break;
                    }

                    Utils.send(sender, "&7Info for service &e" + id + "&8:");
                    Utils.send(sender, "&8  - &7State: &" + (service.isEnabled() ? "aEnabled" : "cDisabled"));
                    if (!Objects.equals(service.name, id)) {
                        Utils.send(sender, "&8  - &7Name: &f" + service.name);
                    }
                    if (service.description != null) {
                        Utils.send(sender, "&8  - &7Description: &f" + service.description);
                    }

                }
                default -> {
                    Utils.send(sender, "&cUsage: /service (start|stop|test) <service>");
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        if (args.size() <= 1) {
            String arg0 = Utils.get(args, 0, "");
            for (String s : List.of("list", "start", "stop", "test", "info")) {
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
                        if ("info".equals(arg0) || ("start".equals(arg0) && !enabled) || (("stop".equals(arg0) || "test".equals(arg0)) && enabled)) {
                            completions.add(s);
                        }
                    }
                }
            }
        }

        return completions;
    }
}
