package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.zuperzv.abyssalcraft_reawakening.services.types.IPlatformHelper;

import java.util.Collection;
import java.util.List;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public <I extends RecipeInput, T extends Recipe<I>> List<Collection<RecipeHolder<T>>> getAllOfType(RecipeType<T> type) {
        return List.of();
    }
}