package net.zuperzv.abyssalcraft_reawakening;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;

@Mod(Constants.MOD_ID)
public class NeoForgeAbyssalCraft {

    public NeoForgeAbyssalCraft(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        eventBus.addListener(AbyssalCraftDatagen::onGatherClientData);

        NeoForgeRegistryHelper.register(eventBus);
    }
}