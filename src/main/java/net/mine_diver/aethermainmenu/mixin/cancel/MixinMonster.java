package net.mine_diver.aethermainmenu.mixin.cancel;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.monster.MonsterBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MonsterBase.class)
public class MixinMonster {
    @Inject(method = "getAttackTarget", at=@At("HEAD"), cancellable = true)
    public void getAttackTarget(CallbackInfoReturnable<EntityBase> cir) {
        if (AetherMenu.musicId != null)
        {
            cir.cancel();
        }
    }
}
