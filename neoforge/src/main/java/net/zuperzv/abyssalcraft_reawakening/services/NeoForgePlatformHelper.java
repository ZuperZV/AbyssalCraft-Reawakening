package net.zuperzv.abyssalcraft_reawakening.services;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.*;
import net.zuperzv.abyssalcraft_reawakening.init.api.jei.custom.JEIPlugin;
import net.zuperzv.abyssalcraft_reawakening.services.types.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

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