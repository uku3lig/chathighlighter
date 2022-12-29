package net.uku3lig.chathighlighter;

import net.minecraft.client.gui.screen.Screen;
import net.uku3lig.chathighlighter.config.ChatHighlightConfigScreen;
import net.uku3lig.ukulib.api.UkulibAPI;
import net.uku3lig.ukulib.config.screen.AbstractConfigScreen;

import java.util.function.Function;

public class UkulibHook implements UkulibAPI {
    @Override
    public Function<Screen, AbstractConfigScreen<?>> supplyConfigScreen() {
        return ChatHighlightConfigScreen::new;
    }
}
