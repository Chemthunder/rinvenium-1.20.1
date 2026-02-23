package silly.chemthunder.rinvenium.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

    @Shadow
    public abstract ItemStack getStack();

    @Shadow
    public abstract void setStack(ItemStack stack);

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/ItemEntity;velocityDirty:Z", ordinal = 0))
    private void rinvenium$itemCrafting(CallbackInfo ci) {
        if (this.getStack().isOf(Items.GOLD_INGOT) && this.getWorld().getBlockState(this.getBlockPos()).isOf(Blocks.CAULDRON) && this.getWorld().getBlockState(this.getBlockPos().offset(Direction.DOWN)).isIn(RinveniumBlockTagProvider.SOUL_FIRE)) {
            List<ItemEntity> itemEntities = this.getWorld().getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(0.5), itemEntity -> !itemEntity.isRemoved() && itemEntity.getItemAge() < 5800 && !itemEntity.getStack().isOf(Items.GOLD_INGOT));
            DefaultedList<ItemEntity> ingredients = DefaultedList.ofSize(2);
            for (ItemEntity entity : itemEntities) {
                if (entity.getStack().isOf(Items.GOLD_INGOT)) {
                    itemEntities.remove(entity);
                    continue;
                }/*
                if (entity.getStack().isOf()) {

                }*/
            }

            this.aurioCraftingTicks++;
            if (this.aurioCraftingTicks >= 200) {
                int count = this.getStack().getCount();
                this.setStack(new ItemStack(RinveniumItems.AURIO_INGOT));
            }
        }
    }
}
