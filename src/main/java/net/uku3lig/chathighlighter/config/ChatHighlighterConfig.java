package net.uku3lig.chathighlighter.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.uku3lig.ukulib.config.IConfig;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatHighlighterConfig implements IConfig<ChatHighlighterConfig> {
    private String text;
    private int color;

    @Override
    public ChatHighlighterConfig defaultConfig() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        return new ChatHighlighterConfig(player != null ? player.getEntityName() : "uku3lig", 0x7FFFFF00);
    }
}
