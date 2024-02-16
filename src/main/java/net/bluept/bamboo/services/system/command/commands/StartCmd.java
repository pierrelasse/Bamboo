package net.bluept.bamboo.services.system.command.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.challenges.forceitembattle.DisplayService;
import net.bluept.bamboo.services.challenges.forceitembattle.ItemService;
import net.bluept.bamboo.services.dep.timer.TimerService;
import net.bluept.bamboo.services.system.command.Command;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StartCmd extends Command {
    public StartCmd() {
        super("start");
        usage("<time: number>");
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class, sender);
        if (timerService == null) {
            return;
        }

        if (timerService.countDown) {
            if (args.size() == 0) {
                Utils.send(sender, usage());
                return;
            }

            long time;
            try {
                time = Long.parseLong(Utils.get(args, 0));
                if (time >= 6000000000000000000L || time <= 0) {
                    throw new NumberFormatException();
                }
                timerService.time = time * 60;

                // ForceItemBattle
                {
                    ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
                    DisplayService displayService = Bamboo.INS.serviceManager.getService(DisplayService.class);
                    if (itemService != null && displayService != null) {
                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            displayService.displayActionbar(itemService, onlinePlayer);
                        }
                    }
                }

            } catch (NumberFormatException ex) {
                Utils.send(sender, "&cInvalid number");
                return;
            }
        } else {
            timerService.time = 0;
        }

        timerService.resumed = true;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:start", SoundCategory.VOICE, 1F, 1F);
        }
    }
}
