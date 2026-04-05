package silly.chemthunder.rinvenium.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.block.SmithingTableBlock;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import silly.chemthunder.rinvenium.datagen.RinveniumItemTagProvider;
import silly.chemthunder.rinvenium.screen.ReskinScreenHandler;

@Mixin(SmithingTableBlock.class)
public abstract class SmithingTableBlockMixin extends CraftingTableBlock {
    @Shadow
    @Final
    private static Text SCREEN_TITLE;

    public SmithingTableBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "createScreenHandlerFactory", at = @At("HEAD"), cancellable = true)
    private void rinvenium$reskinScreen(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<NamedScreenHandlerFactory> cir) {
        final Text[] name = {SCREEN_TITLE};
        NamedScreenHandlerFactory namedScreenHandlerFactory = new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player) -> {
            if (player.isSneaking() && player.getMainHandStack().isIn(RinveniumItemTagProvider.TRIGGERS_RESKIN_SCREEN)) {
                name[0] = Text.literal("Reskin");
                return new ReskinScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
            } else {
                return new SmithingScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos));
            }
        }, name[0]);
    }
}
