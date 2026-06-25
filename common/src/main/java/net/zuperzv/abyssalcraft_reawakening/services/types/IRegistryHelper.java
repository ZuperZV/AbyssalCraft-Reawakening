package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.block.entity.custom.StoneRitualAltarBlockEntity;
import net.zuperzv.abyssalcraft_reawakening.services.util.BlockWithItemRegistryHandle;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

import java.util.function.*;

public interface IRegistryHelper {

    default <T extends Block> BlockWithItemRegistryHandle<T> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, T> block) {
        return registerBlockWithItem(name, block, BlockItem::new);
    }

    default <T extends Block> BlockWithItemRegistryHandle<T> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, T> block, BiFunction<Block, Item.Properties, BlockItem> item) {
        RegistryHandle<T> blockHandle = registerBlock(name, block);
        RegistryHandle<BlockItem> itemHandle = registerBlockItem(name, blockHandle, item);
        return new BlockWithItemRegistryHandle<>(blockHandle, itemHandle);
    }

    <T extends Item> RegistryHandle<T> registerItem(String name, Function<Item.Properties, T> item);

    <T extends Block> RegistryHandle<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> block);

    <T extends BlockItem> RegistryHandle<T> registerBlockItem(String name, RegistryHandle<? extends Block> block, BiFunction<Block, Item.Properties, T> item);


    static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, Constants.id(name));
    }

    static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Constants.id(name));
    }

    @FunctionalInterface
    interface MenuSupplier<T extends AbstractContainerMenu> {
        T create(int id, net.minecraft.world.entity.player.Inventory inv);
    }

    <T extends AbstractContainerMenu>
    RegistryHandle<MenuType<T>> registerMenuType(String name, MenuSupplier<T> menuSupplier);

    RegistryHandle<CreativeModeTab> registerCreativeTab(String name, Supplier<ItemStack> icon, Consumer<CreativeTabOutput> entries);

    @FunctionalInterface
    interface CreativeTabOutput {
        void accept(ItemLike itemLike);
    }

    <T> RegistryHandle<DataComponentType<T>> registerDataComponent(
            String name,
            UnaryOperator<DataComponentType.Builder<T>> builder
    );

    <T extends BlockEntity> RegistryHandle<BlockEntityType<T>> registerBlockEntityType(
            String name,
            BiFunction<BlockPos, BlockState, T> factory,
            Supplier<? extends Block>... blocks
    );

    <T extends RecipeType<?>> RegistryHandle<T> registerRecipeType(String name, Supplier<T> type);

    <T extends RecipeSerializer<?>> RegistryHandle<T> registerRecipeSerializer(String name, Supplier<T> serializer);

    public record RecipeRegistryHandle<T extends RecipeType<?>, S extends RecipeSerializer<?>>(
            RegistryHandle<T> type,
            RegistryHandle<S> serializer
    ) {}

    <T extends RecipeType<?>, S extends RecipeSerializer<?>>
    RecipeRegistryHandle<T, S> registerRecipeTypeAndSerializer(
            String name,
            Supplier<T> type,
            Supplier<S> serializer
    );
}
