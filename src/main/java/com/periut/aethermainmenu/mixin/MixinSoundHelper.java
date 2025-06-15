package com.periut.aethermainmenu.mixin;

import com.periut.aethermainmenu.AetherMenu;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SoundManager.class)
public class MixinSoundHelper {

    @Inject(method = "playSound(Ljava/lang/String;FF)V", at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;newSource(ZLjava/lang/String;Ljava/net/URL;Ljava/lang/String;ZFFFIF)V", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureSoundId(String string, float f, float f1, CallbackInfo ci, Sound var4, String var5) {
        if (AetherMenu.captureSoundId) {
            AetherMenu.musicId = var5;
            AetherMenu.captureSoundId = false;
        }
    }
}
