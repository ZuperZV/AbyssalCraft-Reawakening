package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.renderer;

import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.AbyssalZombieEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.GroundlingEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.AbyssalZombieBabyModel;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.AbyssalZombieModel;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.GroundlingModel;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.state.GroundlingRenderState;
import org.jspecify.annotations.NonNull;

public class GroundlingRenderer extends MobRenderer<GroundlingEntity, GroundlingRenderState, GroundlingModel> {

    private static final Identifier TEXTURE_LOCATION =
            Constants.entityId("groundling/groundling");

    public GroundlingRenderer(EntityRendererProvider.Context context) {
        super(context, new GroundlingModel(context.bakeLayer(GroundlingModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    public GroundlingRenderState createRenderState() {
        return new GroundlingRenderState();
    }

    @Override
    public Identifier getTextureLocation(GroundlingRenderState groundlingRenderState) {
        return TEXTURE_LOCATION;
    }

    @Override
    public void extractRenderState(
            @NonNull GroundlingEntity entity,
            @NonNull GroundlingRenderState state,
            float partialTicks
    ) {
        super.extractRenderState(entity, state, partialTicks);

        state.idleAnimationState.copyFrom(entity.idleAnimationState);
        state.walkAnimationState.copyFrom(entity.walkAnimationState);

        state.attackAnimationState.copyFrom(entity.attackAnimationState);
        state.hideAnimationState.copyFrom(entity.hideAnimationState);

        state.hiddenAnimationState.copyFrom(entity.hiddenAnimationState);
        state.wakeUpAnimationState.copyFrom(entity.wakeUpAnimationState);
    }
}