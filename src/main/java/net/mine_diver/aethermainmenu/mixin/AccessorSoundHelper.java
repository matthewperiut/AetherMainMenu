package net.mine_diver.aethermainmenu.mixin;

import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import paulscode.sound.SoundSystem;

@Mixin(SoundManager.class)
public interface AccessorSoundHelper {

    @Accessor
    static SoundSystem getSoundSystem() {
        throw new AssertionError("Mixin!");
    }
}
