package silly.chemthunder.rinvenium.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.entity.AscentaShotEntity;
import silly.chemthunder.rinvenium.index.RinveniumEntities;

public class AscentaItem extends Item {
    public AscentaItem(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        float f = 2.0f;
        if (world instanceof ServerWorld serverWorld) {
            for (int i = 1; i <= 15; i++) {
                AscentaShotEntity shot = new AscentaShotEntity(RinveniumEntities.ASCENTA_SHOT, serverWorld);
                shot.updatePosition(user.getX(), user.getY() + 1.0f, user.getZ());
                shot.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, f * 3.0f, 5.0f);

                serverWorld.spawnEntity(shot);
            }

            user.playSound(SoundEvents.ITEM_TRIDENT_THUNDER, SoundCategory.MASTER,1, 1);
            user.playSound(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.MASTER, 1, 1);

            user.setVelocity(user.getRotationVec(0).multiply(-1));
            user.addVelocity(0, 0.5, 0);
            user.velocityModified = true;
        }
        return super.use(world, user, hand);
    }
}
