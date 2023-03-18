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
    private byte alpha;
    private boolean usePattern;
    private boolean playSound;
    private String sound;

    public Optional<Pattern> getPattern() {
        if (text.isEmpty() || text.isBlank()) return Optional.empty();

        try {
            return Optional.of(Pattern.compile(text));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public ChatHighlighterConfig defaultConfig() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return new ChatHighlighterConfig(player != null ? player.getEntityName() : "uku3lig", 0xFFFF00, (byte) 0x7F, false, true, "block.note_block.bell");
    }
}
