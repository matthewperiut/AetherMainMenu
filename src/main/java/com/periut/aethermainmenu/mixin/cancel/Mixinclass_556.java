package com.periut.aethermainmenu.mixin.cancel;

import com.periut.aethermainmenu.AetherMenu;
import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class Mixinclass_556 {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void cancelHand(float f, CallbackInfo ci) {

        if (AetherMenu.musicId != null)
            ci.cancel();
    }
}
