package net.bluept.bamboo.services.emoji;

import net.bluept.bamboo.Bamboo;
import org.bukkit.event.player.PlayerChatEvent;

public class StaticEmoji {
    public static String translateEmojis(String s) {
        EmojiService emojiService = Bamboo.INS.serviceManager.getService(EmojiService.class);
        if (emojiService == null) {
            return s;
        }

        return emojiService.translate(s);
    }

    @SuppressWarnings("deprecation")
    public static void handleChatEvent(PlayerChatEvent event) {
        event.setMessage(StaticEmoji.translateEmojis(event.getMessage()));
    }
}
