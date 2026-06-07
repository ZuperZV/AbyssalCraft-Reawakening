package net.zuperzv.abyssalcraft_reawakening.services;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.services.types.IRegistryHelper;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.function.*;

import static net.minecraft.util.datafix.fixes.References.DATA_COMPONENTS;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public <T extends Item> RegistryHandle<T> registerItem(String name, Function<Item.Properties, T> item) {
        ResourceKey<Item> key = IRegistryHelper.itemKey(name);
        Identifier id = key.identifier();
        T registered = Registry.register(BuiltInRegistries.ITEM, id, item.apply(new Item.Properties().setId(key)));

        return new RegistryHandle<T>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public T get() {
                return registered;
            }
        };
    }

    @Override
    public <T extends Block> RegistryHandle<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block) {
        ResourceKey<Block> key = IRegistryHelper.blockKey(name);
        Identifier id = key.identifier();
        T registered = Registry.register(BuiltInRegistries.BLOCK, id, block.apply(BlockBehaviour.Properties.of().setId(key)));

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public T get() {
                return registered;
            }
        };
    }

    @Override
    public <T extends BlockItem> RegistryHandle<T> registerBlockItem(String name, RegistryHandle<? extends Block> block, BiFunction<Block, Item.Properties, T> item) {
        return registerItem(name, properties -> item.apply(block.get(), properties));
    }

    @Override
    public RegistryHandle<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, Consumer<CreativeTabOutput> entries) {
        Identifier id = Constants.id(name);
        CreativeModeTab registered = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id,
                FabricCreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + Constants.MOD_ID + name))
                        .icon(icon)
                        .displayItems((_, output) -> entries.accept(output::accept))
                        .build());
        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public CreativeModeTab get() {
                return registered;
            }
        };
    }

    @Override
    public <T extends AbstractContainerMenu> RegistryHandle<MenuType<T>> registerMenuType(
            String name,
            IRegistryHelper.MenuSupplier<T> menuSupplier
    ) {
        Identifier id = Constants.id(name);

        MenuType<T> type = Registry.register(
                BuiltInRegistries.MENU,
                id,
                new MenuType<>((id1, inv) -> menuSupplier.create(id1, inv), FeatureFlags.VANILLA_SET)
        );

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public MenuType<T> get() {
                return type;
            }
        };
    }

    @Override
    public <T> RegistryHandle<DataComponentType<T>> registerDataComponent(
            String name,
            UnaryOperator<DataComponentType.Builder<T>> builder
    ) {

        Identifier id = Constants.id(name);

        DataComponentType<T> type =
                builder.apply(DataComponentType.builder()).build();

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public DataComponentType<T> get() {
                return type;
            }
        };
    }
}

