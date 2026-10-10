package silly.chemthunder.rinvenium.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.sound.SoundEvent;
import silly.chemthunder.rinvenium.index.RinveniumEnchantments;
import silly.chemthunder.rinvenium.index.RinveniumItems;
import silly.chemthunder.rinvenium.index.RinveniumSoundEvents;
import silly.chemthunder.rinvenium.index.RinveniumStatusEffects;

public class RinveniumLanguageProvider extends FabricLanguageProvider {
    public RinveniumLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(RinveniumItems.DEBUGGER, "Debugger");

        translationBuilder.add("item.rinvenium.expand_toolip", "§7Hold§r §6[Shift]§r §7for more information.");
        translationBuilder.add("item.rinvenium.expand_toolip.debug", "Testing expandable tooltips. \\n And Testing with custom paragraphs. \\n \\n And multi lines");

        translationBuilder.add(RinveniumItems.ENVINIUM_GLAIVE, "Envinium Glaive");
        translationBuilder.add(RinveniumItems.HAIL_OF_THE_GODS, "Hail of the Gods");
        translationBuilder.add("item.rinvenium.hail_of_the_gods.desc", "A minigun that goes brrrrtttt. \\n \\n Starts with high spread, that slowly becomes more accurate. Be careful though, this weapon is prone to overheating. \\n After overheating, it enters a shotgun mode. If you let it heat up even more, things may go boom...");
        translationBuilder.add(RinveniumItems.DAEDALUS_STORMSHOT, "Daedalus Stormshot");

        translationBuilder.add(RinveniumItems.AURIO_INGOT, "Aurio Ingot");
        translationBuilder.add("item.rinvenium.aurio_ingot.desc", "Obtained by [R-Click] a Superheated Aurio Ingot on a water cauldron or source block.");

        translationBuilder.add(RinveniumItems.ENVINIA_INGOT, "Envinia Ingot");
        translationBuilder.add("item.rinvenium.envinia_ingot.desc", "Obtained by [R-Click] a Superheated Envinia Ingot on a water cauldron or source block.");

        translationBuilder.add(RinveniumItems.ENVIXIUS_INGOT, "Envixius Ingot");
        translationBuilder.add("item.rinvenium.envixius_ingot.desc", "Obtained by [R-Click] a Superheated Envixius Ingot on a water cauldron or source block.");

        translationBuilder.add(RinveniumItems.ENVIXIUS_PLATE, "Envixius Plate");
        translationBuilder.add("item.rinvenium.envixius_plate.desc", "Obtained by [R-Click] a Superheated Envixius Plate on a water cauldron or source block.");

        translationBuilder.add(RinveniumItems.SUPERHEATED_AURIO_INGOT, "Superheated Aurio Ingot");
        translationBuilder.add("item.rinvenium.superheated_aurio_ingot.desc", "Obtained by dropping a Gold Ingot and Copper Ingot into an empty cauldron above a soul flame.");

        translationBuilder.add(RinveniumItems.SUPERHEATED_ENVINIA_INGOT, "Superheated Envinia Ingot");
        translationBuilder.add("item.rinvenium.superheated_envinia_ingot.desc", "Obtained by dropping a Netherite Scrap and Iron Ingot into an empty cauldron above a soul flame.");

        translationBuilder.add(RinveniumItems.SUPERHEATED_ENVIXIUS_INGOT, "Superheated Envixius Ingot");
        translationBuilder.add("item.rinvenium.superheated_envixius_ingot.desc", "Obtained by dropping an Envinia Ingot and Aurio Ingot into an empty cauldron above a soul flame.");

        translationBuilder.add(RinveniumItems.SUPERHEATED_ENVIXIUS_PLATE, "Superheated Envixius Plate");
        translationBuilder.add("item.rinvenium.superheated_envixius_plate.desc", "Obtained by having an anvil fall on Superheated Envixius Ingot.");

        translationBuilder.add(RinveniumItems.BATTERY, "Battery");

        translationBuilder.add(RinveniumItems.ION_CELL, "Ion Cell");
        translationBuilder.add("item.rinvenium.ion_cell.desc", "Obtained by dropping a battery on a powered beacon.");

