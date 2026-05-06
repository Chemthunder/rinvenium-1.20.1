package silly.chemthunder.rinvenium.index;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.cca.entity.DeathSequenceComponent;
import silly.chemthunder.rinvenium.command.DeathSequenceCommand;
import silly.chemthunder.rinvenium.command.RestrictCraftingCommand;

import static net.minecraft.server.command.CommandManager.literal;

public class RinveniumCommands {
    public static void registerMainCommand() {
        CommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
            commandDispatcher.register(literal("rinvenium")
                    .then(RestrictCraftingCommand.register(commandDispatcher, commandRegistryAccess))
                    .then(DeathSequenceCommand.register(commandDispatcher, commandRegistryAccess))
            );
        });
    }
    
    public static void registerRinveniumCommands() {
        Rinvenium.LOGGER.info("Registering Rinvenium Commands");
        registerMainCommand();
    }
}