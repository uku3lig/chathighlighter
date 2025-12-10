package net.uku3lig.chathighlighter.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
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

@Mixin(ChatHud.class)
public abstract class MixinChatHud {
    // fucky fix for chat patches compat, don't like it, but it is how it is
    @Unique
    private static final Set<Integer> pingedTicks = new HashSet<>();

    @WrapOperation(method = "method_75802", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud$Backend;fill(IIIII)V", ordinal = 0))
    private static void highlight(ChatHud.Backend instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original, @Local(argsOnly = true) ChatHudLine.Visible line, @Local(argsOnly = true, ordinal = 1) float opacity) {
        original.call(instance, x1, y1, x2, y2, color);

        final ChatHighlighterConfig config = ChatHighlighter.getManager().getConfig();
        final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        final String str = Ukutils.getText(line.content()).toLowerCase(Locale.ROOT);

        int highlightColor = ColorHelper.withAlpha((int) ((config.getColor() >> 24) * opacity), config.getColor() & 0xFFFFFF);

        if (config.isUsePattern() && config.getPattern().isPresent()) {
            Matcher matcher = config.getPattern().get().matcher(str);
            while (matcher.find()) {
                String before = str.substring(0, matcher.start());
                int beforeWidth = textRenderer.getWidth(before) + ChatHighlighter.getOffset();
                int width = textRenderer.getWidth(matcher.group());
                instance.fill(beforeWidth, y1, width + beforeWidth, y2, highlightColor);
            }
        } else {
            for (String keyword : config.getText()) {
                keyword = keyword.toLowerCase(Locale.ROOT);
                int index = str.indexOf(keyword);
                while (index >= 0) {
                    String before = str.substring(0, index);
                    int beforeWidth = textRenderer.getWidth(before) + ChatHighlighter.getOffset();
                    int width = textRenderer.getWidth(keyword);
                    instance.fill(beforeWidth, y1, width + beforeWidth, y2, highlightColor);
                    index = str.indexOf(keyword, index + 1);
                }
            }
        }
    }

    @Inject(method = "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At("HEAD"))
    public void playSound(ChatHudLine message, CallbackInfo ci) {
        final ChatHighlighterConfig config = ChatHighlighter.getManager().getConfig();
        final String str = message.content().getString().toLowerCase(Locale.ROOT);

        if (!config.isPlaySound()) return;

        if (config.isUsePattern() && config.getPattern().isPresent()) {
            Matcher matcher = config.getPattern().get().matcher(str);
            if (matcher.find()) playSound(config, message.creationTick());
        } else if (config.getText().stream().map(String::toLowerCase).anyMatch(str::contains)) {
            playSound(config, message.creationTick());
        }
    }

    @Unique
    private void playSound(ChatHighlighterConfig config, int ticks) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null || pingedTicks.contains(ticks)) return;

        pingedTicks.add(ticks);

        Optional.ofNullable(config.getSound())
                .map(Identifier::tryParse)
                .map(Registries.SOUND_EVENT::get)
                .ifPresent(e -> player.playSound(e, 1, 1));
    }
}
