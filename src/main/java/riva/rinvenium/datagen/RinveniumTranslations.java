package riva.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import riva.rinvenium.items.RinveniumItems;

public class RinveniumTranslations extends FabricLanguageProvider {
    public RinveniumTranslations(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(RinveniumItems.ENVINIUM_GLAIVE, "Envinium Glaive");
    }
}
