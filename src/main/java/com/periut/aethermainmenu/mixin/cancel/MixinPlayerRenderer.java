package com.periut.aethermainmenu.mixin.cancel;

import com.periut.aethermainmenu.AetherMenu;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerRenderer {

    // Render Player
    @Inject(method = "renderMore(Lnet/minecraft/entity/player/PlayerEntity;F)V", at=@At("HEAD"), cancellable = true)
    protected void method_342(PlayerEntity f, float par2, CallbackInfo cir)
    {
        if (AetherMenu.musicId != null && !AetherMenu.renderPlayer)
        {
            cir.cancel();
        }
    }

    // Render Held
    @Inject(method = "render(Lnet/minecraft/entity/player/PlayerEntity;DDDFF)V", at=@At("HEAD"), cancellable = true)
    protected void method_341(PlayerEntity d, double e, double f, double g, float h, float par6, CallbackInfo cir)
    {
        if (AetherMenu.musicId != null && !AetherMenu.renderPlayer)
        {
            cir.cancel();
        }
    }

    // Render Armor
    @Inject(method = "bindTexture(Lnet/minecraft/entity/player/PlayerEntity;IF)Z", at=@At("HEAD"), cancellable = true)
    protected void method_344(PlayerEntity i, int f, float par3, CallbackInfoReturnable<Boolean> cir)
    {
        if (AetherMenu.musicId != null && !AetherMenu.renderPlayer)
        {
            cir.cancel();
        }
    }


    // Render Text
    @Inject(method = "renderNameTag(Lnet/minecraft/entity/player/PlayerEntity;DDD)V", at=@At("HEAD"), cancellable = true)
    protected void method_340(PlayerEntity d, double e, double f, double par4, CallbackInfo cir)
    {
        if (AetherMenu.musicId != null)
        {
            cir.cancel();
        }
    }
}
