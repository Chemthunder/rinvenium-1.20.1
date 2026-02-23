package silly.chemthunder.rinvenium.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class RinveniumUtil {
    /**Equation: -0.0002x^2 + 5*/
    public static double calculateDivergenceDropOff(double input) {
        return MathHelper.clamp(-0.0002 * (input * input) + 5.0, 0.0, 5.0);
    }

    /**Returns it in degrees*/
    public static float pitchFromVecDeg(Vec3d rotVec) {
        return (float) (-Math.toDegrees(Math.atan2(rotVec.y, Math.sqrt(rotVec.x * rotVec.x + rotVec.z * rotVec.z))));
    }
    /**Returns it in radians*/
    public static float pitchFromVecRad(Vec3d rotVec) {
        return (float) (-Math.atan2(rotVec.y, Math.sqrt(rotVec.x * rotVec.x + rotVec.z * rotVec.z)));
    }

    /**Returns it in degrees*/
    public static float yawFromVecDeg(Vec3d rotVec) {
        return (float) (Math.toDegrees(Math.atan2(rotVec.z, rotVec.x)) - 90.0F);
    }
    /**Returns it in radians*/
    public static float yawFromVecRad(Vec3d rotVec) {
        return (float) (Math.atan2(rotVec.z, rotVec.x) - 90.0F);
    }

    public static EntityHitResult raycastWithDivergence(Entity entity, double maxDistance, float tickDelta, float divergence, double radius) {
        Vec3d camPos = entity.getCameraPosVec(tickDelta);
        Vec3d rot = entity.getRotationVec(tickDelta);
        rot = rot.add(
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence)
        );
        Vec3d endPos = camPos.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);

        Box box = new Box(camPos, endPos).expand(radius);
        return ProjectileUtil.raycast(
                entity,
                camPos,
                endPos,
                box,
                target -> !target.isSpectator() && target.canHit(),
                endPos.squaredDistanceTo(camPos)
        );
    }

    public static EntityHitResult raycastWithDivergence(Entity entity, Vec3d start, Vec3d rot, double maxDistance, float tickDelta, float divergence, double radius) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);

        Box box = new Box(start, endPos).expand(radius);
        return ProjectileUtil.raycast(
                entity,
                start,
                endPos,
                box,
                target -> !target.isSpectator() && target.canHit(),
                endPos.squaredDistanceTo(start)
        );
    }

    public static HitResult raycastWithDivergence(Entity entity, double maxDistance, float tickDelta, boolean includeFluids, float divergence) {
        Vec3d camPos = entity.getCameraPosVec(tickDelta);
        Vec3d rot = entity.getRotationVec(tickDelta);
        rot = rot.add(
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence),
                entity.getWorld().random.nextTriangular(0.0, 0.0172275 * divergence)
        );
        Vec3d endPos = camPos.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);
        return entity.getWorld().raycast(
                new RaycastContext(
                        camPos,
                        endPos,
                        RaycastContext.ShapeType.OUTLINE,
                        includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE,
                        entity
                )
        );
    }

    public static HitResult raycastWithDivergence(Entity entity, Vec3d start, Vec3d rot, double maxDistance, float tickDelta, boolean includeFluids, float divergence) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);
        return entity.getWorld().raycast(
                new RaycastContext(
                        start,
                        endPos,
                        RaycastContext.ShapeType.OUTLINE,
                        includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE,
                        entity
                )
        );
    }

    public static void spawnRaycastParticles(ServerWorld world, Vec3d start, Vec3d rot, double maxDistance, double step, double offset, ParticleEffect particle) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);
        Vec3d direction = endPos.subtract(start);
        double length = direction.length();
        Vec3d normal = direction.normalize();
        for (double d = offset; d < length; d += step) {
            Vec3d spawnPos = start.add(normal.multiply(d));
            world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
        }
    }

    public static void spawnRaycastParticles(ServerWorld world, Vec3d start, Vec3d rot, HitResult hitResult, double maxDistance, double step, double offset, ParticleEffect particle) {
        Vec3d endPos = start.add(rot.x * maxDistance, rot.y * maxDistance, rot.z * maxDistance);

        Vec3d direction = endPos.subtract(start);
        double length = direction.length();
        Vec3d normal = direction.normalize();
        if (hitResult != null) {
            if (hitResult.getType() != HitResult.Type.MISS) {
                Vec3d hitPos = hitResult.getPos();
                Vec3d hitDirection = hitPos.subtract(start);
                double hitLength = hitDirection.length();

                for (double d = offset; d < Math.min(length, hitLength); d += step) {
                    Vec3d spawnPos = start.add(normal.multiply(d));
                    world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
                }
            } else {
                for (double d = offset; d < length; d += step) {
                    Vec3d spawnPos = start.add(normal.multiply(d));
                    world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
                }
            }
        } else {
            for (double d = offset; d < length; d += step) {
                Vec3d spawnPos = start.add(normal.multiply(d));
                world.spawnParticles(particle, spawnPos.x, spawnPos.y, spawnPos.z, 1, 0.0, 0.0, 0.0, 0);
            }
        }
    }


    public static ItemStack exchangeWholeStack(ItemStack inputStack, PlayerEntity player, ItemStack outputStack, boolean creativeOverride) {
        boolean bl = player.isCreative();
        int count = inputStack.getCount();
        outputStack.setCount(count);
        if (creativeOverride && bl) {
            player.getInventory().insertStack(outputStack);
        } else {
            if (!player.getInventory().insertStack(outputStack)) {
                player.dropItem(outputStack, false);
                if (!player.isCreative()) {
                    inputStack.decrement(count);
                }
                return inputStack;
            }
            return outputStack;
        }
        return inputStack;
    }
    public static ItemStack exchangeWholeStack(ItemStack inputStack, PlayerEntity player, ItemStack outputStack) {
        return exchangeWholeStack(inputStack, player, outputStack, true);
    }
}
