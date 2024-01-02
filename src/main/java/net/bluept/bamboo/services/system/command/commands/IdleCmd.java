package net.bluept.bamboo.services.system.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.system.command.Command;
import net.bluept.bamboo.services.dep.timer.TimerService;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class IdleCmd extends Command {
    public IdleCmd() {
        super("idle");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService != null) {
            timerService.resumed = !timerService.resumed;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:notification", SoundCategory.VOICE, 1F, 1F);
            }
        }
    }
}
