package silly.chemthunder.rinvenium;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import silly.chemthunder.rinvenium.datagen.RinveniumModelProvider;
import silly.chemthunder.rinvenium.datagen.RinveniumRecipeProvider;

public class RinveniumDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(RinveniumRecipeProvider::new);
        pack.addProvider(RinveniumModelProvider::new);
	}
}
