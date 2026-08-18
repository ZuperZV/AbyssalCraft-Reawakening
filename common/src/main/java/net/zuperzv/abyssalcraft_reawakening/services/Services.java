package net.zuperzv.abyssalcraft_reawakening.services;

import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.services.types.*;
import net.zuperzv.abyssalcraft_reawakening.services.types.client.IClientRegistryHelper;

import java.util.ServiceLoader;

public class Services {
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);
    public static final IServerHelper SERVER = load(IServerHelper.class);
    public static final INetworkHelper NETWORK = load(INetworkHelper.class);
    public static final IMultiblockInput I_MULTIBLOCK_INPUT = load(IMultiblockInput.class);
    public static final IItemFactory ITEM_FACTORY = load(IItemFactory.class);
    public static final IClientRegistryHelper CLIENT_REGISTRY = load(IClientRegistryHelper.class);

    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}