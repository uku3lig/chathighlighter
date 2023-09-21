package net.uku3lig.chathighlighter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.ukulib.config.option.ColorOption;
import net.uku3lig.ukulib.config.option.CyclingOption;
import net.uku3lig.ukulib.config.option.InputOption;
import net.uku3lig.ukulib.config.option.WidgetCreator;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

import java.util.Collections;
import java.util.regex.Pattern;

public class ChatHighlightConfigScreen extends AbstractConfigScreen<ChatHighlighterConfig> {
    public ChatHighlightConfigScreen(Screen parent) {
        super("ChatHighlighter Config", parent, ChatHighlighter.getManager());
    }

    @Override
    protected WidgetCreator[] getWidgets(ChatHighlighterConfig config) {
        return new WidgetCreator[]{
                new InputOption("chathighlighter.option.text", config.getJoinedText(), s -> {
                    if (config.isUsePattern()) config.setText(Collections.singletonList(s));
                    else config.setJoinedText(s);
                }, s -> !s.isBlank() && (!config.isUsePattern() || isValidPattern(s))),
                new ColorOption("chathighlighter.option.color", config.getColor(), config::setColor, true),
                CyclingOption.ofBoolean("chathighlighter.option.regex", config.isUsePattern(), config::setUsePattern,
                        SimpleOption.constantTooltip(Text.translatable("chathighlighter.option.regex.tooltip"))),
                CyclingOption.ofBoolean("chathighlighter.option.play_sound", config.isPlaySound(), config::setPlaySound),
                new InputOption("chathighlighter.option.sound", config.getSound(), config::setSound,
                        s -> Registries.SOUND_EVENT.containsId(new Identifier(s))),
        };
    }

    private boolean isValidPattern(String s) {
        try {
            Pattern.compile(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
