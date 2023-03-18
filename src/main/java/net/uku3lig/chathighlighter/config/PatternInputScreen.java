package net.uku3lig.chathighlighter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.TextInputScreen;

import java.util.Optional;
import java.util.regex.Pattern;

public class PatternInputScreen extends TextInputScreen<String> {
    private final boolean isRegex;

    protected PatternInputScreen(Screen parent, ConfigManager<ChatHighlighterConfig> manager) {
        super(parent, Text.of("Pattern Input Screen"), Text.of("Pattern"),
                p -> manager.getConfig().setJoinedText(p), manager.getConfig().getJoinedText(), manager);
        this.isRegex = manager.getConfig().isUsePattern();
    }

    @Override
    protected void init() {
        super.init();
        this.getTextField().setMaxLength(1024);
        this.getTextField().setText(ChatHighlighter.getManager().getConfig().getJoinedText());

        if (isRegex) {
            addDrawableChild(ButtonWidget.builder(Text.of("Open regexr"), b -> Util.getOperatingSystem().open("https://regexr.com"))
                    .dimensions(this.width / 2 - 100, this.height - 51, 98, 20)
                    .build());
            addDrawableChild(ButtonWidget.builder(Text.of("Open regex101"), b -> Util.getOperatingSystem().open("https://regex101.com"))
                    .dimensions(this.width / 2 + 2, this.height - 51, 98, 20)
                    .build());
        }
    }

    @Override
    protected Optional<String> convert(String value) {
        if (value.isEmpty() || value.isBlank()) return Optional.empty();
        if (!isRegex) return Optional.of(value);

        try {
            return Optional.of(Pattern.compile(value).toString());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
