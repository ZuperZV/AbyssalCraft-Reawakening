package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.commonCode.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiHeartMixin {

    @Unique
    private static final HeartTextures CORALIUM_PLAGUE =
            abyssalcraft$heart("coralium_plague");

    @Inject(
            method = "extractHearts",
            at = @At("TAIL")
    )
    private void abyssalcraft$renderCustomHearts(
            GuiGraphicsExtractor graphics,
            Player player,
            int xLeft,
            int yLineBase,
            int healthRowHeight,
            int heartOffsetIndex,
            float maxHealth,
            int currentHealth,
            int oldHealth,
            int absorption,
            boolean blink,
            CallbackInfo ci
    ) {
        if (!player.hasEffect(
                BuiltInRegistries.MOB_EFFECT.wrapAsHolder(ModEffects.CORALIUM_PLAGUE.get())
        )) {
            return;
        }

        abyssalcraft$renderHearts(
                graphics,
                player,
                xLeft,
                yLineBase,
                healthRowHeight,
                heartOffsetIndex,
                maxHealth,
                currentHealth,
                oldHealth,
                absorption,
                blink,
                CORALIUM_PLAGUE
        );
    }


    @Unique
    private static void abyssalcraft$renderHearts(
            GuiGraphicsExtractor graphics,
            Player player,
            int xLeft,
            int yLineBase,
            int healthRowHeight,
            int heartOffsetIndex,
            float maxHealth,
            int currentHealth,
            int oldHealth,
            int absorption,
            boolean blink,
            HeartTextures textures
    ) {
        boolean hardcore = player.level().getLevelData().isHardcore();

        int healthContainerCount =
                Mth.ceil(maxHealth / 2.0F);

        int absorptionContainerCount =
                Mth.ceil(absorption / 2.0F);

        int maxHealthHalvesCount =
                healthContainerCount * 2;

        for (
                int containerIndex =
                healthContainerCount + absorptionContainerCount - 1;
                containerIndex >= 0;
                containerIndex--
        ) {
            int row = containerIndex / 10;
            int column = containerIndex % 10;

            int xo = xLeft + column * 8;
            int yo = yLineBase - row * healthRowHeight;

            if (containerIndex < healthContainerCount
                    && containerIndex == heartOffsetIndex) {
                yo -= 2;
            }

            if (currentHealth + absorption <= 4) {
                yo += RandomSource.create().nextInt(2);
            }

            if (containerIndex < healthContainerCount && containerIndex == heartOffsetIndex) {
                yo -= 2;
            }

            abyssalcraft$blitSprite(
                    graphics,
                    xo,
                    yo,
                    textures.container()
            );

            int halves = containerIndex * 2;

            boolean absorptionHeart =
                    containerIndex >= healthContainerCount;

            if (absorptionHeart) {
                int absorptionHalves =
                        halves - maxHealthHalvesCount;

                if (absorptionHalves < absorption) {
                    boolean half =
                            absorptionHalves + 1 == absorption;

                    abyssalcraft$blitSprite(
                            graphics,
                            xo,
                            yo,
                            half
                                    ? textures.absorptionHalf()
                                    : textures.absorptionFull()
                    );
                }
            }

            if (blink && halves < oldHealth) {
                boolean half =
                        halves + 1 == oldHealth;

                abyssalcraft$blitSprite(
                        graphics,
                        xo,
                        yo,
                        half
                                ? textures.getHalfBlinking(hardcore)
                                : textures.getFullBlinking(hardcore)
                );
            }

            if (halves < currentHealth) {
                boolean half =
                        halves + 1 == currentHealth;

                abyssalcraft$blitSprite(
                        graphics,
                        xo,
                        yo,
                        half
                                ? textures.getHalf(hardcore)
                                : textures.getFull(hardcore)
                );
            }
        }
    }

    @Unique
    private static HeartTextures abyssalcraft$heart(String name) {
        return new HeartTextures(
                abyssalcraft$texture(name, "_container"),

                abyssalcraft$texture(name, "_full"),
                abyssalcraft$texture(name, "_half"),

                abyssalcraft$texture(name, "_full_blinking"),
                abyssalcraft$texture(name, "_half_blinking"),

                abyssalcraft$texture(name, "_hardcore_full"),
                abyssalcraft$texture(name, "_hardcore_half"),

                abyssalcraft$texture(name, "_hardcore_full_blinking"),
                abyssalcraft$texture(name, "_hardcore_half_blinking"),

                // Absorption
                abyssalcraft$texture(name, "_full"),
                abyssalcraft$texture(name, "_half")
        );
    }


    @Unique
    private static Identifier abyssalcraft$texture(String name, String suffix) {
        return Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                "textures/gui/sprites/hud/heart/" + name + "/" + name + suffix + ".png"
        );
    }

    @Unique
    private static void abyssalcraft$blitSprite(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            Identifier texture
    ) {
        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                texture,
                x,
                y,
                0.0F,
                0.0F,
                9,
                9,
                9,
                9,
                0xFFFFFFFF
        );
    }


    @Unique
    private record HeartTextures(
            Identifier container,

            Identifier full,
            Identifier half,

            Identifier fullBlinking,
            Identifier halfBlinking,

            Identifier hardcoreFull,
            Identifier hardcoreHalf,

            Identifier hardcoreFullBlinking,
            Identifier hardcoreHalfBlinking,

            Identifier absorptionFull,
            Identifier absorptionHalf
    ) {

        @Unique
        private Identifier getFull(boolean hardcore) {
            return hardcore
                    ? hardcoreFull
                    : full;
        }

        @Unique
        private Identifier getHalf(boolean hardcore) {
            return hardcore
                    ? hardcoreHalf
                    : half;
        }

        @Unique
        private Identifier getFullBlinking(boolean hardcore) {
            return hardcore
                    ? hardcoreFullBlinking
                    : fullBlinking;
        }

        @Unique
        private Identifier getHalfBlinking(boolean hardcore) {
            return hardcore
                    ? hardcoreHalfBlinking
                    : halfBlinking;
        }
    }
}