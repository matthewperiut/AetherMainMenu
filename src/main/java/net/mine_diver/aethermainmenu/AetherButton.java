package net.mine_diver.aethermainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.render.TextRenderer;
import org.lwjgl.opengl.GL11;

public class AetherButton extends Button
{
    public AetherButton(int i, int j, int k, String string)
    {
        super(i, j, k, string);
    }

    // og for original
    int ogx;
    int ogy;
    int ogw;

    boolean initialize = true;

    public AetherButton(int i, int j, int k, int l, int m, String string)
    {
        super(i,j,k,l,m,string);
    }

    @Override
    public void render(Minecraft minecraft, int i, int j) {

        if (initialize)
        {
            ogx = this.x;
            ogy = this.y;
            ogw = this.width;
            initialize = false;
        }

        this.x = ogx;
        this.y = ogy;
        this.width = ogw;
        if (!AetherMenu.replaceBgTile)
        {
            super.render(minecraft, i, j);
            return;
        }
        else
        if (minecraft.level != null)
        {
            this.x = 5;
            this.y -= 30;

            if (AetherMenu.modmenu)
            {
                if (id == 100)
                {
                    this.y += 25;
                    this.width *= 2;
                    this.width += 2; // annoying but due to original being odd
                }

                if (id == 0)
                {
                    this.y += 25;
                }

                if (id == 3)
                {
                    this.width *= 2;
                    this.width += 2; // annoying but due to original being odd
                }
            }

        }

        if (this.visible) {
            TextRenderer var4 = minecraft.textRenderer;
            GL11.glBindTexture(3553, minecraft.textureManager.getTextureId("aethermainmenu:textures/gui/buttons.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var5 = i >= this.x && j >= this.y && i < this.x + this.width && j < this.y + this.height;
            int var6 = this.getYImage(var5);
            this.blit(this.x, this.y, 0, 46 + var6 * 20, this.width / 2, this.height);
            this.blit(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + var6 * 20, this.width / 2, this.height);
            this.postRender(minecraft, i, j);
            if (AetherMenu.replaceBgTile) {

                int offset = 20;
                if (minecraft.level != null || !AetherMenu.modmenu)
                {
                    offset = 30;
                }

                if (!this.active) {
                    this.drawTextWithShadow(var4, this.text, this.x + offset, this.y + (this.height - 8) / 2, -6250336);
                } else if (var5) {
                    this.drawTextWithShadow(var4, this.text, this.x + offset, this.y + (this.height - 8) / 2, 7851212);
                } else {
                    this.drawTextWithShadow(var4, this.text, this.x + offset, this.y + (this.height - 8) / 2, 14737632);
                }
            }
            else {
                if (!this.active) {
                    this.drawTextWithShadowCentred(var4, this.text, this.x + this.width / 2, this.y + (this.height - 8) / 2, -6250336);
                } else if (var5) {
                    this.drawTextWithShadowCentred(var4, this.text, this.x + this.width / 2, this.y + (this.height - 8) / 2, 16777120);
                } else {
                    this.drawTextWithShadowCentred(var4, this.text, this.x + this.width / 2, this.y + (this.height - 8) / 2, 14737632);
                }
            }
        }
    }
}
