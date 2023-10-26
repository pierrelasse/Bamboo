package net.bluept.forceitembattle.services.command.commands;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.services.command.Command;
import net.bluept.forceitembattle.services.timer.TimerService;
import net.bluept.forceitembattle.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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

        ForceItemBattle.INS.getConfig().set("reset_world", true);
        ForceItemBattle.INS.serviceManager.getAndRun(TimerService.class, serv -> {
            serv.time = 0;
        });

        Bukkit.shutdown();
    }
}
