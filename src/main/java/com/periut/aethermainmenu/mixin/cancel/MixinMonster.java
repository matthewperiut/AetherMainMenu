package com.periut.aethermainmenu.mixin.cancel;

import com.periut.aethermainmenu.AetherMenu;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MonsterEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MonsterEntity.class)
public class MixinMonster {
    @Inject(method = "getTargetInRange", at=@At("HEAD"), cancellable = true)
    public void getAttackTarget(CallbackInfoReturnable<Entity> cir) {
        if (AetherMenu.musicId != null)
        {
            cir.cancel();
        }
    }
}
