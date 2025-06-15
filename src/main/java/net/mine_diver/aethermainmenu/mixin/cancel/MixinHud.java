package net.mine_diver.aethermainmenu.mixin.cancel;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.ScreenScaler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public abstract class MixinHud {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderVignette(FII)V", shift = At.Shift.BY, by = 2), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void cancelHud(float f, boolean flag, int i, int j, CallbackInfo ci, ScreenScaler var5, int var6, int var7) {
        if (AetherMenu.musicId != null)
            ci.cancel();
    }
}
