package net.bluept.forceitembattle.services.item;

import net.bluept.forceitembattle.service.Service;
import org.bukkit.Material;

import java.util.Random;

public class ItemService extends Service {
    private Random random;

    @Override
    public void start() {
        random = new Random();
    }

    @Override
    public void stop() {

    }

    public Material getRandomMaterial() {
        return Material.values()[random.nextInt(Material.values().length)];
    }
}
