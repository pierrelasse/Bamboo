package net.bluept.forceitembattle.services.actionbar;

import net.bluept.forceitembattle.util.Utils;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.timer.TimerService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionbarService extends Service {
    private TimerService timerService;

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @SuppressWarnings("deprecation")
    public void update(long time) {
        int itemCount = 0;
        String timer = "timer";
        String item = "item";

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(Utils.colorfy("&d" + itemCount + " &8- &d" + timer + " &8 &d" + item));
        }
    }
}
