package net.bluept.bamboo.services.forceitembattle;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.Service;
import net.bluept.bamboo.service.ServiceInfo;
import net.bluept.bamboo.service.ServiceManager;

@ServiceInfo(name = "forceitembattle")
public class ForceItemBattleService extends Service {
    @Override
    public void onEnable() {
        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.registerService(new ItemService());
        serviceManager.registerService(new DisplayService());
        serviceManager.registerService(new TablistService());
        serviceManager.registerService(new ItemDisplayService());
    }

    @Override
    public void onDisable() {
        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        serviceManager.unregisterService(ItemDisplayService.class);
        serviceManager.unregisterService(TablistService.class);
        serviceManager.unregisterService(DisplayService.class);
        serviceManager.unregisterService(ItemService.class);
    }
}
