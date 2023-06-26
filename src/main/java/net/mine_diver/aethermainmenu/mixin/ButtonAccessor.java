package net.mine_diver.aethermainmenu.mixin;

import net.minecraft.client.gui.widgets.Button;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Button.class)
public interface ButtonAccessor
{
	@Accessor(value = "width")
	int getWidth();
	@Accessor(value = "height")
	int getHeight();
}
