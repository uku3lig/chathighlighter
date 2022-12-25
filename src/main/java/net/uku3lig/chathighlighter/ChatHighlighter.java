package net.uku3lig.chathighlighter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.OrderedText;

@Environment(EnvType.CLIENT)
public class ChatHighlighter implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // :true:
    }

    public static String getText(OrderedText text) {
        StringBuilder builder = new StringBuilder();
        text.accept((i, style, j) -> {
            builder.append(Character.toChars(j));
            return true;
        });
        return builder.toString();
    }
}
