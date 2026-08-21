package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.renderer;

import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.AbyssalZombieEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.AbyssalZombieBabyModel;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.AbyssalZombieModel;

public class AbyssalZombieRenderer extends AgeableMobRenderer<AbyssalZombieEntity, HumanoidRenderState, AbyssalZombieModel> {

    private static final Identifier TEXTURE_LOCATION =
            Constants.entityId("abyssal_zombie");
    private static final Identifier BABY_TEXTURE_LOCATION =
            Constants.entityId("abyssal_zombie_baby");

    public AbyssalZombieRenderer(EntityRendererProvider.Context context, float shadow) {
        super(
                context,

                // Adult
                new AbyssalZombieModel(
                        context.bakeLayer(AbyssalZombieModel.LAYER_LOCATION)
                ),

                // Baby
                new AbyssalZombieBabyModel(
                        context.bakeLayer(AbyssalZombieBabyModel.LAYER_LOCATION)
                ),

                shadow
        );
    }

    @Override
    public Identifier getTextureLocation(HumanoidRenderState state) {
        return state.isBaby ? BABY_TEXTURE_LOCATION : TEXTURE_LOCATION;
    }

    @Override
    public HumanoidRenderState createRenderState() {
        return new HumanoidRenderState();
    }
}