package silly.chemthunder.rinvenium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import silly.chemthunder.rinvenium.util.RinveniumUtil;
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
                        .then(literal("setPlayer")
                                .then(argument("player", EntityArgumentType.player())
                                        .executes(context -> executeSetPlayer(context.getSource(), EntityArgumentType.getPlayer(context, "player")))
                                )
                        )
                        .then(literal("queryPlayer")
                                .executes(context -> executeQueryPlayer(context.getSource()))

                        )
                );
    }

    private static int executeSetPlayer(ServerCommandSource source, ServerPlayerEntity player) {
        DeathSequenceState state = DeathSequenceState.getServerState(source.getServer());
        state.playerUuid = player.getUuid();
        state.markDirty();
        sendPlayerSetFeedback(source, false, player);
        return 1;
    }
    private static int executeQueryPlayer(ServerCommandSource source) {
        DeathSequenceState state = DeathSequenceState.getServerState(source.getServer());
        boolean isEmpty = state.playerUuid.equals(RinveniumUtil.EMPTY_UUID);
        sendPlayerSetFeedback(source, isEmpty, isEmpty ? null : source.getServer().getPlayerManager().getPlayer(state.playerUuid));
        return 1;
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
    private static void sendPlayerSetFeedback(ServerCommandSource source, boolean isEmpty, ServerPlayerEntity serverPlayerEntity) {
        source.sendFeedback(() -> {
            if (isEmpty) {
                return Text.literal("Player to die is: Empty");
            } else {
                return Text.literal("Player to die is: ").append(serverPlayerEntity.getDisplayName());
            }
        }, true);
    }
}
