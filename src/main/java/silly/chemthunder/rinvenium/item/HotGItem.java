package silly.chemthunder.rinvenium.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;
import silly.chemthunder.rinvenium.cca.entity.HailOfTheGodComponent;
import silly.chemthunder.rinvenium.entity.GunshotEntity;
import silly.chemthunder.rinvenium.index.RinveniumEntities;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.util.RinveniumUtil;

public class HotGItem extends Item {
    public static final int BULLET_PER_SEC = 10;
    private int shootCooldown = 0;

    public HotGItem(Settings settings) {
        super(settings);
    }

    public Text getName(ItemStack stack) {
        return super.getName(stack).copy().styled(style -> style.withColor(0xf58442));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 100;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!user.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            HailOfTheGodComponent hailOfTheGodComponent = HailOfTheGodComponent.get(player);
            if (player.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
                player.stopUsingItem();
            }
            Vec3d clientRot = null;
            if (world.isClient) {
                clientRot = user.getRotationVecClient();
            }
            if (!world.isClient && !player.getItemCooldownManager().isCoolingDown(RinveniumItems.HAIL_OF_THE_GODS)) {
                for (int i = 0; i < 4; i++) {
                    GunshotEntity bullet = new GunshotEntity(world, user);
                    if (bullet.getOwner() == null) {
                        bullet.setOwner(user);
                    } else if (!bullet.getOwner().equals(user)) {
                        bullet.setOwner(user);
                    }
                    Vec3d spawnPos = user.getPos();
                    bullet.updatePosition(spawnPos.getX(), spawnPos.getY() + 0.5f, spawnPos.getZ());
                    float pitch;
                    float yaw;
                    if (clientRot != null) {
                        pitch = RinveniumUtil.pitchFromVecDeg(clientRot);
                        yaw = RinveniumUtil.yawFromVecDeg(clientRot);
                    } else {
                        pitch = user.getPitch();
                        yaw = user.getYaw();
                    }
                    bullet.updateRotation();
                    bullet.setVelocity(user, pitch, yaw, 0.0f, 3.2f * 3.0f, (float) RinveniumUtil.calculateDivergenceDropOff(hailOfTheGodComponent.getDoubleIntValue1()) * 2.0f + 0.1f);
                    if (hailOfTheGodComponent.getDoubleIntValue2() > 0) {
                        if (hailOfTheGodComponent.getDoubleIntValue2() % 2 == 0) {
                            world.spawnEntity(bullet);
                            break;
                        }
                    } else {
                        world.spawnEntity(bullet);
                    }

                }
            }
            if (!world.isClient) {
                if (hailOfTheGodComponent.getDoubleIntValue1() < HailOfTheGodComponent.MAX_USE_TIME && hailOfTheGodComponent.getDoubleIntValue2() <= 0) {
                    hailOfTheGodComponent.addToDoubleIntValue1(3);
                } else {
                    hailOfTheGodComponent.addToDoubleIntValue2(5);
                }
            }
            player.playSound(SoundEvents.ENTITY_BEE_LOOP_AGGRESSIVE, 1.0f, 0.4f);
        }
        if (user.isUsingItem() && user.getActiveItem().isOf(RinveniumItems.HAIL_OF_THE_GODS) && user.getItemUseTime() >= this.getMaxUseTime(stack)) {
            user.stopUsingItem();
            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(RinveniumItems.HAIL_OF_THE_GODS, 100);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            HailOfTheGodComponent hailOfTheGodComponent = HailOfTheGodComponent.get(player);
            player.sendMessage(Text.literal("use time: " + hailOfTheGodComponent.getDoubleIntValue1() + "   overheat: " + hailOfTheGodComponent.getDoubleIntValue2()), true);
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x9cfdff;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return 0;
    }
}
