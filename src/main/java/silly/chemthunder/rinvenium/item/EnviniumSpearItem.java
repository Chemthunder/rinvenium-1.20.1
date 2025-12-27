package silly.chemthunder.rinvenium.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.cca.item.EnviniumSpearItemComponent;

import java.util.UUID;

public class EnviniumSpearItem extends SwordItem {
    public EnviniumSpearItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        EnviniumSpearItemComponent spear = EnviniumSpearItemComponent.KEY.get(stack);

        if (user.getMainHandStack().isOf(this)) {
            float j = 4;
                if (spear.getCharge() > 0) {
                    float f = user.getYaw();
                    float g = user.getPitch();
                    float h = -MathHelper.sin(f * ((float)Math.PI / 180F)) * MathHelper.cos(g * ((float)Math.PI / 180F));
                    float k = -MathHelper.sin(g * ((float)Math.PI / 180F));
                    float l = MathHelper.cos(f * ((float)Math.PI / 180F)) * MathHelper.cos(g * ((float)Math.PI / 180F));
                    float m = MathHelper.sqrt(h * h + k * k + l * l);
                    float n = 3.0F * ((1.0F + j) / 4.0F);
                    h *= n / m;
                    k *= n / m;
                    l *= n / m;
                    user.addVelocity(h, k, l);
                    user.useRiptide(20);
                }
        }


        return super.use(world, user, hand);
    }

    @Override
    public Text getName(ItemStack stack) {
        return super.getName(stack).copy().styled(style -> style.withColor(0xf58442));
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        EnviniumSpearItemComponent spear = EnviniumSpearItemComponent.KEY.get(stack);

        return Math.round((float) spear.getCharge() / 10 * 13);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return EnviniumSpearItemComponent.KEY.get(stack).getCharge() > 0;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x9cfdff;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            if (player.isSneaking() && player.getMainHandStack().isOf(this)) {
                EnviniumSpearItemComponent spear = EnviniumSpearItemComponent.KEY.get(stack);

                if (spear.getCharge() < 11) {
                    spear.setCharge(spear.getCharge() + 1);
                    player.getHungerManager().addExhaustion(1.5f);
                }
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
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
}
