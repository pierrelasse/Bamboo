package net.bluept.bamboo.services.kmswitch;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.display.DisplayController;
import net.bluept.bamboo.services.kmswitch.commands.KMSwitchDevCmd;
import net.bluept.bamboo.services.timer.TimerService;
import net.bluept.bamboo.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

@ServiceInfo(name = "KeyboardMouseSwitch")
public class KMSwitchService extends Service {
    public int tick;
    public int interval;
    public boolean isMouse;
    private BukkitTask tickTask;
    private KMSwitchDevCmd kmSwitchDevCmd;

    @Override
    public void onEnable() {
        generateInterval();
        tickTask = Bukkit.getScheduler().runTaskTimer(Bamboo.INS, this::tick, 0, 20);

        DisplayController.push();

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registerCommand(kmSwitchDevCmd = new KMSwitchDevCmd());
        }
    }

    @Override
    public void onDisable() {
        DisplayController.pop();

        CommandService commandService = Bamboo.INS.serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.unregisterCommand(kmSwitchDevCmd);
        }

        tickTask.cancel();
    }

    public void generateInterval() {
        interval = Bamboo.INS.random.nextInt(1, 5) * 60;
    }

    public void tick() {
        TimerService timerService = Bamboo.INS.serviceManager.getService(TimerService.class);
        if (timerService == null || !timerService.resumed) {
            return;
        }

        tick++;
        if (tick >= interval) {
            tick = 0;
            generateInterval();

            isMouse = Bamboo.INS.random.nextBoolean();
            String title = isMouse ? KMSwitchConfig.MOUSE_CHAR : KMSwitchConfig.KEYBOARD_CHAR;
            String subTitle = Utils.colorfy("&d&l" + (isMouse ? "Maus" : "Tastatur"));
            Bamboo.INS.getLogger().info("KMSwitch: Now changing to " + (isMouse ? "Mouse" : "Keyboard"));

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Utils.title(onlinePlayer, title, subTitle, 10, 27, 30);
                onlinePlayer.playSound(onlinePlayer.getLocation(), "bluept:notification", SoundCategory.VOICE, 1F, 1F);
            }
        }
    }
}
