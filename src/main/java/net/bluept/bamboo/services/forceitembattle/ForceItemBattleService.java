package net.bluept.bamboo.services.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.display.DisplayController;
import net.bluept.bamboo.services.forceitembattle.commands.FIBDevCmd;
import net.bluept.bamboo.services.forceitembattle.commands.JokerCmd;
import net.bluept.bamboo.services.timer.TimerService;

@ServiceInfo(id = "forceitembattle", name = "ForceItemBattle")
public class ForceItemBattleService extends Service {
    private FIBDevCmd devCmd;
    private JokerCmd jokerCmd;

    @Override
    public void onEnable() {
        ServiceManager serviceManager = Bamboo.INS.serviceManager;

        TimerService timerService = serviceManager.getService(TimerService.class);
        if (timerService != null) {
            timerService.countDown = true;
        }

        serviceManager.registerService(new ItemService());
        serviceManager.registerService(new DisplayService());
        serviceManager.registerService(new TablistService());
        serviceManager.registerService(new ItemDisplayService());
        for (String id : serviceManager.getServices()) {
            if (id.startsWith("forceitembattle/")) {
                serviceManager.startService(id);
            }
        }

        DisplayController.push();

        CommandService commandService = serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registerCommand(devCmd = new FIBDevCmd());
            commandService.registerCommand(jokerCmd = new JokerCmd());
        }
    }

    @Override
    public void onDisable() {
        DisplayController.pop();

        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.unregisterService(ItemDisplayService.class);
        serviceManager.unregisterService(TablistService.class);
        serviceManager.unregisterService(DisplayService.class);
        serviceManager.unregisterService(ItemService.class);

        CommandService commandService = serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.unregisterCommand(devCmd);
            commandService.unregisterCommand(jokerCmd);
        }
    }
}
