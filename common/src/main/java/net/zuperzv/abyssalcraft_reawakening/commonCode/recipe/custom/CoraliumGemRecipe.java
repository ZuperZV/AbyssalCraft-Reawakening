package net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.CoraliumGemsData;
import net.zuperzv.abyssalcraft_reawakening.commonCode.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.commonCode.recipe.ModRecipes;

public class CoraliumGemRecipe extends CustomRecipe {

    public static final CoraliumGemRecipe INSTANCE =
            new CoraliumGemRecipe(CraftingBookCategory.MISC);

    public static final MapCodec<CoraliumGemRecipe> CODEC =
            MapCodec.unit(INSTANCE);

    public static final StreamCodec<RegistryFriendlyByteBuf, CoraliumGemRecipe> STREAM_CODEC =
            StreamCodec.unit(INSTANCE);

    public CoraliumGemRecipe(CraftingBookCategory category) {
        super();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int total = 0;
        int gemItems = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.isEmpty()) {
                continue;
            }

            if (!stack.is(ModItems.CORALIUM_GEM.get())) {
                return false;
            }

            CoraliumGemsData data =
                    stack.get(ModDataComponentTypes.CORALIUM_GEMS.get());

            int amount = data != null
                    ? data.getCorealiumGems()
                    : 0;

            total += amount;
            gemItems++;
        }

        return gemItems >= 2 && gemItems <= 9 && total <= 9;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        int total = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);

            if (stack.isEmpty()) {
                continue;
            }

            CoraliumGemsData data =
                    stack.get(ModDataComponentTypes.CORALIUM_GEMS.get());

            if (data != null) {
                total += data.getCorealiumGems();
            }
        }

        if (total > 9) {
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(ModItems.CORALIUM_GEM.get());

        result.set(
                ModDataComponentTypes.CORALIUM_GEMS.get(),
                new CoraliumGemsData(total)
        );

        return result;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.CORALIUM_GEM.serializer().get();
    }
}