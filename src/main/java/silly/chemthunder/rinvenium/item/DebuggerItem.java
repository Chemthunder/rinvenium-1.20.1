package silly.chemthunder.rinvenium.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.index.RinveniumParticles;
import silly.chemthunder.rinvenium.particle.AgeVaryingParticleEffect;
import silly.chemthunder.rinvenium.particle.SmokeTrailParticle;
import silly.chemthunder.rinvenium.particle.SmokeTrailParticleEffect;
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
                    RinveniumUtil.spawnRaycastSmokeParticles(serverWorld, player.getCameraPosVec(0.0f), player.getRotationVector(), hitResult, 50.0, 0.5, 1.2);
                }
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
