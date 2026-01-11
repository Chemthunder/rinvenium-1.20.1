package silly.chemthunder.rinvenium.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.joml.Vector3f;
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;
import silly.chemthunder.rinvenium.index.RinveniumItems;

public class GunshotEntity extends ThrownItemEntity {

    public GunshotEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected Item getDefaultItem() {
        return RinveniumItems.HAIL_OF_THE_GODS;
    }

    @Override
    public void tick() {
        if (getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    10,
                    0.09,
                    0.09,
                    0.09,
                    0.05
            );

            serverWorld.spawnParticles(
                    ParticleTypes.SOUL,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    15,
                    0.03,
                    0.03,
                    0.03,
                    0.07
            );


            serverWorld.spawnParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    1,
                    0,
                    0,
                    0,
                    0.06
            );
        }

        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();

        target.damage(RinveniumDamageSources.boop(target), 8.0f);
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockCollision(BlockState state) {
        if (!state.isOf(Blocks.AIR)) {
            this.discard();
        }
        super.onBlockCollision(state);
    }
}
