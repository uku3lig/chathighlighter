package net.uku3lig.chathighlighter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.TextInputScreen;

import java.util.Optional;
import java.util.regex.Pattern;

public class RegexInputScreen extends TextInputScreen<String> {
    protected RegexInputScreen(Screen parent, ConfigManager<ChatHighlighterConfig> manager) {
        super(parent, Text.of("Pattern Input Screen"), Text.of("Pattern"),
                p -> manager.getConfig().setText(p), manager.getConfig().getText(), manager);
    }

    @Override
    protected Optional<String> convert(String value) {
        try {
            return Optional.of(Pattern.compile(value).toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
