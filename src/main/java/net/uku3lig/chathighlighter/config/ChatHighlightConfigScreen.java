package net.uku3lig.chathighlighter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.CyclingOption;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.client.option.Option;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.ukulib.config.impl.StringInputScreen;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

import java.util.function.UnaryOperator;

public class ChatHighlightConfigScreen extends AbstractConfigScreen<ChatHighlighterConfig> {
    public ChatHighlightConfigScreen(Screen parent) {
        super(parent, Text.of("ChatHighlighter Config"), ChatHighlighter.getManager());
    }

    @Override
    protected Option[] getOptions(ChatHighlighterConfig config) {
        final Text textOption = new TranslatableText("chathighlighter.option.text");
        final UnaryOperator<Screen> textScreen = parent -> config.isUsePattern() ?
                new RegexInputScreen(parent, manager) :
                new StringInputScreen(parent, textOption, textOption, config::setText, config.getText(), manager);

        return new Option[]{
                Ukutils.createOpenButton("chathighlighter.option.text", config.getText(), textScreen),
                Ukutils.createOpenButton("chathighlighter.option.color", parent -> new HighlightSelectScreen(parent, manager)),
                new DoubleOption("chathighlighter.option.alpha", 0, 255, 1, opt -> (double) Byte.toUnsignedInt(config.getAlpha()),
                        (opt, value) -> config.setAlpha(value.byteValue()),
                        (opt, option) -> new TranslatableText("options.generic_value", new TranslatableText("chathighlighter.option.alpha"), Byte.toUnsignedInt(config.getAlpha()))),
                CyclingOption.create("chathighlighter.option.regex",
                        new TranslatableText("chathighlighter.option.regex.tooltip"),
                        opt -> config.isUsePattern(), (opt, option, value) -> config.setUsePattern(value)),
                CyclingOption.create("chathighlighter.option.play_sound", opt -> config.isPlaySound(), (opt, option, value) -> config.setPlaySound(value)),
                Ukutils.createOpenButton("chathighlighter.option.sound", parent -> new SoundInputScreen(parent, manager))
        };
    }
}
