package riva.rinvenium;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;
import riva.rinvenium.datagen.*;

import java.util.concurrent.CompletableFuture;

public class RinveniumDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(RinveniumAdvancements::new);
		pack.addProvider(RinveniumBlockLootTables::new);
		pack.addProvider((FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lootContextType) -> new RinveniumChestLootTables(output));
		pack.addProvider(RinveniumItemTags::new);
		pack.addProvider(RinveniumModels::new);
		pack.addProvider(RinveniumTranslations::new);
	}
}
