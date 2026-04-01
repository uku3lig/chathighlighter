package net.uku3lig.chathighlighter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.chathighlighter.config.ChatHighlighterConfig;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.regex.Matcher;

@Mixin(ChatComponent.class)
public abstract class MixinChatComponent {
    // fucky fix for chat patches compat, don't like it, but it is how it is
    @Unique
    private static final Set<Integer> pingedTicks = new HashSet<>();

    @WrapOperation(method = "lambda$extractRenderState$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;fill(IIIII)V", ordinal = 0))
    private static void highlight(ChatComponent.ChatGraphicsAccess instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original, @Local(argsOnly = true) GuiMessage.Line line, @Local(argsOnly = true, ordinal = 1) float opacity) {
        original.call(instance, x1, y1, x2, y2, color);

        final ChatHighlighterConfig config = ChatHighlighter.getManager().getConfig();
        final Font font = Minecraft.getInstance().font;
        final String str = Ukutils.getText(line.content()).toLowerCase(Locale.ROOT);

        int highlightColor = ARGB.color((int) ((config.getColor() >> 24) * opacity), config.getColor() & 0xFFFFFF);

        if (config.isUsePattern() && config.getPattern().isPresent()) {
            Matcher matcher = config.getPattern().get().matcher(str);
            while (matcher.find()) {
                String before = str.substring(0, matcher.start());
                int beforeWidth = font.width(before) + ChatHighlighter.getOffset();
                int width = font.width(matcher.group());
                instance.fill(beforeWidth, y1, width + beforeWidth, y2, highlightColor);
            }
        } else {
            for (String keyword : config.getText()) {
                keyword = keyword.toLowerCase(Locale.ROOT);
                int index = str.indexOf(keyword);
                while (index >= 0) {
                    String before = str.substring(0, index);
                    int beforeWidth = font.width(before) + ChatHighlighter.getOffset();
                    int width = font.width(keyword);
                    instance.fill(beforeWidth, y1, width + beforeWidth, y2, highlightColor);
                    index = str.indexOf(keyword, index + 1);
                }
            }
        }
    }

    @Inject(method = "addMessageToQueue", at = @At("HEAD"))
    public void playSound(GuiMessage message, CallbackInfo ci) {
        final ChatHighlighterConfig config = ChatHighlighter.getManager().getConfig();
        final String str = message.content().getString().toLowerCase(Locale.ROOT);

        if (!config.isPlaySound()) return;

        if (config.isUsePattern() && config.getPattern().isPresent()) {
            Matcher matcher = config.getPattern().get().matcher(str);
            if (matcher.find()) playSound(config, message.addedTime());
        } else if (config.getText().stream().map(String::toLowerCase).anyMatch(str::contains)) {
            playSound(config, message.addedTime());
        }
    }

    @Unique
    private void playSound(ChatHighlighterConfig config, int ticks) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || pingedTicks.contains(ticks)) return;

        pingedTicks.add(ticks);

        Optional.ofNullable(config.getSound())
                .map(Identifier::tryParse)
                .map(BuiltInRegistries.SOUND_EVENT::getValue)
                .ifPresent(e -> player.playSound(e, 1, 1));
    }
}
