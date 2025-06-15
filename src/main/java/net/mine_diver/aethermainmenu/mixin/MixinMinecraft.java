package net.mine_diver.aethermainmenu.mixin;

import net.mine_diver.aethermainmenu.AetherMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow public LivingEntity camera;

    @Shadow public ClientPlayerEntity player;

    @Shadow private int ticksPlayed;

    @Shadow public World world;

    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;lockMouse()V", shift = At.Shift.BEFORE))
    private void stopMusic(Screen screen, CallbackInfo ci) {
        if (AetherMenu.musicId != null) {
            AccessorSoundHelper.getSoundSystem().stop(AetherMenu.musicId);
            AetherMenu.musicId = null;
            AetherMenu.needsPlayerCreation = false;
        }
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;tick()V", shift = At.Shift.AFTER))
    private void tick(CallbackInfo ci) {
        // "ticksPlayed" is available to use

        if (AetherMenu.musicId != null)
        {
            if (this.world != null)
            {
                if (AetherMenu.needsPlayerCreation)
                {
                    camera = new PlayerEntity(world) {
                        @Override
                        public void spawn() {
                        }

                        @Override
                        public boolean damage(Entity arg, int i) {
                            return false;
                        }

                        @Override
                        protected void applyDamage(int i) {

                        }
                    };
                    camera.width = 0;
                    camera.height = 0;
                    world.spawnEntity(camera);

                    camera.setPosition(player.x, player.y, player.z);
                    AetherMenu.needsPlayerCreation = false;
                }

                if (camera != null)
                {
                    double radius = 5.0F; // Adjust this as needed
                    double angle = ticksPlayed / 200F; // Adjust this as needed

                    Vec3d center = Vec3d.createCached(
                            player.x,
                            player.y,
                            player.z
                    );
                    Vec3d outerPoint = Vec3d.createCached(
                            center.x + radius * Math.cos(angle),
                            center.y,
                            center.z + radius * Math.sin(angle)
                    );

                    double d0 = center.x - outerPoint.x;
                    double d1 = center.y - outerPoint.y;
                    double d2 = center.z - outerPoint.z;
                    Vec3d.createCached(d0,d1,d2);
                    double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                    camera.yaw = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
                    camera.pitch = (float)-(Math.atan2(d1, d3) * 180.0D / Math.PI);

                    HitResult hitResult = this.world.raycast(center, outerPoint, false);
                    Vec3d position = outerPoint;
                    if (hitResult != null)
                    {
                        if (hitResult.type == HitResultType.BLOCK) //field 789 is TILE
                        {
                            position = hitResult.pos;
                            // prevent clipping walls
                            position.x += d0/10.f;
                            position.y += d1/10.f;
                            position.z += d2/10.f;
                        }
                    }

                    camera.setPosition(position.x, position.y, position.z);
                    camera.setVelocityClient(0,0,0);
                }
            }
        }
    }
}
