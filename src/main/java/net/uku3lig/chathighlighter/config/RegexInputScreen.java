package net.uku3lig.chathighlighter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
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
    protected void init() {
        super.init();
        addDrawableChild(new ButtonWidget(this.width / 2 - 100, this.height - 51, 98, 20, Text.of("Open regexr"),
                        b -> Util.getOperatingSystem().open("https://regexr.com")));
        addDrawableChild(new ButtonWidget(this.width / 2 + 2, this.height - 51, 98, 20, Text.of("Open regex101"),
                b -> Util.getOperatingSystem().open("https://regex101.com")));
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
