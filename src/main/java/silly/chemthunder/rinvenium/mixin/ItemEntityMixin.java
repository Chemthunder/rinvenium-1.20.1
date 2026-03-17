package silly.chemthunder.rinvenium.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import silly.chemthunder.rinvenium.datagen.RinveniumBlockTagProvider;
import silly.chemthunder.rinvenium.index.RinveniumItems;

import java.util.List;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public int aurioCraftingTicks = 0;
    public int enviniaCraftingTicks = 0;
    public int envixiusCraftingTicks = 0;
    public int envixiusColdCraftingTicks = 0;

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    public abstract void setStack(ItemStack stack);

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/ItemEntity;velocityDirty:Z", ordinal = 0))
    private void rinvenium$itemCrafting(CallbackInfo ci) {
        if (!this.getWorld().isClient) {
            craftAurio();
            craftEnvinia();
            craftEnvixius();
            craftColdEnvixius();

            if (this.getStack().isOf(RinveniumItems.SUPERHEATED_ENVIXIUS_INGOT) && this.getWorld().getBlockState(this.getBlockPos()).isIn(BlockTags.ANVIL)) {
                int count = this.getStack().getCount();
                this.setStack(new ItemStack(RinveniumItems.SUPERHEATED_ENVIXIUS_PLATE, count));
            }
        }
    }

    private void craftAurio() {
        if (this.getStack().isOf(Items.GOLD_INGOT) && this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.CAULDRON) && this.getWorld().getBlockState(this.getBlockPos().offset(Direction.DOWN)).isIn(RinveniumBlockTagProvider.SOUL_FIRE)) {
            List<ItemEntity> itemEntities = this.getWorld().getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(0.5), itemEntity -> !itemEntity.isRemoved() && itemEntity.getItemAge() < 5800 && !itemEntity.getStack().isOf(Items.GOLD_INGOT));
            DefaultedList<ItemEntity> ingredients = DefaultedList.ofSize(2);
            for (ItemEntity entity : itemEntities) {
                if (entity.getStack().isOf(Items.GOLD_INGOT)) {
                    itemEntities.remove(entity);
                    continue;
                }
                if (entity.getStack().isOf(Items.COPPER_INGOT)) {
                    ingredients.add(entity);
                    ingredients.add((ItemEntity) ((Object) (this)));
                }
            }
            if (!ingredients.isEmpty()) {
                this.aurioCraftingTicks++;
                ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.DRIPPING_LAVA, this.getX(), this.getY() + 0.5, this.getZ(), 4, 0.1, 0.1, 0.1, 1.0);
            } else {
                this.aurioCraftingTicks = 0;
            }
            if (this.aurioCraftingTicks >= 200) {
                int mainCount = 0;
                int otherCount = 0;
                for (ItemEntity entity : ingredients) {
                    if (entity.getStack().isOf(Items.GOLD_INGOT)) {
                        mainCount = entity.getStack().getCount();
                    } else {
                        otherCount = entity.getStack().getCount();
                    }
                }
                int count = Math.min(mainCount, otherCount);
                for (ItemEntity entity : ingredients) {
                    if (entity.getStack().isOf(Items.GOLD_INGOT)) {
                        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), new ItemStack(RinveniumItems.SUPERHEATED_AURIO_INGOT, count));
                        this.getWorld().spawnEntity(itemEntity);
                        entity.setStack(entity.getStack().copyWithCount(mainCount - count));
                    } else {
                        entity.setStack(entity.getStack().copyWithCount(otherCount - count));
                    }
                }
                this.aurioCraftingTicks = 0;
            }
        }
    }

    private void craftEnvinia() {
        if (this.getStack().isOf(Items.NETHERITE_SCRAP) && this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.CAULDRON) && this.getWorld().getBlockState(this.getBlockPos().offset(Direction.DOWN)).isIn(RinveniumBlockTagProvider.SOUL_FIRE)) {
            List<ItemEntity> itemEntities = this.getWorld().getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(0.5), itemEntity -> !itemEntity.isRemoved() && itemEntity.getItemAge() < 5800 && !itemEntity.getStack().isOf(Items.NETHERITE_SCRAP));
            DefaultedList<ItemEntity> ingredients = DefaultedList.ofSize(2);
            for (ItemEntity entity : itemEntities) {
                if (entity.getStack().isOf(Items.NETHERITE_SCRAP)) {
                    itemEntities.remove(entity);
                    continue;
                }
                if (entity.getStack().isOf(Items.IRON_INGOT)) {
                    ingredients.add(entity);
                    ingredients.add((ItemEntity) ((Object) (this)));
                }
            }
            if (!ingredients.isEmpty()) {
                this.enviniaCraftingTicks++;
                ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.DRIPPING_LAVA, this.getX(), this.getY() + 0.5, this.getZ(), 4, 0.1, 0.1, 0.1, 1.0);
            } else {
                this.enviniaCraftingTicks = 0;
            }
            if (this.enviniaCraftingTicks >= 200) {
                int mainCount = 0;
                int otherCount = 0;
                for (ItemEntity entity : ingredients) {
                    if (entity.getStack().isOf(Items.NETHERITE_SCRAP)) {
                        mainCount = entity.getStack().getCount();
                    } else {
                        otherCount = entity.getStack().getCount();
                    }
                }
                int count = Math.min(mainCount, otherCount);
                for (ItemEntity entity : ingredients) {
                    if (entity.getStack().isOf(Items.NETHERITE_SCRAP)) {
                        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), new ItemStack(RinveniumItems.SUPERHEATED_ENVINIA_INGOT, count));
                        this.getWorld().spawnEntity(itemEntity);
                        entity.setStack(entity.getStack().copyWithCount(mainCount - count));
                    } else {
                        entity.setStack(entity.getStack().copyWithCount(otherCount - count));
                    }
                }
                this.enviniaCraftingTicks = 0;
            }
        }
    }

    private void craftEnvixius() {
        if (this.getStack().isOf(RinveniumItems.SUPERHEATED_ENVINIA_INGOT) && this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.CAULDRON) && this.getWorld().getBlockState(this.getBlockPos().offset(Direction.DOWN)).isIn(RinveniumBlockTagProvider.SOUL_FIRE)) {
            List<ItemEntity> itemEntities = this.getWorld().getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(0.5), itemEntity -> !itemEntity.isRemoved() && itemEntity.getItemAge() < 5800 && !itemEntity.getStack().isOf(RinveniumItems.SUPERHEATED_ENVINIA_INGOT));
            DefaultedList<ItemEntity> ingredients = DefaultedList.ofSize(2);
            for (ItemEntity entity : itemEntities) {
                if (entity.getStack().isOf(RinveniumItems.SUPERHEATED_ENVINIA_INGOT)) {
                    itemEntities.remove(entity);
                    continue;
                }
                if (entity.getStack().isOf(RinveniumItems.SUPERHEATED_AURIO_INGOT)) {
                    ingredients.add(entity);
                    ingredients.add((ItemEntity) ((Object) (this)));
                }
            }
            if (!ingredients.isEmpty()) {
                this.envixiusCraftingTicks++;
                ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.DRIPPING_LAVA, this.getX(), this.getY() + 0.5, this.getZ(), 4, 0.1, 0.1, 0.1, 1.0);
            } else {
                this.envixiusCraftingTicks = 0;
            }
            if (this.envixiusCraftingTicks >= 200) {
                int mainCount = 0;
                int otherCount = 0;
                for (ItemEntity entity : ingredients) {
                    if (entity.getStack().isOf(RinveniumItems.SUPERHEATED_ENVINIA_INGOT)) {
                        mainCount = entity.getStack().getCount();
                    } else {
                        otherCount = entity.getStack().getCount();
                    }
                }
                int count = Math.min(mainCount, otherCount);
                for (ItemEntity entity : ingredients) {
                    if (entity.getStack().isOf(RinveniumItems.SUPERHEATED_ENVINIA_INGOT)) {
                        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), new ItemStack(RinveniumItems.SUPERHEATED_ENVIXIUS_INGOT, count));
                        this.getWorld().spawnEntity(itemEntity);
                        entity.setStack(entity.getStack().copyWithCount(mainCount - count));
                    } else {
                        entity.setStack(entity.getStack().copyWithCount(otherCount - count));
                    }
                }
                this.envixiusCraftingTicks = 0;
            }
        }
    }

    private void craftColdEnvixius() {
        if (this.getStack().isOf(RinveniumItems.ENVINIA_INGOT) && this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.CAULDRON) && this.getWorld().getBlockState(this.getBlockPos().offset(Direction.DOWN)).isIn(RinveniumBlockTagProvider.SOUL_FIRE)) {
            List<ItemEntity> itemEntities = this.getWorld().getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(0.5), itemEntity -> !itemEntity.isRemoved() && itemEntity.getItemAge() < 5800 && !itemEntity.getStack().isOf(RinveniumItems.ENVINIA_INGOT));
            DefaultedList<ItemEntity> ingredients = DefaultedList.ofSize(2);
            for (ItemEntity entity : itemEntities) {
                if (entity.getStack().isOf(RinveniumItems.ENVINIA_INGOT)) {
                    itemEntities.remove(entity);
                    continue;
                }
                if (entity.getStack().isOf(RinveniumItems.AURIO_INGOT)) {
                    ingredients.add(entity);
                    ingredients.add((ItemEntity) ((Object) (this)));
                }
            }
            if (!ingredients.isEmpty()) {
                this.envixiusColdCraftingTicks++;
                ((ServerWorld) this.getWorld()).spawnParticles(ParticleTypes.DRIPPING_LAVA, this.getX(), this.getY() + 0.5, this.getZ(), 4, 0.1, 0.1, 0.1, 1.0);
            } else {
                this.envixiusColdCraftingTicks = 0;
            }
            if (this.envixiusColdCraftingTicks >= 400) {
                int mainCount = 0;
                int otherCount = 0;
                for (ItemEntity entity : ingredients) {
                    if (entity.getStack().isOf(RinveniumItems.ENVINIA_INGOT)) {
                        mainCount = entity.getStack().getCount();
                    } else {
                        otherCount = entity.getStack().getCount();
                    }
                }
                int count = Math.min(mainCount, otherCount);
                for (ItemEntity entity : ingredients) {
                    if (entity.getStack().isOf(RinveniumItems.ENVINIA_INGOT)) {
                        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getX(), this.getY(), this.getZ(), new ItemStack(RinveniumItems.SUPERHEATED_ENVIXIUS_INGOT, count));
                        this.getWorld().spawnEntity(itemEntity);
                        entity.setStack(entity.getStack().copyWithCount(mainCount - count));
                    } else {
                        entity.setStack(entity.getStack().copyWithCount(otherCount - count));
                    }
                }
                this.envixiusColdCraftingTicks = 0;
            }
        }
    }
}
