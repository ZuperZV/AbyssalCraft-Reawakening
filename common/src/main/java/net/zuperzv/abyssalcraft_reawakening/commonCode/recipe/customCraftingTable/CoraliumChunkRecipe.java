package net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.customCraftingTable;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.ModRecipes;

public class CoraliumChunkRecipe extends CustomRecipe {
    public static final CoraliumChunkRecipe INSTANCE = new CoraliumChunkRecipe();

    public static final MapCodec<CoraliumChunkRecipe> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, CoraliumChunkRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    public CoraliumChunkRecipe() {
        super();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 3 || input.height() != 3) {
            return false;
        }

        for (int slot = 0; slot < input.size(); slot++) {
            ItemStack stack = input.getItem(slot);
            if (slot == 4) {
                if (!stack.is(ModBlocks.ABYSSAL_STONE.block().get().asItem())) {
                    return false;
                }
            } else if (slot == 1 || slot == 3 || slot == 5 || slot == 7) {
                var gems = stack.get(ModDataComponentTypes.CORALIUM_GEMS.get());
                if (!stack.is(ModItems.CORALIUM_GEM.get())
                        || gems == null
                        || gems.getCorealiumGems() != 9) {
                    return false;
                }
            } else if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return new ItemStack(ModItems.CHUNK_OF_CORALIUM.get(), 4);
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.CORALIUM_CHUNK.serializer().get();
    }
}
