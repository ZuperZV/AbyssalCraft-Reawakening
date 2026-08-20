package net.zuperzv.abyssalcraft_reawakening.commonCode.screen;

import net.minecraft.world.inventory.MenuType;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public final class ModMenuTypes {
    private ModMenuTypes() {}

    public static void load() {}

    public static final RegistryHandle<MenuType<NecronomiconMenu>> NECRONOMICON_MENU =
            Services.REGISTRY.registerMenuType("necronomicon_menu", NecronomiconMenu::new);

}
