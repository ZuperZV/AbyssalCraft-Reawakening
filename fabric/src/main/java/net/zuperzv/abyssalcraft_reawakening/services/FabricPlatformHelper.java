package net.zuperzv.abyssalcraft_reawakening.services;

import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.zuperzv.abyssalcraft_reawakening.services.types.IPlatformHelper;

import java.util.Collection;
import java.util.List;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <I extends RecipeInput, T extends Recipe<I>> List<Collection<RecipeHolder<T>>> getAllOfType(RecipeType<T> type) {
        SynchronizedRecipes recipeMap = Minecraft.getInstance().level.recipeAccess().getSynchronizedRecipes();

        return List.of(recipeMap.getAllOfType(type)).reversed();
    }
}
