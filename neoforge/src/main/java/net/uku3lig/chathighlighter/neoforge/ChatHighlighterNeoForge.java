package net.uku3lig.chathighlighter.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.uku3lig.chathighlighter.UkulibHook;
import net.uku3lig.ukulib.neoforge.UkulibNFProvider;

@Mod(value = "chathighlighter", dist = Dist.CLIENT)
public class ChatHighlighterNeoForge {
    public ChatHighlighterNeoForge(ModContainer container) {
        container.registerExtensionPoint(UkulibNFProvider.class, UkulibHook::new);
    }
}
