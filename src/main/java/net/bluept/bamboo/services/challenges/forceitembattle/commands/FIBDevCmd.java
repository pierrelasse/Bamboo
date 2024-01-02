package net.bluept.bamboo.services.challenges.forceitembattle.commands;

import net.bluept.bamboo.Bamboo;
import net.bluept.bamboo.services.system.command.Command;
import net.bluept.bamboo.services.challenges.forceitembattle.ItemService;
import net.bluept.bamboo.util.Utils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class FIBDevCmd extends Command {
    public List<String> revealColors;
    public String defaultRevealColor;

    public FIBDevCmd() {
        super("FibDev");
        revealColors = Arrays.asList("6", "7", "<#BF8970>");
        defaultRevealColor = "8";
        setPermission("penis");
    }

    @SuppressWarnings("deprecation")
    @Override
    public void executePlayer(Player player, List<String> args) {
        String subCommand = Utils.get(args, 0);
        String targetName = Utils.get(args, 1);
        Player target = (targetName == null) ? null : Bukkit.getPlayer(targetName);

        switch (subCommand) {
            case "regenchunk" -> {
                Bukkit.getScheduler().runTask(Bamboo.INS, () -> {
                    Chunk chunk = player.getChunk();
                    player.getWorld().regenerateChunk(chunk.getX(), chunk.getZ());
                    Utils.send(player, "&aDone!");
                });
            }
            case "playerinfo" -> {
                if (target == null) {
                    Utils.send(player, "&cUsage: /fibdev playerinfo <player: player>");
                    return;
                }

                ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class, player);
                if (itemService == null) {
                    return;
                }

                Material material = itemService.getPlayerMaterial(target.getUniqueId());
                Locale locale = target.locale();

                int jokerLeft = itemService.getJokerLeft(target.getUniqueId());
                Utils.send(player, "&dInfo for player &5" + target.getName() + "&8:");
                if (material != null) {
                    String key = material.getItemTranslationKey();
                    if (key != null) {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("&d  Current item&8: &6<lang:%k>".replace("%k", key)));
                    }
                }
                Utils.send(player, "&d  Items collected&8: &a" + itemService.getPlayerItems(target.getUniqueId()));
                Utils.send(player, "&d  Jokers left&8: &" + (jokerLeft == 0 ? "c" : "7") + jokerLeft + "&8/&7" + ItemService.MAX_JOKER);
                Utils.send(player, "&d  Locale&8: &f" + (locale.getCountry() + "_" + locale.getLanguage()).toLowerCase() + " - " + locale.getDisplayLanguage());
            }
            case "skipplayeritem" -> {
                if (target == null) {
                    Utils.send(player, "&cUsage: /fibdev skipplayeritem <player: player>");
                    return;
                }

                ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class, player);
                if (itemService == null) {
                    return;
                }

                itemService.nextPlayerMaterial(target.getUniqueId());
            }
            case "setplayeritems" -> {
                if (target == null) {
                    Utils.send(player, "&cUsage: /fibdev setplayeritems <player: player> <amount: number>");
                    return;
                }

                Integer amount = null;
                try {
                    amount = Integer.parseInt(Utils.get(args, 2));
                } catch (NumberFormatException ignored) {
                }
                if (amount == null || !(amount < 10000 && amount >= 0)) {
                    Utils.send(player, "&cInvalid amount");
                    return;
                }

                ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
                if (itemService == null) {
                    Utils.send(player, "&cUnable to connect to item service");
                    return;
                }

                if (amount == 0) {
                    itemService.playerItems.remove(target.getUniqueId());
                } else {
                    itemService.playerItems.put(target.getUniqueId(), amount);
                }
            }
            case "setplayerjokerleft" -> {
                if (target == null) {
                    Utils.send(player, "&cUsage: /fibdev setplayerjokerleft <player: player> <amount: number>");
                    return;
                }

                Integer amount = null;
                try {
                    amount = Integer.parseInt(Utils.get(args, 2));
                } catch (NumberFormatException ignored) {
                }
                if (amount == null || !(amount <= ItemService.MAX_JOKER && amount >= -100)) {
                    Utils.send(player, "&cInvalid amount");
                    return;
                }

                ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
                if (itemService == null) {
                    Utils.send(player, "&cUnable to connect to item service");
                    return;
                }

                if (amount == ItemService.MAX_JOKER) {
                    itemService.playerJoker.remove(target.getUniqueId());
                } else {
                    itemService.playerJoker.put(target.getUniqueId(), amount);
                }
            }
            case "reveal" -> {
                ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
                if (itemService == null) {
                    Utils.send(player, "&cFailed to connect to the item service");
                    return;
                }

                if (itemService.playerItems.isEmpty()) {
                    Utils.send(player, "&cNothing to display");
                    return;
                }
                String message = getReveal(itemService);
                Bukkit.getOnlinePlayers().forEach(i -> i.sendMessage(message));
            }
            case "revealself" -> {
                ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
                if (itemService == null) {
                    Utils.send(player, "&cFailed to connect to the item service");
                    return;
                }

                if (itemService.playerItems.isEmpty()) {
                    Utils.send(player, "&cNothing to display");
                    return;
                }

                player.sendMessage(getReveal(itemService));
            }
            case "resetall" -> {
                ItemService itemService = Bamboo.INS.serviceManager.getService(ItemService.class);
                if (itemService == null) {
                    Utils.send(player, "&cFailed to connect to the item service");
                    return;
                }

                itemService.playerItems.clear();
                itemService.playerMaterials.clear();
                itemService.playerJoker.clear();

                Utils.send(player, "&aEverything related to the force item battle was reset");
            }
            default -> {
                Utils.send(player, "&cUsage: /fibdev <regenchunk|playerinfo|skipplayeritem|setplayeritems|setplayerjokerleft|reveal|resetall>");
            }
        }
    }

    public String getReveal(ItemService itemService) {
        List<Map.Entry<UUID, Integer>> entryList = new ArrayList<>(itemService.playerItems.entrySet());
        entryList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        StringBuilder sb = new StringBuilder("&d&lErgebnisse:");
        int index = 0;
        for (Map.Entry<UUID, Integer> entry : entryList) {
            Player targetPlayer = Bukkit.getPlayer(entry.getKey());
            if (targetPlayer != null) {
                sb.append("\n  &").append(Utils.get(revealColors, index, defaultRevealColor))
                        .append("#").append(index + 1).append(" &d&l").append(targetPlayer.getName())
                        .append(" &8- &d").append(entry.getValue() + itemService.getJokerLeft(entry.getKey()));
                index++;
            }
        }

        return Utils.colorfy(sb.toString());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String alias, List<String> args) {
        List<String> completions = new ArrayList<>();

        String arg0 = Utils.get(args, 0, "");
        if (args.size() == 2) {
            // TODO: Potential crash
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(i -> i.startsWith(arg0)).toList());
        } else {
            for (String s : List.of("regenchunk", "playerinfo", "skipplayeritem", "setplayeritems", "setplayerjokerleft", "reveal", "revealself", "resetall")) {
                if (s.startsWith(arg0)) {
                    completions.add(s);
                }
            }
        }

        return completions;
    }
}
