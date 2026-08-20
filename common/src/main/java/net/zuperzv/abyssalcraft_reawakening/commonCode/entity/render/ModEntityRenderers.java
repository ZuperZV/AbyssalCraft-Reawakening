package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.render;

import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.ModEntityTypes;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

public final class ModEntityRenderers {

    private ModEntityRenderers() {
    }

    public static void load() {

        Services.CLIENT_REGISTRY.registerEntityRenderer(
                ModEntityTypes.WITHERWOOD_BOAT,
                context -> new WitherwoodBoatRenderer(
                        context,
                        Constants.id("textures/entity/boat/witherwood.png")
                )
        );

        Services.CLIENT_REGISTRY.registerEntityRenderer(
                ModEntityTypes.WITHERWOOD_CHEST_BOAT,
                context -> new WitherwoodChestBoatRenderer(
                        context,
                        Constants.id("textures/entity/chest_boat/witherwood.png")
                )
        );
    }
}