package net.zuperzv.abyssalcraft_reawakening.services;

import net.fabricmc.fabric.api.recipe.v1.sync.SynchronizedRecipes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.*;
import net.zuperzv.abyssalcraft_reawakening.services.types.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

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
