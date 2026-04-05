package silly.chemthunder.rinvenium.index;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import silly.chemthunder.rinvenium.Rinvenium;
import silly.chemthunder.rinvenium.screen.ReskinScreenHandler;

public class RinveniumScreenHandlers {
    public static final ScreenHandlerType<ReskinScreenHandler> RESKIN = register("reskin", ReskinScreenHandler::new);

    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER, Rinvenium.id(name), new ScreenHandlerType<>(factory, FeatureFlags.VANILLA_FEATURES));
    }

    public static void registerRinveniumScreenHandlers() {
        Rinvenium.LOGGER.info("Registering Rinvenium Screen Handlers");
    }
}
