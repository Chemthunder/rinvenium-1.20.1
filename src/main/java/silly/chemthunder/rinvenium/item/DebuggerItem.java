package silly.chemthunder.rinvenium.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;

public class DebuggerItem extends Item {
    public DebuggerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (player.isSneaking()) {
            if (world.isClient) {

            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 20, 10));
            }
        } else {
            if (world.isClient) {

            } else {
                player.addStatusEffect(new StatusEffectInstance(RinveniumStatusEffects.SPARKED, 30, 0));
            }

        }
        return TypedActionResult.success(stack);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
