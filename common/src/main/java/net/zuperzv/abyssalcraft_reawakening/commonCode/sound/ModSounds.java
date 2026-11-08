package net.zuperzv.abyssalcraft_reawakening.commonCode.sound;

import net.minecraft.sounds.SoundEvent;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public final class ModSounds {

    private ModSounds() {}

    public static final RegistryHandle<SoundEvent> RAW_SEWER =
            Services.REGISTRY.registerSoundEvent("music_disc.raw_sewer");

    public static void load() {}
}
