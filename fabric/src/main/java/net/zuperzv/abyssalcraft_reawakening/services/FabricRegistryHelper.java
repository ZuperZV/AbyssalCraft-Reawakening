package net.zuperzv.abyssalcraft_reawakening.services;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.services.types.IRegistryHelper;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.Arrays;
import java.util.function.*;

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

        DataComponentType<T> type = Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                id,
                builder.apply(DataComponentType.builder()).build()
        );

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

    @Override
    public <T extends BlockEntity> RegistryHandle<BlockEntityType<T>> registerBlockEntityType(
            String name,
            BiFunction<BlockPos, BlockState, T> factory,
            Supplier<? extends Block>... blocks
    ) {

        Identifier id = Constants.id(name);

        Block[] resolved = Arrays.stream(blocks)
                .map(Supplier::get)
                .toArray(Block[]::new);

        BlockEntityType<T> registered = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                id,
                FabricBlockEntityTypeBuilder.create(factory::apply, resolved).build()
        );

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public BlockEntityType<T> get() {
                return registered;
            }
        };
    }

    @Override
    public <T extends RecipeType<?>> RegistryHandle<T> registerRecipeType(String name, Supplier<T> type) {
        Identifier id = Constants.id(name);

        T registered = Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                id,
                type.get()
        );

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
    public <T extends RecipeSerializer<?>> RegistryHandle<T> registerRecipeSerializer(String name, Supplier<T> serializer) {
        Identifier id = Constants.id(name);

        T registered = Registry.register(
                BuiltInRegistries.RECIPE_SERIALIZER,
                id,
                serializer.get()
        );

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
    public <T extends RecipeType<?>, S extends RecipeSerializer<?>>
    RecipeRegistryHandle<T, S> registerRecipeTypeAndSerializer(
            String name,
            Supplier<T> type,
            Supplier<S> serializer
    ) {

        RegistryHandle<T> typeHandle = registerRecipeType(name, type);
        RegistryHandle<S> serializerHandle = registerRecipeSerializer(name, serializer);

        return new RecipeRegistryHandle<>(typeHandle, serializerHandle);
    }

    @Override
    public <T extends TreeDecorator> RegistryHandle<TreeDecoratorType<T>> registerTreeDecoratorType(
            String name,
            MapCodec<T> codec
    ) {
        Identifier id = Constants.id(name);

        TreeDecoratorType<T> registered =
                Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, id, new TreeDecoratorType<>(codec));

        return new RegistryHandle<>() {

            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public TreeDecoratorType<T> get() {
                return registered;
            }
        };
    }

    @Override
    public <T extends PlacementModifier> RegistryHandle<PlacementModifierType<T>> registerPlacementModifierType(
            String name,
            MapCodec<T> codec
    ) {
        Identifier id = Constants.id(name);

        PlacementModifierType<T> registered =
                Registry.register(
                        BuiltInRegistries.PLACEMENT_MODIFIER_TYPE,
                        id,
                        () -> codec
                );

        return new RegistryHandle<>() {

            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public PlacementModifierType<T> get() {
                return registered;
            }
        };
    }

    @Override
    public <T extends Entity> RegistryHandle<EntityType<T>> registerEntityType(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = IRegistryHelper.entityTypeKey(name);
        Identifier id = key.identifier();
        EntityType<T> registered = Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(key));
        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public EntityType<T> get() {
                return registered;
            }
        };
    }
}

