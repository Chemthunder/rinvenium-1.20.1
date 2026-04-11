package silly.chemthunder.rinvenium.item;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import silly.chemthunder.rinvenium.index.RinveniumPackets;
import silly.chemthunder.rinvenium.render.ScreenFlash;
import silly.chemthunder.rinvenium.render.SlashRender;
import silly.chemthunder.rinvenium.render.VertexColorSet;
import silly.chemthunder.rinvenium.render.manager.FlashManager;
import silly.chemthunder.rinvenium.render.manager.global.SlashRendererManager;
import silly.chemthunder.rinvenium.util.RinveniumUtil;

public class DebuggerItem extends Item {
    public DebuggerItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!player.isSneaking()) {
            if (world.isClient) { // Client Side Standing
                MinecraftClient client = MinecraftClient.getInstance();

            } else { // Server Side Standing
                if (world instanceof ServerWorld serverWorld) {
                    HitResult hitResult = player.raycast(50.0, 0.0f, false);
                    RinveniumUtil.spawnRaycastSmokeParticles(serverWorld, player.getCameraPosVec(0.0f), player.getRotationVector(), hitResult, 256.0, 0.5, 1.1, 40);
                    RinveniumUtil.spawnRaycastRailgunParticles(serverWorld, player.getCameraPosVec(0.0f), player.getRotationVector(), hitResult, 256.0, 0.5, 2.3, 30);
                }
                if (player instanceof ServerPlayerEntity serverPlayerEntity) {
                    PacketByteBuf buf = PacketByteBufs.create();
                    buf.writeInt(10);
                    buf.writeInt(0xFFFFFF);
                    buf.writeInt(5);
                    buf.writeFloat(0.8f);
                    //ServerPlayNetworking.send(serverPlayerEntity, RinveniumPackets.ADD_SCREEN_FLASH, buf);
                }
                Vec3d origin = player.getEyePos().add(player.getRotationVector().normalize().multiply(1.5));
                float pitch = world.random.nextFloat() * 360.0F;
                float yaw = world.random.nextFloat() * 360.0F;
                float roll = world.random.nextFloat() * 360.0F;
                SlashRender slashRender = new SlashRender(
                        origin,
                        100,
                        new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                        new VertexColorSet(0.4f, 0.0f, 0.0f, 0.9f),
                        new VertexColorSet(1.0f, 0.0f, 0.0f, 0.9f),
                        new VertexColorSet(1.0f, 0.9f, 0.9f, 1.0f)
                );
                slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(90));
                slashRender.addTransformation(RotationAxis.NEGATIVE_X.rotationDegrees(90));
                slashRender.addTransformation(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
                slashRender.addTransformation(RotationAxis.POSITIVE_Z.rotationDegrees(roll));
                slashRender.addTransformation(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
                SlashRendererManager.add(slashRender);
            }
        } else {
            if (world.isClient) { // Client Side Sneak

            } else { // Server Side Sneak
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 20, 10));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 20));
            }

        }
        return TypedActionResult.success(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}