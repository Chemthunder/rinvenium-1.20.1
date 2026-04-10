package silly.chemthunder.rinvenium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import silly.chemthunder.rinvenium.util.persistent.DeathSequenceState;

import static net.minecraft.server.command.CommandManager.*;

public class DeathSequenceCommand {
    public static LiteralArgumentBuilder<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        return literal("envixiaDeathSequence")
                .requires(c -> c.hasPermissionLevel(2))
                .then(literal("canExecute")
                        .then(literal("query")
                                .executes(context -> executeQueryCanSequence(context.getSource()))
                        )
                        .then(literal("set")
                                .then(argument("value", BoolArgumentType.bool())
                                        .executes(context -> executeSetCanSequence(context.getSource(), BoolArgumentType.getBool(context, "value")))
                                )
                        )
                );
    }

    private static int executeQueryCanSequence(ServerCommandSource source) {
        DeathSequenceState state = DeathSequenceState.getServerState(source.getServer());
        sendCanSequenceFeedback(source, state.canSequence);
        return 1;
    }

    private static int executeSetCanSequence(ServerCommandSource source, boolean value) {
        DeathSequenceState state = DeathSequenceState.getServerState(source.getServer());
        state.canSequence = value;
        sendCanSequenceFeedback(source, state.canSequence);
        state.markDirty();
        return 1;
    }

    private static void sendCanSequenceFeedback(ServerCommandSource source, boolean canExecute) {
        source.sendFeedback(() -> Text.literal("Death sequence on next death is set to: ").append(Text.literal(canExecute ? "true" : "false").formatted(canExecute ? Formatting.GREEN : Formatting.RED)), true);
    }
}
