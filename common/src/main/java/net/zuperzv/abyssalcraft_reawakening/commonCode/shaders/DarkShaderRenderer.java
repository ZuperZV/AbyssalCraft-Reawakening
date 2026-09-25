package net.zuperzv.abyssalcraft_reawakening.commonCode.shaders;

import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import net.zuperzv.abyssalcraft_reawakening.Constants;

import java.util.Set;

public final class DarkShaderRenderer {

    private static final Identifier DARK_SHADER =
            Constants.id("shaders/post/dark");

    private static PostChain darkShaderChain;

    private static boolean enabled = false;

    private static long enableStartTime = 0;
    private static long durationMs = 0;

    private DarkShaderRenderer() {
    }

    private static void initShader() {

        if (darkShaderChain != null) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        darkShaderChain = mc.getShaderManager().getPostChain(
                DARK_SHADER,
                Set.of()
        );
    }

    public static void enableForTicks(int ticks) {

        Minecraft mc = Minecraft.getInstance();

        mc.execute(() -> {

            enabled = true;

            enableStartTime =
                    System.currentTimeMillis();

            durationMs =
                    ticks * 50L;

            initShader();
        });
    }

    public static void render() {

        if (!enabled) {
            return;
        }

        if (durationMs > 0) {

            long elapsed =
                    System.currentTimeMillis()
                            - enableStartTime;

            if (elapsed >= durationMs) {
                enabled = false;
                return;
            }
        }

        initShader();

        if (darkShaderChain == null) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        darkShaderChain.process(
                mc.getMainRenderTarget(),
                GraphicsResourceAllocator.UNPOOLED
        );
    }

    public static void disable() {
        enabled = false;
    }
}