package net.bluept.bamboo.services.system.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.system.command.Command;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Set;

public class ResetCmd extends Command {
    public ResetCmd() {
        super("reset");
        setPermission("penis");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if ("confirm".equals(Utils.get(args, 0))) {
            kickAllPlayers(sender);

            Set<String> blacklistedFolders = Set.of("datapacks", "paper-world.yml");
            for (String world : List.of("world", "world_nether", "world_the_end")) {
                File folder = new File(Bamboo.INS.serverRoot, world);
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (!blacklistedFolders.contains(file.getName())) {
                            Utils.rDelete(file);
                        }
                    }
                }
            }

            Bamboo.INS.serviceManager.getAndRun(TimerService.class, serv -> serv.time = 0);

            Bukkit.shutdown();

        } else {
            Utils.send(sender, "&<#FB0498>Bitte benutze /reset confirm");
        }
    }

    @SuppressWarnings("deprecation")
    public void kickAllPlayers(CommandSender sender) {
        String message = Utils.colorfy("&<#E83845>Der Server wird nun zur\u00FCckgesetzt.\n&<#D62B38>Von " + sender.getName());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.kickPlayer(message);
        }
    }
}
