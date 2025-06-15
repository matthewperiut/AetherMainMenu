package com.periut.aethermainmenu.mixin;

import com.periut.aethermainmenu.AetherButton;
import com.periut.aethermainmenu.AetherMenu;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.modificationstation.stationapi.api.util.math.ColorHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TitleScreen.class)
public class MixinMainMenu extends Screen {

    @Shadow private float ticks;

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    public boolean replaceButton(List instance, Object e)
    {
        ButtonWidget button = ((ButtonWidget) e);
        int id = button.id;
        ButtonAccessor b = (ButtonAccessor) button;

        return instance.add(new AetherButton(id, button.x, button.y, ((ButtonAccessor) button).getWidth(), ((ButtonAccessor) button).getHeight(), button.text));
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void startMusic(CallbackInfo ci) {
        if (AetherMenu.musicId == null) {
            AccessorSoundHelper.getSoundSystem().stop("BgMusic");
            AetherMenu.captureSoundId = true;
            minecraft.soundManager.playSound(AetherMenu.modular.toString(), 1, 1);
            AetherMenu.needsPlayerCreation = true;
        }
    }

    @ModifyConstant(method = "render(IIF)V", constant = @Constant(intValue = 5263440))
    private int modCon(int color) {
        AetherMenu.LoadWorld(this.minecraft);

        long delta = System.currentTimeMillis() - AetherMenu.musicStartTimestamp;
        if (delta >= 25000 && delta < 30000) {
            int red = (color & 0xFF0000) >> 16;
            int green = (color & 0x00FF00) >> 8;
            int blue = color & 0x0000FF;
            red += (delta - 25000) / (5000F / (256 - red));
            green += (delta - 25000) / (5000F / (256 - green));
            blue += (delta - 25000) / (5000F / (256 - blue));
            return red << 16 | green << 8 | blue;
        } else if (delta >= 30000)
            return 255 << 16 | 255 << 8 | 255;
        else
            return color;
    }

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void test(CallbackInfo ci) {
        if (ticks < 1) {
            AetherMenu.musicStartTimestamp = System.currentTimeMillis();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(int j, int f, float par3, CallbackInfo ci)
    {
        int x = this.width - this.textRenderer.getWidth(AetherMenu.toolTip) - 5;
        int y = 30;
        this.drawTextWithShadow(this.textRenderer, AetherMenu.toolTip, x, y, ColorHelper.Abgr.getAbgr(255,255,255, 255));

    }

    @Redirect(method = "render", at=@At(value = "INVOKE", target="Lorg/lwjgl/opengl/GL11;glBindTexture(II)V"))
    public void BindCustomTexture(int target, int texture)
    {
        if (AetherMenu.replaceBgTile)
        {
            if (minecraft.world != null)
            {
                GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("aethermainmenu:textures/gui/mclogomod1.png"));
            }
            else
            {
                GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("aethermainmenu:textures/gui/mclogomod2.png"));
            }
        }
        else
            GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/title/mclogo.png"));
    }

    @Redirect(method = "render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawTexture(IIIIII)V"))
    public void blit(TitleScreen mm, int i, int j, int k, int l, int m, int n) {

        if (!AetherMenu.replaceBgTile || this.minecraft.world == null)
        {
            this.drawTexture(i,j,k,l,m,n);
        }
        else
        {
            final byte var6 = 15;
            final byte var7 = 15;
            if (l == 0)
            {
                this.drawTexture(var6 + 0, var7 + 0, 0, 0, 155, 44);
            }
            if (l == 45)
            {
                this.drawTexture(var6 + 155, var7 + 0, 0, 45, 155, 44);
            }
        }
    }
    /*

                GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("aether:textures/title/mclogomod1.png"));
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                (MainMenu.class.cast(this)).blit(var6 + 0, var7 + 0, 0, 0, 155, 44);
                (MainMenu.class.cast(this)).blit(var6 + 155, var7 + 0, 0, 45, 155, 44);

     */

    @Redirect(method = "render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public void drawTextWithShadowCentred(TitleScreen instance, TextRenderer textRenderer, String s, int a, int b, int c)
    {
        if (!AetherMenu.replaceBgTile || this.minecraft.world == null)
        {
            this.drawCenteredTextWithShadow(this.textRenderer, s, 0, -8, 16776960);
        }
    }

    @Redirect(method = "render", at=@At(value = "INVOKE", target="Lnet/minecraft/client/gui/screen/TitleScreen;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public void drawTextWithShadow(TitleScreen instance, TextRenderer textRenderer, String text, int i, int j, int color)
    {
        if (text.charAt(0) == 'M' && minecraft.world != null) // moves minecraft b1.7.3 to bottom right corner
        {
            i = this.width - this.textRenderer.getWidth(text) - 2;
            j = this.height - 21; // 21 because that's what the original did
        }
        textRenderer.drawWithShadow(text, i, j, ColorHelper.Abgr.getAbgr(255,255,255, 255));
    }
}
