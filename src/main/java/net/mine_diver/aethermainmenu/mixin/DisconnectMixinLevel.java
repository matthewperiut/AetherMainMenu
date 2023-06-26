package net.mine_diver.aethermainmenu.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.level.Level;
import net.minecraft.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(Level.class)
public class DisconnectMixinLevel {
    @Shadow protected LevelProperties properties;

    @Inject(method = "onPlayerDisconnect", at=@At("HEAD"))
    public void disconnect(CallbackInfo ci)
    {
        Level l = (Level)((Object) this);
        if (!l.isServerSide)
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
