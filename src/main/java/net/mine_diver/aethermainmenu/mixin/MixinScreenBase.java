package net.mine_diver.aethermainmenu.mixin;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Screen.class)
public abstract class MixinScreenBase extends DrawContext {

    @Shadow public int width;

    @Shadow public int height;

    @Shadow
    protected List buttons;

    @Shadow
    protected Minecraft minecraft;

    @Shadow public abstract void renderBackgroundTexture(int vOffset);

    @Redirect(method = "renderBackgroundTexture", at = @At(value = "INVOKE", target="Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"), require = 0)
    public void BindCustomTexture(int bind_to, int texture)
    {
        if (AetherMenu.replaceBgTile && ((ButtonWidget)buttons.get(1)).id == 2)
            GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("aethermainmenu:textures/gui/aetherBG.png"));
        else
            GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("/gui/background.png")); // vanilla
    }

    @Redirect(method = "renderBackgroundTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;color(I)V"), require = 0)
    public void invoke(Tessellator instance, int i)
    {
        if (this.minecraft.world != null)
        {
            instance.color(0,0,0,0);
        }
        else
        {
            if ((AetherMenu.replaceBgTile && ((ButtonWidget)buttons.get(1)).id == 2)) // has singleplayer button
                instance.color(10066329); // aether
            else
                instance.color(4210752); // vanilla
        }
    }

    @Inject(method = "shouldPause", at = @At("RETURN"), cancellable = true)
    private void isPauseScreen(CallbackInfoReturnable<Boolean> cir) {
        if (AetherMenu.musicId != null)
            cir.setReturnValue(false);
    }

    @Inject(method = "renderBackground(I)V", at = @At("HEAD"), cancellable = true)
    private void renderBackground(int alpha, CallbackInfo ci) {
        if (AetherMenu.musicId != null && this.minecraft.world != null) {
            long delta = System.currentTimeMillis() - AetherMenu.musicStartTimestamp;
            if (delta <= 30000)
                renderBackgroundTexture(alpha);
            ci.cancel();
        }
    }
}
