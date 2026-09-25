package net.zuperzv.abyssalcraft_reawakening.commonCode.shaders;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.BasicEffect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ModShaderRenderer {

    private static final Map<Identifier, ActiveShader> ACTIVE_SHADERS =
            new HashMap<>();

    private static final Map<Identifier, PostChain> LOADED_CHAINS =
            new HashMap<>();

    private ModShaderRenderer() {
    }

    private static class ActiveShader {

        final PostChain chain;
        final long startTime;
        final long durationMs;

        ActiveShader(PostChain chain, long durationMs) {
            this.chain = chain;
            this.startTime = System.currentTimeMillis();
            this.durationMs = durationMs;
        }

        boolean isExpired() {
            return durationMs > 0
                    && System.currentTimeMillis() - startTime >= durationMs;
        }
    }

    private static PostChain loadShader(Identifier id) {

        PostChain existing = LOADED_CHAINS.get(id);

        if (existing != null) {
            return existing;
        }

        Minecraft mc = Minecraft.getInstance();

        PostChain chain = mc.getShaderManager().getPostChain(
                id,
                Set.of(Identifier.parse("minecraft:main"))
        );

        if (chain == null) {
            return null;
        }

        LOADED_CHAINS.put(id, chain);

        return chain;
    }

    public static void enableShader(
            Identifier shaderId,
            int ticks
    ) {
        Minecraft mc = Minecraft.getInstance();

        mc.execute(() -> {

            PostChain chain = loadShader(shaderId);

            if (chain == null) {
                return;
            }

            long durationMs = ticks * 50L;

            ACTIVE_SHADERS.put(
                    shaderId,
                    new ActiveShader(chain, durationMs)
            );
        });
    }

    public static void render() {

        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) {
            return;
        }

        for (MobEffectInstance effect : mc.player.getActiveEffects()) {

            MobEffect mobEffect = effect.getEffect().value();

            if (mobEffect instanceof BasicEffect basicEffect
                    && basicEffect.hasShader()) {

                Identifier shaderId = basicEffect.getShader();

                PostChain chain = loadShader(shaderId);

                if (chain == null) {
                    continue;
                }

                chain.process(
                        mc.getMainRenderTarget(),
                        GraphicsResourceAllocator.UNPOOLED
                );

                break;
            }
        }

        if (ACTIVE_SHADERS.isEmpty()) {
            return;
        }

        Iterator<Map.Entry<Identifier, ActiveShader>> iterator =
                ACTIVE_SHADERS.entrySet().iterator();

        while (iterator.hasNext()) {

            Map.Entry<Identifier, ActiveShader> entry =
                    iterator.next();

            ActiveShader shader = entry.getValue();

            if (shader.isExpired()) {
                iterator.remove();
                continue;
            }

            shader.chain.process(
                    mc.getMainRenderTarget(),
                    GraphicsResourceAllocator.UNPOOLED
            );
        }
    }

    public static void clear() {
        ACTIVE_SHADERS.clear();
    }
}