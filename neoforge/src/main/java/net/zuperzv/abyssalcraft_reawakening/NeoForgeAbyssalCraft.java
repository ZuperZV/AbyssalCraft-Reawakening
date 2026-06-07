package net.zuperzv.abyssalcraft_reawakening;


import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@Mod(Constants.MOD_ID)
public class NeoForgeAbyssalCraft {

    public NeoForgeAbyssalCraft(IEventBus eventBus) {
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        eventBus.addListener(AbyssalCraftDatagen::onGatherClientData);

        NeoForgeRegistryHelper.register(eventBus);
    }
}