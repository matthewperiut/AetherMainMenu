package net.mine_diver.aethermainmenu.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.DeleteConfirmation;
import net.minecraft.client.gui.widgets.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(DeleteConfirmation.class)
public class DeleteLoadedWorldMixinSelectWorld extends ScreenBase {
    @Inject(method = "buttonClicked", at=@At("HEAD"))
    protected void buttonClicked(Button par1, CallbackInfo ci)
    {
        // todo: check if its the current world deleted?
        if (par1.id == 0)
        {
            if (minecraft.level != null)
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
