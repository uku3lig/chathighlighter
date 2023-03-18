package net.uku3lig.chathighlighter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;
import net.uku3lig.ukulib.utils.Ukutils;

public class ChatHighlightConfigScreen extends AbstractConfigScreen<ChatHighlighterConfig> {
    public ChatHighlightConfigScreen(Screen parent) {
        super(parent, Text.of("ChatHighlighter Config"), ChatHighlighter.getManager());
    }

    @Override
    protected SimpleOption<?>[] getOptions(ChatHighlighterConfig config) {
        return new SimpleOption[]{
                Ukutils.createOpenButton("chathighlighter.option.text", config.getText(), parent -> new PatternInputScreen(parent, manager)),
                Ukutils.createOpenButton("chathighlighter.option.color", parent -> new HighlightSelectScreen(parent, manager)),
                new SimpleOption<>("chathighlighter.option.alpha", SimpleOption.emptyTooltip(), GameOptions::getGenericValueText,
                        new SimpleOption.ValidatingIntSliderCallbacks(0, 255), Byte.toUnsignedInt(config.getAlpha()), i -> config.setAlpha(i.byteValue())),
                SimpleOption.ofBoolean("chathighlighter.option.regex",
                        SimpleOption.constantTooltip(Text.translatable("chathighlighter.option.regex.tooltip")),
                        config.isUsePattern(), config::setUsePattern),
                SimpleOption.ofBoolean("chathighlighter.option.play_sound", config.isPlaySound(), config::setPlaySound),
                Ukutils.createOpenButton("chathighlighter.option.sound", parent -> new SoundInputScreen(parent, manager))
        };
    }
}
