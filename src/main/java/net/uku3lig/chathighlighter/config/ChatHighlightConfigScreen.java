package net.uku3lig.chathighlighter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.ukulib.config.impl.StringInputScreen;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.config.screen.ColorSelectScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class ChatHighlightConfigScreen extends AbstractConfigScreen<ChatHighlighterConfig> {
    public ChatHighlightConfigScreen(Screen parent) {
        super(parent, Text.of("ChatHighlighter Config"), ChatHighlighter.getManager());
    }

    @Override
    protected SimpleOption<?>[] getOptions(ChatHighlighterConfig config) {
        final Text textOption = Text.translatable("chathighlighter.option.text");

        return new SimpleOption[]{
                Ukutils.createOpenButton("chathighlighter.option.text", config.getText(), parent -> new StringInputScreen(
                        parent, textOption, textOption, config::setText, config.getText(), manager)),
                Ukutils.createOpenButton("chathighlighter.option.color", parent -> new ColorSelectScreen(
                        Text.translatable("chathighlighter.option.color"), parent, config::setColor, config.getColor(), manager))
        };
    }
}
