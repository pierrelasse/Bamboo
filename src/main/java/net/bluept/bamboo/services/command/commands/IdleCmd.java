package net.bluept.bamboo.services.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.command.Command;
import net.bluept.bamboo.services.timer.TimerService;
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
        Bamboo.INS.serviceManager.getAndRun(TimerService.class, serv -> {
            serv.resumed = !serv.resumed;
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:notification", SoundCategory.VOICE, 1F, 1F);
            }
        });
    }
}
