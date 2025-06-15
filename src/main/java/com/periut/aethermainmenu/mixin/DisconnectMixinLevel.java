package com.periut.aethermainmenu.mixin;

import net.fabricmc.loader.api.FabricLoader;
import com.periut.aethermainmenu.AetherMenu;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(World.class)
public class DisconnectMixinLevel {
    @Shadow protected WorldProperties properties;

    @Inject(method = "disconnect", at=@At("HEAD"))
    public void disconnect(CallbackInfo ci)
    {
        World l = (World)((Object) this);
        if (!l.isRemote)
        {
            AetherMenu.lastLevel  = this.properties.getName();
            AetherMenu.shouldWorldLoad = true;

            Path configFolder = FabricLoader.getInstance().getConfigDir();
            Path lastsave = Path.of(configFolder.toString() + "/aether/lastsave.txt");

            try {
                Files.deleteIfExists(lastsave);
                Files.createFile(lastsave);
                Files.write(lastsave, AetherMenu.lastLevel.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
