package net.mine_diver.aethermainmenu.mixin.cancel;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.entity.player.PlayerBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer {

    // Render Player
    @Inject(method = "method_342", at=@At("HEAD"), cancellable = true)
    protected void method_342(PlayerBase f, float par2, CallbackInfo cir)
    {
        if (AetherMenu.musicId != null && !AetherMenu.renderPlayer)
        {
            cir.cancel();
        }
    }

    // Render Held
    @Inject(method = "method_341", at=@At("HEAD"), cancellable = true)
    protected void method_341(PlayerBase d, double e, double f, double g, float h, float par6, CallbackInfo cir)
    {
        if (AetherMenu.musicId != null && !AetherMenu.renderPlayer)
        {
            cir.cancel();
        }
    }

    // Render Armor
    @Inject(method = "method_344", at=@At("HEAD"), cancellable = true)
    protected void method_344(PlayerBase i, int f, float par3, CallbackInfoReturnable<Boolean> cir)
    {
        if (AetherMenu.musicId != null && !AetherMenu.renderPlayer)
        {
            cir.cancel();
        }
    }


    // Render Text
    @Inject(method = "method_340", at=@At("HEAD"), cancellable = true)
    protected void method_340(PlayerBase d, double e, double f, double par4, CallbackInfo cir)
    {
        if (AetherMenu.musicId != null)
        {
            cir.cancel();
        }
    }
}
