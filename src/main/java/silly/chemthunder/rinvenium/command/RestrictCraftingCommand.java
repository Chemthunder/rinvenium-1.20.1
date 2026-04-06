package silly.chemthunder.rinvenium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import silly.chemthunder.rinvenium.util.persistent.RestrictCraftingState;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RestrictCraftingCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        return literal("restrictCrafting")
                .requires(c -> c.hasPermissionLevel(3))
                .then(literal("query")
                        .executes(context -> executeQuery(context.getSource()))
                )
                .then(literal("set")
                        .then(argument("value", BoolArgumentType.bool())
                                .executes(context -> executeSet(context.getSource(), BoolArgumentType.getBool(context, "value")))
                        )
                );
    }

    private static int executeSet(ServerCommandSource source, boolean value) {
        RestrictCraftingState state = RestrictCraftingState.getServerState(source.getServer());
        state.lockRecipes = value;
        sendFeedback(source, state.lockRecipes);
        state.markDirty();
        return 1;
    }

    private static int executeQuery(ServerCommandSource source) {
        RestrictCraftingState state = RestrictCraftingState.getServerState(source.getServer());
        sendFeedback(source, state.lockRecipes);
        return 1;
    }

    private static void sendFeedback(ServerCommandSource source, boolean isLocked) {
        source.sendFeedback(() -> Text.literal("Rinvenium crafting recipes are ").append(Text.literal(isLocked ? "locked" : "unlocked").formatted(isLocked ? Formatting.RED : Formatting.GREEN)), true);
    }
}