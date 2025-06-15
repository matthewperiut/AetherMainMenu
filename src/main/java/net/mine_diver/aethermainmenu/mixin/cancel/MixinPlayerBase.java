package net.mine_diver.aethermainmenu.mixin.cancel;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 990)
public class MixinPlayerBase extends LivingEntity {

    public MixinPlayerBase(World arg) {
        super(arg);
    }

    @Inject(method = "onLanding", at = @At("HEAD"), cancellable = true)
    private void handleFallDamage(float height, CallbackInfo ci) {
        if (AetherMenu.musicId != null)
            ci.cancel();
    }

    @Override
    public boolean isOnLadder() { // dont fall down ladders
        if (AetherMenu.musicId == null)
            return super.isOnLadder();
        else
            return false;
    }
    @Inject(method = "collideWithEntity", at = @At("HEAD"), cancellable = true)
    public void EntityCollide(Entity par1, CallbackInfo ci) {
        if (AetherMenu.musicId != null)
            ci.cancel();
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    public void damage(Entity i, int par2, CallbackInfoReturnable<Boolean> ci)
    {
        if (AetherMenu.musicId != null)
            ci.cancel();
    }
}
