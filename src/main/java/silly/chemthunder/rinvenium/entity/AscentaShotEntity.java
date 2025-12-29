package silly.chemthunder.rinvenium.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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

public class AscentaShotEntity extends ThrownItemEntity {
    public AscentaShotEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }


    @Override
    public void tick() {

        if (getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(
                    ParticleTypes.END_ROD,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    5,
                    0.07,
                    0.07,
                    0.07,
                    0.1
            );

            serverWorld.spawnParticles(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    15,
                    0.14,
                    0.14,
                    0.14,
                    0.1
            );

            serverWorld.spawnParticles(
                    ParticleTypes.SONIC_BOOM,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    15,
                    0.14,
                    0.14,
                    0.14,
                    0.1
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
                    0
            );

            var dust = new DustParticleEffect(new Vector3f((float) 252 / 256, (float) 3 / 256, (float) 78 / 256), 1.0f);
            serverWorld.addParticle(dust, this.getX(), this.getY(), this.getZ(), 1, 1, 1);
        }

        super.tick();
    }

    @Override
    protected Item getDefaultItem() {
        return RinveniumItems.ASCENTA_DE_RIVULETA;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }


    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();

        target.damage(RinveniumDamageSources.bap(target), 8.0f);


        super.onEntityHit(entityHitResult);
    }
}
