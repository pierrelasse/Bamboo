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

    @Override
    public void execute(CommandSender sender, List<String> args) {
        String message = Utils.colorfy("<#E83845>Der Server nun von " + sender.getName() + " resettet.");
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.kickPlayer(message);
        }

        ForceItemBattle.INSTANCE.getConfig().set("reset_world", true);
        ForceItemBattle.INSTANCE.serviceManager.getAndRun("timer", TimerService.class, serv -> {
            serv.time = 0;
        });

        Bukkit.shutdown();
    }
}
