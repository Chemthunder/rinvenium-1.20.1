package silly.chemthunder.rinvenium.item;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;

import java.util.List;

public class DebuggerItem extends Item {
    public DebuggerItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!player.isSneaking()) {
            if (world.isClient) { // if (!world.isClient) instead? idfk     nah i dont really care. Its all the same
                MinecraftClient client = MinecraftClient.getInstance();
                int k = DyeColor.YELLOW.getFireworkColor();
                float f = ((k & 0xFF0000) >> 16) / 255.0F;
                float g = ((k & 0xFF00) >> 8) / 255.0F;
                float h = ((k & 0xFF) >> 0) / 255.0F;
                Particle particle = client.particleManager.createParticle(ParticleTypes.FLASH, player.getX(), player.getY(), player.getZ(), 0.0, 0.0, 0.0);
                particle.setColor(f, g, h);
                particle.scale(0.01f);
                client.particleManager.addParticle(particle);
            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 20, 10));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SATURATION, 20, 20));
            }
        } else {
            if (world.isClient) {

            } else {
                player.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 10, 0));
            }

        }
        return TypedActionResult.success(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
