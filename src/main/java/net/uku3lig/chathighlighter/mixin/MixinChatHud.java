package net.uku3lig.chathighlighter.mixin;

import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.chathighlighter.config.ChatHighlighterConfig;
import net.uku3lig.ukulib.utils.Ukutils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Locale;

@Mixin(ChatHud.class)
@Slf4j
public abstract class MixinChatHud extends DrawableHelper {
    @Shadow protected abstract int getLineHeight();

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/OrderedText;FFI)I"))
    public int highlight(TextRenderer instance, MatrixStack matrices, OrderedText text, float x, float y, int color) {
        final ChatHighlighterConfig config = ChatHighlighter.getManager().getConfig();
        final String keyword = config.getText().toLowerCase(Locale.ROOT);

        String str = Ukutils.getText(text).toLowerCase(Locale.ROOT);
        int index = str.indexOf(keyword);
        while (index >= 0) {
            String before = str.substring(0, index);
            int beforeWidth = instance.getWidth(before);
            int width = instance.getWidth(keyword);
            fill(matrices, beforeWidth, (int) y, width + beforeWidth, (int) y + getLineHeight(), config.getColor());
            index = str.indexOf(keyword, index + 1);
        }

        return instance.drawWithShadow(matrices, text, x, y, color);
    }
}
