package silly.chemthunder.rinvenium.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import silly.chemthunder.rinvenium.entity.GunshotEntity;
import silly.chemthunder.rinvenium.index.RinveniumEntities;

public class HotGItem extends Item {
    public HotGItem(Settings settings) {
        super(settings);
    }

    public Text getName(ItemStack stack) {
        return super.getName(stack).copy().styled(style -> style.withColor(0xf58442));
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        float f = 2.0f;

        if (world instanceof ServerWorld serverWorld) {
            GunshotEntity bullet = new GunshotEntity(RinveniumEntities.GUNSHOT, world);
            bullet.updatePosition(user.getX(), user.getY() + 0.5f, user.getZ());
            bullet.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, f * 3.0f, 0.0f);
            serverWorld.spawnEntity(bullet);
        }
        return super.use(world, user, hand);
    }

}
