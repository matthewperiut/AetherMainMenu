package net.mine_diver.aethermainmenu.mixin.cancel;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerBase.class, priority = 990)
public class MixinPlayerBase extends Living {

    public MixinPlayerBase(Level arg) {
        super(arg);
    }

    @Inject(method = "handleFallDamage(F)V", at = @At("HEAD"), cancellable = true)
    private void handleFallDamage(float height, CallbackInfo ci) {
        if (AetherMenu.musicId != null)
            ci.cancel();
    }

    @Override
    public boolean method_932() { // dont fall down ladders
        if (AetherMenu.musicId == null)
            return super.method_932();
        else
            return false;
    }
    @Inject(method = "method_520", at = @At("HEAD"), cancellable = true)
    public void EntityCollide(EntityBase par1, CallbackInfo ci) {
        if (AetherMenu.musicId != null)
            ci.cancel();
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(EntityBase i, int par2, CallbackInfoReturnable<Boolean> ci)
    {
        if (AetherMenu.musicId != null)
            ci.cancel();
    }

/*
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci)
    {
        if (AetherMenu.musicId != null)
        {
            ci.cancel();
        }
    }*/
}
