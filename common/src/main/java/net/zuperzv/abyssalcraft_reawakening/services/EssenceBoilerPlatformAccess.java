package net.zuperzv.abyssalcraft_reawakening.services;

import net.zuperzv.abyssalcraft_reawakening.services.types.IEssenceBoilerPlatformHooks;

public final class EssenceBoilerPlatformAccess {
    private EssenceBoilerPlatformAccess() {
    }

    public static IEssenceBoilerPlatformHooks get() {
        if (Services.ESSENCE_BOILER_PLATFORM_HOOKS instanceof IEssenceBoilerPlatformHooks hooks) {
            return hooks;
        }

        throw new IllegalStateException(
                "Services.PLATFORM does not implement IEssenceBoilerPlatformHooks"
        );
    }
}
