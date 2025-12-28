package silly.chemthunder.rinvenium;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;

public class Rinvenium implements ModInitializer {
	public static final String MOD_ID = "rinvenium";

    public static Identifier id (String path){
        return Identifier.of(MOD_ID, path); }
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        RinveniumItems.index();
        RinveniumEnchantments.index();
        RinveniumStatusEffects.index();

		LOGGER.info("Hello Fabric world!");
	}
}