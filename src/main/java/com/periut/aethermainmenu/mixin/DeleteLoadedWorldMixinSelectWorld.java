package com.periut.aethermainmenu.mixin;

import net.fabricmc.loader.api.FabricLoader;
import com.periut.aethermainmenu.AetherMenu;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(ConfirmScreen.class)
public class DeleteLoadedWorldMixinSelectWorld extends Screen {
    @Inject(method = "buttonClicked", at=@At("HEAD"))
    protected void buttonClicked(ButtonWidget par1, CallbackInfo ci)
    {
        // todo: check if its the current world deleted?
        if (par1.id == 0)
        {
            if (minecraft.world != null)
            {
                AetherMenu.startOrStopMenuWorld(minecraft);

                Path lastsave = Path.of(FabricLoader.getInstance().getConfigDir().toString() + "/aether/lastsave.txt");
                try {
                    Files.deleteIfExists(lastsave);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            AetherMenu.lastLevel = null;
            AetherMenu.LoadWorld(minecraft);
        }
    }
}
