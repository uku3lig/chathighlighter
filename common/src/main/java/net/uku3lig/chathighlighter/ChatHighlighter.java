package net.uku3lig.chathighlighter;

import lombok.Getter;
import net.uku3lig.chathighlighter.config.ChatHighlighterConfig;
import net.uku3lig.ukulib.config.ConfigManager;

public class ChatHighlighter {
    @Getter
    private static final ConfigManager<ChatHighlighterConfig> manager = ConfigManager.createDefault(ChatHighlighterConfig.class, "chathighlighter");

    private ChatHighlighter() {}
}
