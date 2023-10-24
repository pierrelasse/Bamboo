package net.bluept.forceitembattle.services.actionbar;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.timer.TimerService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionbarService extends Service {
    private TimerService timerService;

    @Override
    public void start() {
        timerService = ForceItemBattle.INSTANCE.serviceManager.getServiceHandle("timer", TimerService.class);
    }

    @Override
    public void stop() {
    }

    @SuppressWarnings("deprecation")
    public void update() {
        int itemCount = 0;
        String timer = "";
        String item = "";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar("&d" + itemCount + " &8- &d" + timer + " &8 &d" + item);
        }
    }
}
