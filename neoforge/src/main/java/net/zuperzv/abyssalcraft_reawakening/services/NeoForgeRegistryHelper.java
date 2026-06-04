package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.services.types.IRegistryHelper;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.function.BiFunction;
import java.util.function.Function;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
    }

    @Override
    public <T extends Item> RegistryHandle<T> registerItem(String name, Function<Item.Properties, T> item) {
        Identifier id = Constants.id(name);
        DeferredItem<T> deferredItem = ITEMS.registerItem(name, item);
        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public T get() {
                return deferredItem.get();
            }
        };
    }

    @Override
    public <T extends Block> RegistryHandle<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block) {
        Identifier id = Constants.id(name);
        DeferredBlock<T> deferredBlock = BLOCKS.registerBlock(name, block);
        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public T get() {
                return deferredBlock.get();
            }
        };
    }

    @Override
    public <T extends BlockItem> RegistryHandle<T> registerBlockItem(String name, RegistryHandle<? extends Block> block, BiFunction<Block, Item.Properties, T> item) {
        return registerItem(name, properties -> item.apply(block.get(), properties));
    }
}
