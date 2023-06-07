package net.uku3lig.chathighlighter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.uku3lig.ukulib.config.IConfig;

import java.util.*;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatHighlighterConfig implements IConfig<ChatHighlighterConfig> {
    private List<String> text = new ArrayList<>();
    private int color = 0xFFFF00;
    private byte alpha = (byte) 0x7F;
    private boolean usePattern = false;
    private boolean playSound = true;
    private String sound = "block.note_block.bell";

    public Optional<Pattern> getPattern() {
        if (text.isEmpty()) return Optional.empty();
        String first = text.get(0);
        if (first.isEmpty() || first.isBlank()) return Optional.empty();

        try {
            return Optional.of(Pattern.compile(first));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String getJoinedText() {
        return String.join(", ", text);
    }

    public void setJoinedText(String text) {
        this.text = new ArrayList<>(Arrays.asList(text.split(", *")));
    }

    private ChatHighlighterConfig(String text) {
        this.text = new ArrayList<>();
        this.text.add(text);
    }

    @Override
    public ChatHighlighterConfig defaultConfig() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return new ChatHighlighterConfig(player != null ? player.getEntityName() : "uku3lig");
    }
}
