package com.periut.aethermainmenu.mixin;

import net.fabricmc.loader.api.FabricLoader;
import com.periut.aethermainmenu.AetherButton;
import com.periut.aethermainmenu.AetherMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TitleScreen.class, priority = 1010)
public class CustomButtonMixinMainMenu extends Screen {
    @Inject(at = @At("RETURN"), method = "init")
    public void addMenuButton(CallbackInfo info) {
        AetherMenu.toolTip = "";
        this.buttons.add(new ButtonWidget(101, this.width - 25, 5, 20, 20,  "T") {
            @Override
            public void render(Minecraft minecraft, int i, int j)
            {
                boolean var5 = i >= this.x && j >= this.y && i < this.x + this.width && j < this.y + this.height;
                if (var5)
                {
                    if (AetherMenu.replaceBgTile)
                        AetherMenu.toolTip = "Normal Theme";
                    else
                        AetherMenu.toolTip = "Aether Theme";
                }
                else
                {
                    if (AetherMenu.toolTip.length() > 7)
                        if (AetherMenu.toolTip.charAt(7) == 'T')
                            AetherMenu.toolTip = "";
                }
                super.render(minecraft, i, j);
            }
        });
        this.buttons.add(new ButtonWidget(102, this.width - 50, 5, 20, 20,  "W") {
            @Override
            public void render(Minecraft minecraft, int i, int j)
            {
                active = visible = AetherMenu.visibleWorldButton && AetherMenu.lastLevel != null;
                if (active)
                {
                    boolean var5 = i >= this.x && j >= this.y && i < this.x + this.width && j < this.y + this.height;
                    if (var5)
                    {
                        AetherMenu.toolTip = "Toggle World";
                    }
                    else
                    {
                        if (AetherMenu.toolTip.length() > 0)
                            if (AetherMenu.toolTip.charAt(0) == 'T')
                                AetherMenu.toolTip = "";
                    }
                }
                super.render(minecraft, i, j);
            }
        });
        this.buttons.add(new ButtonWidget(103, this.width - 75, 5, 20, 20,  "P") {
            @Override
            public void render(Minecraft minecraft, int i, int j)
            {
                active = visible = AetherMenu.visibleWorldButton && minecraft.world != null;
                if (active)
                {
                    boolean var5 = i >= this.x && j >= this.y && i < this.x + this.width && j < this.y + this.height;
                    if (var5)
                    {
                        AetherMenu.toolTip = "Render Player";
                    }
                    else
                    {
                        if (AetherMenu.toolTip.length() > 0)
                            if (AetherMenu.toolTip.charAt(0) == 'R')
                                AetherMenu.toolTip = "";
                    }
                }
                super.render(minecraft, i, j);
            }
        });
        this.buttons.add(new ButtonWidget(104, this.width - 100, 5, 20, 20,  "Q") {
            @Override
            public void render(Minecraft minecraft, int i, int j)
            {
                active = visible = AetherMenu.visibleWorldButton && minecraft.world != null;
                if (active)
                {
                    boolean var5 = i >= this.x && j >= this.y && i < this.x + this.width && j < this.y + this.height;
                    if (var5)
                    {
                        AetherMenu.toolTip = "Quick Load";
                    }
                    else
                    {
                        if (AetherMenu.toolTip.length() > 0)
                            if (AetherMenu.toolTip.charAt(0) == 'Q')
                                AetherMenu.toolTip = "";
                    }
                }
                super.render(minecraft, i, j);
            }
        });
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"))
    private void onActionPerformed(ButtonWidget button, CallbackInfo ci) {
        if (button.id == 101) {
            AetherMenu.replaceBgTile = !AetherMenu.replaceBgTile;
        }
        if (button.id == 102)
        {
            AetherMenu.startOrStopMenuWorld(this.minecraft);
        }
        if (button.id == 103)
        {
            AetherMenu.renderPlayer = !AetherMenu.renderPlayer;
        }
        if (button.id == 104)
        {
            AetherMenu.quickLoad(this.minecraft);
        }

        if (button.id > 100 && button.id < 105)
        {
            AetherMenu.SaveCurrentSettings();
        }
    }
    @Inject(at = @At("RETURN"), method = "init")
    public void replaceMenuButtons(CallbackInfo info) {
        if (AetherMenu.modmenu) {
            ButtonWidget button = null;
            for (Object o : buttons) {
                ButtonWidget b = (ButtonWidget) o;
                if (b.id == 100) {
                    button = b;
                }
            }

            if (button == null)
                return;

            ButtonAccessor ba = (ButtonAccessor) button;
            AetherButton replacement = new AetherButton(button.id, button.x, button.y, ba.getWidth(), ba.getHeight(), button.text);
            this.buttons.remove(button);
            this.buttons.add(replacement);
        }
    }
}
