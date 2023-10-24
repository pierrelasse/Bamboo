package net.bluept.forceitembattle.services.tablist;

import net.bluept.forceitembattle.ForceItemBattle;
import net.bluept.forceitembattle.service.Service;
import net.bluept.forceitembattle.services.item.ItemService;
import net.bluept.forceitembattle.services.timer.TimerService;

public class TablistService extends Service {
    private ItemService itemService;

    @Override
    public void start() {
        itemService = ForceItemBattle.INSTANCE.serviceManager.getServiceHandle("item", ItemService.class);
    }

    @Override
    public void stop() {
    }
}
