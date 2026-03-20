package silly.chemthunder.rinvenium.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import silly.chemthunder.rinvenium.cca.entity.EnvixiaFormComponent;
import silly.chemthunder.rinvenium.util.inject.HungerDecrement;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin implements HungerDecrement {
    @Shadow
    public abstract void add(int food, float saturationModifier);

    @Shadow
    private int foodLevel;

    @Shadow
    private float saturationLevel;

    @Override
    public void rinvenium$eatSubtract(Item item, ItemStack stack) {
        if (item.isFood()) {
            FoodComponent foodComponent = item.getFoodComponent();
            this.rinvenium$subtract(foodComponent.getHunger(), foodComponent.getSaturationModifier());
        }
    }

    @Override
    public void rinvenium$subtract(int food, float saturationModifier) {
        this.foodLevel = Math.min((int) -Math.floor((double) food / 2) + this.foodLevel, 20);
        this.saturationLevel = MathHelper.clamp(this.saturationLevel + food * saturationModifier * 2.0F, 0, 20.0f);
    }

    @ModifyExpressionValue(method = "update", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/HungerManager;foodLevel:I", ordinal = 3))
    private int rinvenium$envixiaAlwaysHeals(int foodLevel, @Local(argsOnly = true) PlayerEntity player) {
        EnvixiaFormComponent envixiaFormComponent = EnvixiaFormComponent.get(player);
        if (envixiaFormComponent.getTripleBoolValue1()) {
            return 20;
        }
        return foodLevel;
    }
}
