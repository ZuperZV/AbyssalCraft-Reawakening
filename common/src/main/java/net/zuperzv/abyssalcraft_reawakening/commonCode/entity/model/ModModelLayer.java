package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.model;

import net.zuperzv.abyssalcraft_reawakening.services.Services;

public final class ModModelLayer {

    private ModModelLayer() {
    }

    public static void load() {

        Services.CLIENT_REGISTRY.registerModelLayer(
                AbyssalZombieModel.LAYER_LOCATION, AbyssalZombieModel::createBodyLayer);
        Services.CLIENT_REGISTRY.registerModelLayer(
                AbyssalZombieBabyModel.LAYER_LOCATION, AbyssalZombieBabyModel::createBodyLayer);
    }
}