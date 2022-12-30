package net.uku3lig.chathighlighter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.uku3lig.ukulib.config.IConfig;

import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatHighlighterConfig implements IConfig<ChatHighlighterConfig> {
    private String text;
    private int color;
    private boolean usePattern;
    private boolean playSound;

    public Optional<Pattern> getPattern() {
        try {
            return Optional.of(Pattern.compile(text));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ChatHighlighterConfig defaultConfig() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return new ChatHighlighterConfig(player != null ? player.getEntityName() : "uku3lig", 0x7FFFFF00, false, true);
    }
}
