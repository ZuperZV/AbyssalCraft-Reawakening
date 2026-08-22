package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.renderer;

import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.AbyssalZombieEntity;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.AbyssalZombieBabyModel;
import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model.AbyssalZombieModel;

public class AbyssalZombieRenderer extends AbstractZombieRenderer<AbyssalZombieEntity, ZombieRenderState, ZombieModel<ZombieRenderState>> {

    private static final Identifier TEXTURE_LOCATION =
            Constants.entityId("abyssal_zombie/abyssal_zombie");

    private static final Identifier BABY_TEXTURE_LOCATION =
            Constants.entityId("abyssal_zombie/abyssal_zombie_baby");

    public AbyssalZombieRenderer(EntityRendererProvider.Context context) {
        super(
                context,

                // Adult model
                new AbyssalZombieModel(
                        context.bakeLayer(AbyssalZombieModel.LAYER_LOCATION)
                ),

                // Baby model
                new AbyssalZombieBabyModel(
                        context.bakeLayer(AbyssalZombieBabyModel.LAYER_LOCATION)
                ),

                // Adult armor
                ArmorModelSet.bake(
                        net.minecraft.client.model.geom.ModelLayers.ZOMBIE_ARMOR,
                        context.getModelSet(),
                        ZombieModel::new
                ),

                // Baby armor
                ArmorModelSet.bake(
                        net.minecraft.client.model.geom.ModelLayers.ZOMBIE_BABY_ARMOR,
                        context.getModelSet(),
                        ZombieModel::new
                )
        );
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return state.isBaby
                ? BABY_TEXTURE_LOCATION
                : TEXTURE_LOCATION;
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }
}