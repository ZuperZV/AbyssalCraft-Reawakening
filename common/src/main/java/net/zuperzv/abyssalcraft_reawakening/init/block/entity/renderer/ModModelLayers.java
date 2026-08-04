package net.zuperzv.abyssalcraft_reawakening.init.block.entity.renderer;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.zuperzv.abyssalcraft_reawakening.Constants;

public class ModModelLayers {

    public static final ModelLayerLocation WITHERWOOD_SIGN =
            new ModelLayerLocation(
                    Identifier.fromNamespaceAndPath(
                            Constants.MOD_ID,
                            "sign/standing/witherwood"
                    ),
                    "main"
            );

    public static final ModelLayerLocation WITHERWOOD_WALL_SIGN =
            new ModelLayerLocation(
                    Identifier.fromNamespaceAndPath(
                            Constants.MOD_ID,
                            "sign/wall/witherwood"
                    ),
                    "main"
            );

}