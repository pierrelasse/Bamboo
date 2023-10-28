package net.bluept.bamboo.services.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.service.ServiceManager;
import net.bluept.bamboo.services.command.CommandService;
import net.bluept.bamboo.services.forceitembattle.commands.FIBDevCmd;

@ServiceInfo(name = "forceitembattle")
public class ForceItemBattleService extends Service {
    private FIBDevCmd devCmd;

    @Override
    public void onEnable() {
        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.registerService(new ItemService());
        serviceManager.registerService(new DisplayService());
        serviceManager.registerService(new TablistService());
        serviceManager.registerService(new ItemDisplayService());
        for (String id : serviceManager.getServices()) {
            if (id.startsWith("forceitembattle/")) {
                serviceManager.startService(id);
            }
        }

        CommandService commandService = serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.registerCommand((devCmd = new FIBDevCmd()));
        }
    }

    @Override
    public void onDisable() {
        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.unregisterService(ItemDisplayService.class);
        serviceManager.unregisterService(TablistService.class);
        serviceManager.unregisterService(DisplayService.class);
        serviceManager.unregisterService(ItemService.class);

        CommandService commandService = serviceManager.getService(CommandService.class);
        if (commandService != null) {
            commandService.unregisterCommand(devCmd);
        }
    }
}
