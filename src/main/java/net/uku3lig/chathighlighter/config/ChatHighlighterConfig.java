package net.uku3lig.chathighlighter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
@Getter
@Setter
public class ChatHighlighterConfig implements Serializable {
    private List<String> text;
    private int color = 0x7FFFFF00;
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

    @SuppressWarnings("unused")
    public ChatHighlighterConfig() {
        this(MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.getNameForScoreboard() : "uku3lig");
    }
}
