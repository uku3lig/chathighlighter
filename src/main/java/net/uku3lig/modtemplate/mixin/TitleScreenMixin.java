package net.uku3lig.modtemplate.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.uku3lig.modtemplate.ModTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// See https://fabricmc.net/wiki/tutorial:mixin_introduction for a good introduction to mixins
// See https://github.com/SpongePowered/Mixin/wiki for the official mixin wiki
// See https://jenkins.liteloader.com/view/Other/job/Mixin/javadoc/index.html for the mixin javadocs
// This mixin simply replaces the splash text with `uku was here (again)`
@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Shadow private String splashText;

    @Inject(method = "init", at = @At("HEAD"))
    public void addSplashText(CallbackInfo ci) {
        ModTemplate.getLog().info("hi from TitleScreenMixin");
        this.splashText = "uku was here (again)";
    }
}
