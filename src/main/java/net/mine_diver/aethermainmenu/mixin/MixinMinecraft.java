package net.mine_diver.aethermainmenu.mixin;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitType;
import net.minecraft.util.maths.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow public Living viewEntity;

    @Shadow public AbstractClientPlayer player;

    @Shadow private int ticksPlayed;

    @Shadow public Level level;

    @Shadow
    private static Minecraft instance;

    PlayerBase savedPlayer;

    @Inject(method = "openScreen(Lnet/minecraft/client/gui/screen/ScreenBase;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;lockCursor()V", shift = At.Shift.BEFORE))
    private void stopMusic(ScreenBase screen, CallbackInfo ci) {
        if (AetherMenu.musicId != null) {
            AccessorSoundHelper.getSoundSystem().stop(AetherMenu.musicId);
            AetherMenu.musicId = null;
            AetherMenu.needsPlayerCreation = false;
            //viewEntity = savedPlayer;
            //level.spawnEntity(savedPlayer);

        }
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ScreenBase;tick()V", shift = At.Shift.AFTER))
    private void tick(CallbackInfo ci) {
        // "ticksPlayed" is available to use

        if (AetherMenu.musicId != null)
        {
            if (this.level != null)
            {
                if (AetherMenu.needsPlayerCreation)
                {
                    viewEntity = new PlayerBase(level) {
                        @Override
                        public void method_494() {
                        }

                        @Override
                        public boolean damage(EntityBase arg, int i) {
                            return false;
                        }

                        @Override
                        protected void applyDamage(int i) {

                        }
                    };
                    viewEntity.width = 0;
                    viewEntity.height = 0;
                    level.spawnEntity(viewEntity);

                    savedPlayer = (PlayerBase) player;

                    viewEntity.setPosition(savedPlayer.x, savedPlayer.y, savedPlayer.z);
                    AetherMenu.needsPlayerCreation = false;
                }

                if (viewEntity != null)
                {
                    double radius = 5.0F; // Adjust this as needed
                    double angle = ticksPlayed / 200F; // Adjust this as needed

                    Vec3f center = Vec3f.from(
                            player.x,
                            player.y,
                            player.z
                    );
                    Vec3f outerPoint = Vec3f.from(
                            center.x + radius * Math.cos(angle),
                            center.y,
                            center.z + radius * Math.sin(angle)
                    );

                    double d0 = center.x - outerPoint.x;
                    double d1 = center.y - outerPoint.y;
                    double d2 = center.z - outerPoint.z;
                    Vec3f.from(d0,d1,d2);
                    double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                    viewEntity.yaw = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
                    viewEntity.pitch = (float)-(Math.atan2(d1, d3) * 180.0D / Math.PI);

                    HitResult hitResult = this.level.method_161(center, outerPoint, false);
                    Vec3f position = outerPoint;
                    if (hitResult != null)
                    {
                        if (hitResult.type == HitType.field_789) //field 789 is TILE
                        {
                            position = hitResult.field_1988;
                            // prevent clipping walls
                            position.x += d0/20.f;
                            position.y += d1/20.f;
                            position.z += d2/20.f;
                        }
                    }



                    viewEntity.setPosition(position.x, position.y, position.z);
                    //viewEntity.setPositionAndAngles(viewEntity.x, 128, viewEntity.z, 180, 30);
                    viewEntity.setVelocity(0,0,0);
                }
            }
        }
    }
}
