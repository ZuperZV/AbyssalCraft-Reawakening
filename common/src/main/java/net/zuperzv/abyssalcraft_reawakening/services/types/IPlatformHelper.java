package net.zuperzv.abyssalcraft_reawakening.services.types;

import net.minecraft.world.item.crafting.*;

import java.util.Collection;
import java.util.List;

public interface IPlatformHelper {

    /**
     * Gets the name of the current types
     *
     * @return The name of the current types.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Only For Fabric
     *
     * @return the list of recipe entries of given type
     */
    public <I extends RecipeInput, T extends Recipe<I>> List<Collection<RecipeHolder<T>>> getAllOfType(RecipeType<T> type);
}