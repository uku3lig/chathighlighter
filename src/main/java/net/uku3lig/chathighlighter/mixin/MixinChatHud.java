package net.uku3lig.chathighlighter.mixin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.registry.Registries;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.chathighlighter.config.ChatHighlighterConfig;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;

@Mixin(ChatHud.class)
@Slf4j
public abstract class MixinChatHud {
    @Shadow
    protected abstract int getLineHeight();

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    public void highlight(Args args) {
        TextRenderer instance = MinecraftClient.getInstance().textRenderer;
        DrawContext context = args.get(0);
        OrderedText text = args.get(1);
        float y = args.get(3);

        final ChatHighlighterConfig config = ChatHighlighter.getManager().getConfig();
        final String str = Ukutils.getText(text).toLowerCase(Locale.ROOT);

        int highlightColor = config.getColor();
        if (highlightColor <= 0xFFFFFF) highlightColor |= config.getAlpha();

        if (config.isUsePattern() && config.getPattern().isPresent()) {
            Matcher matcher = config.getPattern().get().matcher(str);
            while (matcher.find()) {
                String before = str.substring(0, matcher.start());
                int beforeWidth = instance.getWidth(before) + ChatHighlighter.getOffset();
                int width = instance.getWidth(matcher.group());
                context.fill(beforeWidth, (int) y, width + beforeWidth, (int) y + getLineHeight(), highlightColor);
            }
        } else {
            for (String keyword : config.getText()) {
                keyword = keyword.toLowerCase(Locale.ROOT);
                int index = str.indexOf(keyword);
                while (index >= 0) {
                    String before = str.substring(0, index);
                    int beforeWidth = instance.getWidth(before) + ChatHighlighter.getOffset();
                    int width = instance.getWidth(keyword);
                    context.fill(beforeWidth, (int) y, width + beforeWidth, (int) y + getLineHeight(), highlightColor);
                    index = str.indexOf(keyword, index + 1);
                }
            }
        }
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V",
            at = @At("HEAD"))
    public void playSound(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo ci) {
        final ChatHighlighterConfig config = ChatHighlighter.getManager().getConfig();
        final String str = message.getString().toLowerCase(Locale.ROOT);

        if (!config.isPlaySound()) return;

        if (config.isUsePattern() && config.getPattern().isPresent()) {
            Matcher matcher = config.getPattern().get().matcher(str);
            if (matcher.find()) playSound(config);
        } else if (config.getText().stream().map(String::toLowerCase).anyMatch(str::contains)) {
            playSound(config);
        }
    }

    private void playSound(ChatHighlighterConfig config) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        Optional.ofNullable(config.getSound())
                .map(Identifier::new)
                .map(Registries.SOUND_EVENT::get)
                .ifPresent(e -> player.playSound(e, 1, 1));
    }
}
