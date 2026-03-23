package silly.chemthunder.rinvenium.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DescriptionItem extends Item {
    private final String item;
    private final int numberOfLines;

    public DescriptionItem(Settings settings, String item, int numberOfLines) {
        super(settings);
        this.item = item;
        this.numberOfLines = numberOfLines;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        for (int i = 1; i <= numberOfLines; i++) {
            tooltip.add(Text.translatable("item.rinvenium." + item + ".desc" + i).formatted(Formatting.GRAY));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
} // i like this, this is good, maybe add a . between the "desc" and the number though for formatting's sake
// i'm stealing this