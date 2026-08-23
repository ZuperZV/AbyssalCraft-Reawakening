package net.zuperzv.abyssalcraft_reawakening.services;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.services.types.IRegistryHelper;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.Arrays;
import java.util.function.*;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Constants.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, Constants.MOD_ID);
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, Constants.MOD_ID);
    private static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(BuiltInRegistries.TREE_DECORATOR_TYPE, Constants.MOD_ID);
    private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
            DeferredRegister.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.key(), Constants.MOD_ID);
    public static final DeferredRegister.Entities ENTITIES = DeferredRegister.createEntities(Constants.MOD_ID);
    private static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, Constants.MOD_ID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
        MENU_TYPES.register(eventBus);
        DATA_COMPONENTS.register(eventBus);
        BLOCK_ENTITY_TYPES.register(eventBus);
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
        TREE_DECORATORS.register(eventBus);
        PLACEMENT_MODIFIERS.register(eventBus);
        FEATURES.register(eventBus);
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

    @Override
    public RegistryHandle<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, Consumer<CreativeTabOutput> entries) {
        Identifier id = Constants.id(name);
        DeferredHolder<CreativeModeTab, CreativeModeTab> deferredTab = CREATIVE_MODE_TABS.register(name,
                () -> CreativeModeTab.builder()
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
                return deferredTab.get();
            }
        };
    }

    @Override
    public <T extends AbstractContainerMenu> RegistryHandle<MenuType<T>> registerMenuType(
            String name,
            IRegistryHelper.MenuSupplier<T> menuSupplier
    ) {
        Identifier id = Constants.id(name);

        DeferredHolder<MenuType<?>, MenuType<T>> holder =
                MENU_TYPES.register(name, () ->
                        new MenuType<>(menuSupplier::create, FeatureFlags.VANILLA_SET)
                );

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public MenuType<T> get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T> RegistryHandle<DataComponentType<T>> registerDataComponent(
            String name,
            UnaryOperator<DataComponentType.Builder<T>> builder
    ) {

        DeferredHolder<DataComponentType<?>, DataComponentType<T>> holder =
                DATA_COMPONENTS.register(name,
                        () -> builder.apply(DataComponentType.builder()).build()
                );

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return Constants.id(name);
            }

            @Override
            public DataComponentType<T> get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T extends BlockEntity> RegistryHandle<BlockEntityType<T>> registerBlockEntityType(
            String name,
            BiFunction<BlockPos, BlockState, T> factory,
            Supplier<? extends Block>... blocks
    ) {
        DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder =
                BLOCK_ENTITY_TYPES.register(name,
                        () -> new BlockEntityType<>(
                                factory::apply,
                                Arrays.stream(blocks)
                                        .map(Supplier::get)
                                        .toArray(Block[]::new)
                        )
                );

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return Constants.id(name);
            }

            @Override
            public BlockEntityType<T> get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T extends RecipeType<?>> RegistryHandle<T> registerRecipeType(String name, Supplier<T> type) {
        Identifier id = Constants.id(name);

        DeferredHolder<RecipeType<?>, T> holder =
                (DeferredHolder) RECIPE_TYPES.register(name, type);

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public T get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T extends RecipeSerializer<?>> RegistryHandle<T> registerRecipeSerializer(String name, Supplier<T> serializer) {
        Identifier id = Constants.id(name);

        DeferredHolder<RecipeSerializer<?>, T> holder =
                (DeferredHolder) RECIPE_SERIALIZERS.register(name, serializer);

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public T get() {
                return holder.get();
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

        DeferredHolder<TreeDecoratorType<?>, TreeDecoratorType<T>> holder =
                (DeferredHolder) TREE_DECORATORS.register(name, () -> new TreeDecoratorType<>(codec));

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public TreeDecoratorType<T> get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T extends PlacementModifier> RegistryHandle<PlacementModifierType<T>> registerPlacementModifierType(
            String name,
            MapCodec<T> codec
    ) {

        DeferredHolder<PlacementModifierType<?>, PlacementModifierType<T>> holder =
                (DeferredHolder) PLACEMENT_MODIFIERS.register(
                        name,
                        () -> new PlacementModifierType<T>() {

                            @Override
                            public MapCodec<T> codec() {
                                return (MapCodec<T>) codec;
                            }
                        }
                );

        return new RegistryHandle<>() {

            @Override
            public Identifier id() {
                return Constants.id(name);
            }

            @Override
            public PlacementModifierType<T> get() {
                return holder.get();
            }
        };
    }

    @Override
    public <T extends Entity> RegistryHandle<EntityType<T>> registerEntityType(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = IRegistryHelper.entityTypeKey(name);
        Identifier id = key.identifier();
        DeferredHolder<EntityType<?>, EntityType<T>> deferredEntityType = ENTITIES.register(name, () -> builder.build(key));
        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public EntityType<T> get() {
                return deferredEntityType.get();
            }
        };
    }

    @Override
    public <T extends FeatureConfiguration, F extends Feature<T>>
    RegistryHandle<F> registerFeature(
            String name,
            F feature
    ) {
        Identifier id = Constants.id(name);

        DeferredHolder<Feature<?>, F> holder =
                (DeferredHolder<Feature<?>, F>) FEATURES.register(
                        name,
                        () -> feature
                );

        return new RegistryHandle<>() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public F get() {
                return holder.get();
            }
        };
    }
}
