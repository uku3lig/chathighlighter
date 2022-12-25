package net.uku3lig.chathighlighter.mixin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.uku3lig.chathighlighter.ChatHighlighter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Locale;

@Mixin(ChatHud.class)
@Slf4j
public abstract class MixinChatHud extends DrawableHelper {
    @Shadow protected abstract int getLineHeight();

    private static final String KEYWORD = "piss";

    @ModifyArg(method = "render", index = 5, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 0))
    public int editColor(int original) {
        return original + 0xFF00;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"))
    public int highlight(TextRenderer instance, MatrixStack matrices, OrderedText text, float x, float y, int color) {
        String str = ChatHighlighter.getText(text);
        if (str.toLowerCase(Locale.ROOT).contains(KEYWORD)) {
            String before = str.substring(0, str.indexOf(KEYWORD));
            int beforeWidth = instance.getWidth(before);
            int width = instance.getWidth(KEYWORD);
            fill(matrices, beforeWidth-1, (int) y-1, width + beforeWidth + 1, (int) y + getLineHeight() + 1, 0xB0_FFFF00);
        }

        return instance.drawWithShadow(matrices, text, x, y, color);
    }
}
