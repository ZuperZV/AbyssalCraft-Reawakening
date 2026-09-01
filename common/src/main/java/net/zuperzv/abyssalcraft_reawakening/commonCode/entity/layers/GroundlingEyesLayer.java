package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.layers;

import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.GroundlingModel;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.state.GroundlingRenderState;

public class GroundlingEyesLayer extends EyesLayer<GroundlingRenderState, GroundlingModel> {

    private static final RenderType GROUNDLING_EYES = RenderTypes.eyes(
            Constants.entityId("groundling/groundling_eyes"));

    public GroundlingEyesLayer(RenderLayerParent<GroundlingRenderState, GroundlingModel> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return GROUNDLING_EYES;
    }
}