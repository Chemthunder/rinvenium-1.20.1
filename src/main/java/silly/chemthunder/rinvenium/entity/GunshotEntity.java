package silly.chemthunder.rinvenium.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.index.RinveniumDamageSources;
import silly.chemthunder.rinvenium.index.RinveniumEntities;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.network.s2c.SpawnFlashParticleS2CPacket;

public class GunshotEntity extends ThrownItemEntity {
    public GunshotEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }
    public GunshotEntity(World world, LivingEntity owner) {
        super(RinveniumEntities.GUNSHOT, owner, world);
    }

    protected Item getDefaultItem() {
        return RinveniumItems.EMPTY;
    }

    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof PlayerEntity && !entity.isAlive()) {
            this.discard();
        } else {
            super.tick();
        }
        if (getWorld() instanceof ServerWorld serverWorld && this.age > 1) {
            serverWorld.spawnParticles(
                    ParticleTypes.WAX_ON,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    5,
                    0.0,
                    0.0,
                    0.0,
                    0.006
            );

            serverWorld.spawnParticles(
                    ParticleTypes.SMOKE,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    5,
                    0.0,
                    0.0,
                    0.0,
                    0.0
            );

            serverWorld.spawnParticles(
                    ParticleTypes.WAX_ON,
                    this.getX(),
                    this.getY() + 1.0f,
                    this.getZ(),
                    5,
                    0.0,
                    0.0,
                    0.0,
                    0.006
            );
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity target = entityHitResult.getEntity();
        if (target.equals(this.getOwner())) {
            return;
        }
        if (target.damage(RinveniumDamageSources.boop(target), 1.5f)) {
            if (target.getWorld().isClient) {
                ClientWorld clientWorld = (ClientWorld) target.getWorld();
                clientWorld.getPlayers().forEach(player -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    int k = DyeColor.YELLOW.getFireworkColor();
                    float f = ((k & 0xFF0000) >> 16) / 255.0F;
                    float g = ((k & 0xFF00) >> 8) / 255.0F;
                    float h = ((k & 0xFF) >> 0) / 255.0F;
                    Particle particle = client.particleManager.createParticle(ParticleTypes.FLASH, target.getX(), target.getY(), target.getZ(), 0.0, 0.0, 0.0);
                    particle.setColor(f, g, h);
                    particle.scale(0.01f);
                    client.particleManager.addParticle(particle);
                });
            } else {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) this.getOwner();
                if (serverPlayerEntity != null) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeDouble(target.getX());
                    buf.writeDouble(target.getY());
                    buf.writeDouble(target.getZ());
                    ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.FLASH_PARTICLE, buf);
                }
            }
        }
        super.onEntityHit(entityHitResult);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onBlockCollision(BlockState state) {
        if (state.isSolid()) {
            this.discard();
        }
        super.onBlockCollision(state);
    }
}
