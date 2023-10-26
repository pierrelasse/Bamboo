package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.timer.TimerService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Set;

public class ResetCmd extends Command {
    public ResetCmd() {
        super("reset");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, List<String> args) {
        String message = Utils.colorfy("&<#E83845>Der Server wird nun zur\u00FCckgesetzt.\n&<#D62B38>Von " + sender.getName());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.kickPlayer(message);
        }

        Set<String> blacklistedFolders = Set.of("datapacks", "paper-world.yml");
        for (String world : List.of("world", "world_nether", "world_the_end")) {
            File folder = new File(ForceItemBattle.INS.serverRoot, world);
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!blacklistedFolders.contains(file.getName())) {
                        Utils.rDelete(file);
                    }
                }
            }
        }

        ForceItemBattle.INS.serviceManager.getAndRun(TimerService.class, serv -> {
            serv.time = 0;
        });

        Bukkit.shutdown();
    }
}
