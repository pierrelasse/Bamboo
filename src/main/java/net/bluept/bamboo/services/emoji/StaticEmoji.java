package net.bluept.bamboo.services.emoji;

import net.bluept.bamboo.Bamboo;

public class StaticEmoji {
    public static String translateEmojis(String s) {
        EmojiService emojiService = Bamboo.INS.serviceManager.getService(EmojiService.class);
        if (emojiService == null) {
            return s;
        }
        return emojiService.translate(s);
    }
}
