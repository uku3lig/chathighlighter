package net.uku3lig.chathighlighter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.uku3lig.chathighlighter.ChatHighlighter;
import net.uku3lig.ukulib.config.ConfigManager;
import net.uku3lig.ukulib.config.screen.ColorSelectScreen;

public class HighlightSelectScreen extends ColorSelectScreen {
    public HighlightSelectScreen(Screen parent, ConfigManager<ChatHighlighterConfig> manager) {
        super(Text.of("Highlight Color Selection"), parent,
                c -> manager.getConfig().setColor(c), manager.getConfig().getColor(), manager);
    }

    @Override
    protected boolean allowAlpha() {
        return false;
    }

    @Override
    protected byte defaultAlpha() {
        return ChatHighlighter.getManager().getConfig().getAlpha();
    }

    @Override
    protected void renderColor(MatrixStack matrices, int mouseX, int mouseY, float delta, int color) {
        OrderedText text = Text.of("Look at this super cool highlighted text!").asOrderedText();
        int centerX = this.width / 2;
        int x1 = centerX - textRenderer.getWidth(text) / 2 + textRenderer.getWidth("Look at this super cool ");
        int x2 = x1 + textRenderer.getWidth("highlighted");
        int y1 = 146;
        int y2 = y1 + textRenderer.fontHeight;

        fill(matrices, x1, y1, x2, y2, color);
        drawCenteredTextWithShadow(matrices, textRenderer, text, centerX, y1, 0xFFFFFF);
    }
}
