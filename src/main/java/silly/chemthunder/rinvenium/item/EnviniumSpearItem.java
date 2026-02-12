package silly.chemthunder.rinvenium.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import silly.chemthunder.rinvenium.cca.entity.SpearDashingComponent;
import silly.chemthunder.rinvenium.cca.entity.SpearParryComponent;
import silly.chemthunder.rinvenium.cca.item.EnviniumSpearItemComponent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;

import java.util.List;
import java.util.UUID;

public class EnviniumSpearItem extends SwordItem {
    public EnviniumSpearItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        EnviniumSpearItemComponent spear = EnviniumSpearItemComponent.KEY.get(stack);
        boolean hasRush = EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0;
        SpearParryComponent spearParryComponent = SpearParryComponent.get(user);
        SpearDashingComponent spearDashingComponent = SpearDashingComponent.get(user);

        if (hand == Hand.OFF_HAND) {
            return TypedActionResult.fail(stack);
        } else {
            user.setCurrentHand(hand);
            if (hasRush && !user.isSneaking()) {
                if (spearDashingComponent.getInt() >= 55) {
                    rush(user, stack);
                    return TypedActionResult.consume(stack);
                } else {
                    return TypedActionResult.fail(stack);
                }
            } else {
                if (user.getItemCooldownManager().isCoolingDown(this)) {
                    user.stopUsingItem();
                    return TypedActionResult.fail(stack);
                } else {
                    if (spearParryComponent.getDoubleBoolValue1()) {
                        user.setCurrentHand(hand);
                        spearParryComponent.setDoubleBoolValue2(true);
                        return TypedActionResult.consume(stack);
                    } else {
                        return TypedActionResult.fail(stack);
                    }
                }
            }
        }
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            SpearParryComponent.get(player).setDoubleBoolValue2(false);
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    public void rush(PlayerEntity user, ItemStack stack) {
        EnviniumSpearItemComponent spear = EnviniumSpearItemComponent.KEY.get(stack);
        float j = EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack);
        if (spear.getCharge() > 0) {
            float f = user.getYaw();
            float g = user.getPitch();
            float h = -MathHelper.sin(f * ((float) Math.PI / 180F)) * MathHelper.cos(g * ((float) Math.PI / 180F));
            float k = -MathHelper.sin(g * ((float) Math.PI / 180F));
            float l = MathHelper.cos(f * ((float) Math.PI / 180F)) * MathHelper.cos(g * ((float) Math.PI / 180F));
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            float n = 3.0F * ((1.0F + j) / 4.0F);
            h *= n / m;
            k *= n / m;
            l *= n / m;
            user.addVelocity(h, k, l);
            user.useRiptide(20);
            SpearDashingComponent dashingComponent = SpearDashingComponent.KEY.get(user);

            dashingComponent.dashTicks = 20;
            dashingComponent.sync();

            if (!user.isCreative()) {
                /*if (!spear.getFinalRush() && spear.getCharge() == 1) {
                    spear.setFinalRush(true);
                }
                spear.setCharge(spear.getCharge() - 1);*/
                dashingComponent.addValueToInt(-55);
                user.getItemCooldownManager().set(this, 5);
            }
        }
    }

    public Text getName(ItemStack stack) {
        return super.getName(stack).copy().styled(style -> style.withColor(0xf58442));
    }

    public int getItemBarStep(ItemStack stack) {
        return 0;
        /*EnviniumSpearItemComponent spear = EnviniumSpearItemComponent.KEY.get(stack);

        if (EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0) {
            return Math.round((float) spear.getCharge() / 4 * 13);
        } else {
            if (spear.isInDamageState()) {
                return (int) Math.ceil((float) spear.getDamageWindow() / SpearParryComponent.MAX_DAMAGE_WINDOW * 13);
            } else {
                return (int) Math.ceil((float) spear.getParryWindow() / SpearParryComponent.MAX_PARRY_WINDOW * 13);
            }
        }*/
    }

    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    public int getItemBarColor(ItemStack stack) {
        if (EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0) {
            return 0x9cfdff; // blue for rush
        }
        return 0x7a1c8c; // purple for max parries
    }

    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            if (EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0) {
                if (player.isSneaking() && player.getMainHandStack().isOf(this) && player.isUsingItem() && player.getActiveItem().isOf(this)) {
                    int j = EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack);
                    EnviniumSpearItemComponent spear = EnviniumSpearItemComponent.KEY.get(stack);

                    if (spear.getCharge() < 4) {
                        spear.setFinalRush(false);
                        spear.setCharge(spear.getCharge() + 1);
                        player.getHungerManager().addExhaustion(3.0f * j);
                    }
                }
            } else {
                if (!player.getWorld().isClient) {
                    SpearParryComponent spearParryComponent = SpearParryComponent.get(player);
                    if (spearParryComponent.getDoubleIntValue1() > 0) {
                        spearParryComponent.decrementDoubleIntValue1();
                    }
                    if (spearParryComponent.getDoubleIntValue1() <= 0) {
                        player.stopUsingItem();
                        player.getItemCooldownManager().set(this, 10);
                    }
                }
            }
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot));

            builder.put(ReachEntityAttributes.ATTACK_RANGE,
                    new EntityAttributeModifier(
                            UUID.fromString("a67e3cc0-45d5-4e8e-9d64-7421e1b5fe3e"),
                            "Additional range",
                            1.5,
                            EntityAttributeModifier.Operation.ADDITION
                    )
            );
            return builder.build();
        }
        return super.getAttributeModifiers(slot);
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (EnchantmentHelper.getLevel(RinveniumEnchantments.RUSH, stack) > 0) {
            tooltip.add(Text.translatable("desc.spear.enchanted_1").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
            tooltip.add(Text.translatable("desc.spear.enchanted_2").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
        } else {
            tooltip.add(Text.translatable("desc.spear.unenchanted_1").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
            tooltip.add(Text.translatable("desc.spear.unenchanted_2").formatted(Formatting.DARK_GRAY).formatted(Formatting.ITALIC));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