        translationBuilder.add(RinveniumItems.ENVIXIA_CORE, "Envixia Core");
        translationBuilder.add(RinveniumItems.ENVIXIA_HELMET, "Envixia Helmet");
        translationBuilder.add(RinveniumItems.ENVIXIA_CHESTPLATE, "Envixia Chestplate");
        translationBuilder.add(RinveniumItems.ENVIXIA_LEGGINGS, "Envixia Leggings");
        translationBuilder.add(RinveniumItems.ENVIXIA_BOOTS, "Envixia Boots");

        //region Glaivee
        translationBuilder.add(RinveniumEnchantments.RUSH, "Rush");
        translationBuilder.add("enchantment.rinvenium.rush.desc", "The Envinium Glaive will charge over time, \nallowing the user to rush forward at great speeds on right-click.");

        translationBuilder.add("desc.glaive.unenchanted", "Holding right-click allows the Envinium Glaive to parry attacks, knocking entities back and giving the user an attack buff. \\n Attacking in a certain time frame will increase the damage by 50%.");

        translationBuilder.add("desc.glaive.enchanted", "[Debug] If the dash does not work, right-click while sneaking to reset the timers.");

        translationBuilder.add(RinveniumEnchantments.LOW_POWER_OUTPUT, "Low Power Output");
        translationBuilder.add(RinveniumEnchantments.HIGH_POWER_OUTPUT, "High Power Output");
        translationBuilder.add(RinveniumEnchantments.STARSTRUCK, "Starstruck");
        //endregion

        translationBuilder.add("death.attack.niki", "fuck you");
        translationBuilder.add("death.attack.niki.player", "fuck you");
        translationBuilder.add("death.attack.niki.item", "fuck you");

        translationBuilder.add("death.attack.electricity", "%s was electrocuted");
        translationBuilder.add("death.attack.electricity.player", "%s was electrocuted by %s");
        translationBuilder.add("death.attack.electricity_with_cooldown", "%s was electrocuted");
        translationBuilder.add("death.attack.electricity_with_cooldown.player", "%s was electrocuted by %s");
        translationBuilder.add("death.attack.orchid", "%s was executed");
        translationBuilder.add("death.attack.orchid.player", "%s was executed");

        translationBuilder.add("death.attack.boop", "%s was peppered a thousand times");
        translationBuilder.add("death.attack.boop.player", "%s was peppered a thousand times by %s");

        translationBuilder.add(RinveniumStatusEffects.SPARKED, "Sparked");
        translationBuilder.add("effect.rinvenium.sparked.desc", "Deals low damage rapidly and can chain to nearby players if they are wet.");

        translationBuilder.add(RinveniumStatusEffects.SPARKED_WITH_CD, "Electrified");
        translationBuilder.add("effect.rinvenium.sparked_with_cooldown.desc", "Deals low damage in short intervals.");

        translationBuilder.add(RinveniumStatusEffects.SWISS_CHEESE, "Swiss Cheese");
        translationBuilder.add("effect.rinvenium.swiss_cheese.desc", "Applies a visually impairing overlay of holes.");

        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.GLAIVE_DASH), "Glaive rushes");
        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.GLAIVE_DASH_IMPACT), "Glaive rush hits");
        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.GLAIVE_PARRY), "Glaive parries");
        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.GLAIVE_SLASH), "Glaive slashes");

        translationBuilder.add("sound.rinvenium.hail_of_the_gods.shoot", "Hail of the Gods goes BRRRTT");
        translationBuilder.add("sound.rinvenium.hail_of_the_gods.overheat", "Hail of the Gods overheats"); // these should also prolly use 'getSoundTranslationKey'

        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.ENVIXIA_CORE_USE), "Envixia core activates");

        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.ENVIXIUS_FORGED), "Envixius Ingot forged");
        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.INGOT_FORGED), "Ingot forged");

        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.ION_CELL_FORMED), "Ion Cell formed");
        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.PLATE_FORMED), "Envixius Plate formed");
        translationBuilder.add(getSoundTranslationKey(RinveniumSoundEvents.BELL), "Ominous Bell Rings");

        translationBuilder.add("itemgroup.rinvenium", "Rinvenium");
    }

    public static String getSoundTranslationKey(SoundEvent sound) {
        return "sound." + sound.getId().getNamespace() + "." + sound.getId().getPath();
    }
}