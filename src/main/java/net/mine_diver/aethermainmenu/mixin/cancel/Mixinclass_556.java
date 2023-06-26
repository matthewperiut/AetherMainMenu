package net.mine_diver.aethermainmenu.mixin.cancel;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.class_556;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_556.class)
public class Mixinclass_556 {

    @Inject(method = "method_1860(F)V", at = @At("HEAD"), cancellable = true)
    private void cancelHand(float f, CallbackInfo ci) {

        if (AetherMenu.musicId != null)
            ci.cancel();
    }
}
