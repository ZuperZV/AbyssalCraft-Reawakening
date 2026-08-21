package net.zuperzv.abyssalcraft_reawakening.commonCode.entity;

import net.zuperzv.abyssalcraft_reawakening.commonCode.entity.custom.AbyssalZombieEntity;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

public class ModEntityAttributes {
    private ModEntityAttributes() {
    }

    public static void load() {
        Services.ATTRIBUTES.registerEntityAttributes(ModEntityTypes.ABYSSAL_ZOMBIE::get, AbyssalZombieEntity::createAttributes);
    }
}
