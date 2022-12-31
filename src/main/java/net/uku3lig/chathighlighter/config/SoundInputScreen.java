package net.uku3lig.chathighlighter.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.TextInputScreen;

import java.util.Optional;

public class SoundInputScreen extends TextInputScreen<String> {
    private ButtonWidget playButton;

    protected SoundInputScreen(Screen parent, ConfigManager<ChatHighlighterConfig> manager) {
        super(parent, Text.of("Sound Selection"), Text.of("Sound identifier"),
                s -> manager.getConfig().setSound(s), manager.getConfig().getSound(), manager);
    }

    @Override
    protected void init() {
        super.init();
        playButton = addDrawableChild(ButtonWidget.builder(Text.of("Play Sound"),
                        b -> convert(getTextField().getText())
                                .map(Identifier::new)
                                .map(Registries.SOUND_EVENT::get)
                                .ifPresent(e -> MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(e, 1))))
                .dimensions(this.width / 2 - 100, this.height - 51, 200, 20)
                .build());
    }

    @Override
    protected void onTextChange(String value) {
        playButton.active = convert(value).isPresent();
    }

    @Override
    protected Optional<String> convert(String value) {
        try {
            if (Registries.SOUND_EVENT.containsId(new Identifier(value))) {
                return Optional.of(value);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
