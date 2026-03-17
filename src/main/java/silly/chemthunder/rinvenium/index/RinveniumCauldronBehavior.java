package silly.chemthunder.rinvenium.index;

import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.util.RinveniumUtil;

import java.util.Map;

public interface RinveniumCauldronBehavior {
    public static final Map<Item, Item> QUENCHING_RECIPE = Map.of(
            RinveniumItems.SUPERHEATED_AURIO_INGOT, RinveniumItems.AURIO_INGOT,
            RinveniumItems.SUPERHEATED_ENVINIA_INGOT, RinveniumItems.ENVINIA_INGOT,
            RinveniumItems.SUPERHEATED_ENVIXIUS_INGOT, RinveniumItems.ENVIXIUS_INGOT,
            RinveniumItems.SUPERHEATED_ENVIXIUS_PLATE, RinveniumItems.ENVIXIUS_PLATE
    );

    Map<Item, CauldronBehavior> AURIO_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    Map<Item, CauldronBehavior> ENVINIA_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();

    public static void registerCauldronBehaviors() {
        addQuenching();
        Rinvenium.LOGGER.info("Registering Rinvenium Cauldron Behaviors");
    }

    private static void addQuenching() {
        Map<Item, CauldronBehavior> map = CauldronBehavior.WATER_CAULDRON_BEHAVIOR;
        RinveniumCauldronBehavior.QUENCHING_RECIPE.forEach((inputItem, outputItem) -> {
            map.put(
                    inputItem,
                    (CauldronBehavior) (state, world, pos, player, hand, stack) -> {
                        if (!world.isClient) {
                            Item item = stack.getItem();
                            player.setStackInHand(hand, RinveniumUtil.exchangeWholeStack(stack, player, new ItemStack(outputItem)));
                            player.incrementStat(Stats.USE_CAULDRON);
                            player.incrementStat(Stats.USED.getOrCreateStat(item));
                            emptyCauldron(world, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH);
                        }
                        return ActionResult.success(world.isClient);
                    }
            );
        });
    }

    private static void emptyCauldron(World world, BlockPos pos, SoundEvent soundEvent) {
        world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
        world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0f, 1.0f);
    }
}
