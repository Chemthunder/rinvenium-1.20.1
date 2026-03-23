package silly.chemthunder.rinvenium.item;

import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.index.RinveniumItems;

import java.util.HashMap;
import java.util.Map;

public class BatteryItem extends Item {
    public boolean shouldCraft = false;
    public int craftTime = 0;

    public BatteryItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient) {
            if (entity instanceof PlayerEntity player) {
                ItemStack itemStack = player.getStackInHand(Hand.MAIN_HAND);
                if (itemStack.isOf(RinveniumItems.BATTERY)) {
                    Map<BlockState, BlockPos> AREA = new HashMap<>();
                    for (int i = player.getBlockPos().getX() - 2; i < player.getBlockPos().getX() + 2; i++) {
                        for (int j = player.getBlockPos().getZ() - 2; j < player.getBlockPos().getZ() + 2; j++) {
                            for (int k = player.getBlockPos().getY() - 2; k < player.getBlockPos().getY() + 2; k++) {
                                BlockPos pos = new BlockPos(i, j, k);
                                AREA.put(world.getBlockState(pos), pos);
                            }
                        }
                    }
                    AREA.forEach((blockState, pos) -> {
                        if (blockState.isOf(Blocks.BEACON)) {
                            BeaconBlock beaconBlock = (BeaconBlock) blockState.getBlock();
                            BeaconBlockEntity beaconBlockEntity = (BeaconBlockEntity) world.getBlockEntity(pos);
                            this.shouldCraft = beaconBlockEntity != null && beaconBlockEntity.level > 0;
                        } else {
                            this.shouldCraft = false;
                        }
                    });
                }

                if (this.shouldCraft) {
                    craftTime += 2;
                    if (world instanceof ServerWorld serverWorld) {
                        serverWorld.spawnParticles(
                                ParticleTypes.END_ROD,
                                player.getHandPosOffset(this).x,
                                player.getHandPosOffset(this).y,
                                player.getHandPosOffset(this).z,
                                10,
                                0.001,
                                0.001,
                                0.001,
                                0.1
                        );
                    }
                } else {
                    craftTime--;
                }

                if (craftTime >= 80 * (int) Math.floor((double) itemStack.getCount() / 2)) {
                    ItemUsage.exchangeStack(stack, player, new ItemStack(RinveniumItems.ION_CELL).copyWithCount(itemStack.getCount()));
                }
            }
        }
    }
}