package net.uku3lig.chathighlighter;

import lombok.Getter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.uku3lig.chathighlighter.config.ChatHighlighterConfig;
import net.uku3lig.ukulib.config.ConfigManager;

@Environment(EnvType.CLIENT)
public class ChatHighlighter implements ClientModInitializer {
    @Getter
    private static final ConfigManager<ChatHighlighterConfig> manager = ConfigManager.create(ChatHighlighterConfig.class, "chathighlighter");

    @Override
    public void onInitializeClient() {
        // :true:
    }
}
