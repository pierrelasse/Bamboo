package net.bluept.bamboo.services.dep.display;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.service.ServiceManager;

public class DisplayController {
    private static int state = 0;

    private static void update() {
        if (state < 0) {
            state = 0;
        }

        ServiceManager serviceManager = Bamboo.INS.serviceManager;
        if (serviceManager == null) {
            return;
        }

        String id = serviceManager.getServiceId(DisplayService.class);
        if (state == 0) {
            serviceManager.stopService(id);
        } else {
            serviceManager.startService(id);
        }
    }

    public static void push() {
        state++;
        update();
    }

    public static void pop() {
        state--;
        update();
    }
}
