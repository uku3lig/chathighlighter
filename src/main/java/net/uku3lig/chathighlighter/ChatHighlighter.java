package net.uku3lig.chathighlighter;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.uku3lig.chathighlighter.config.ChatHighlighterConfig;
import net.uku3lig.ukulib.config.ConfigManager;

@Environment(EnvType.CLIENT)
public class ChatHighlighter {
    @Getter
    private static final ConfigManager<ChatHighlighterConfig> manager = ConfigManager.create(ChatHighlighterConfig.class, "chathighlighter");

    public static int getOffset() {
        return FabricLoader.getInstance().isModLoaded("chat_heads") ? 10 : 0;
    }

    private ChatHighlighter() {}
}
