package net.zuperzv.abyssalcraft_reawakening.commonCode.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiArmorBarMixin {
    private static final Identifier ARMOR_EMPTY_SPRITE =
            Identifier.withDefaultNamespace("textures/gui/sprites/hud/armor_empty.png");

    private static final Identifier ARMOR_HALF_SPRITE =
            Identifier.withDefaultNamespace("textures/gui/sprites/hud/armor_half.png");

    private static final Identifier ARMOR_FULL_SPRITE =
            Identifier.withDefaultNamespace("textures/gui/sprites/hud/armor_full.png");

    @Inject(
            method = "extractArmor(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/entity/player/Player;IIII)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void abyssalcraft$renderExtraArmor(
            GuiGraphicsExtractor graphics,
            Player player,
            int yLineBase,
            int numHealthRows,
            int healthRowHeight,
            int xLeft,
            CallbackInfo ci
    ) {
        ci.cancel();

        int armor = player.getArmorValue();

        if (armor <= 0) {
            return;
        }

        int yLineArmor =
                yLineBase - (numHealthRows - 1) * healthRowHeight - 10;

        int displayedArmor = ((armor - 1) % 20) + 1;

        int tier = (armor - 1) / 20;

        for (int i = 0; i < 10; ++i) {
            int xo = xLeft + i * 8;
            int points = displayedArmor - i * 2;

            if (points <= 1) {
                abyssalcraft$blitArmorTexture(
                        graphics,
                        xo,
                        yLineArmor,
                        ARMOR_EMPTY_SPRITE
                );
            }

            if (tier > 0 && points < 2) {
                int previousTier = tier - 1;

                if (previousTier == 0) {
                    abyssalcraft$blitArmorTexture(
                            graphics,
                            xo,
                            yLineArmor,
                            ARMOR_FULL_SPRITE
                    );
                } else {
                    String previousTexture = abyssalcraft$getArmorTierTexture(
                            previousTier,
                            false
                    );

                    abyssalcraft$blitArmorTexture(
                            graphics,
                            xo,
                            yLineArmor,
                            previousTexture
                    );
                }
            }

            if (points <= 0) {
                continue;
            }

            if (tier == 0) {
                Identifier texture = points == 1
                        ? ARMOR_HALF_SPRITE
                        : ARMOR_FULL_SPRITE;

                abyssalcraft$blitArmorTexture(
                        graphics,
                        xo,
                        yLineArmor,
                        texture
                );

                continue;
            }

            String textureName = abyssalcraft$getArmorTierTexture(
                    tier,
                    points == 1
            );

            abyssalcraft$blitArmorTexture(
                    graphics,
                    xo,
                    yLineArmor,
                    textureName
            );
        }
    }

    @Unique
    private static String abyssalcraft$getArmorTierTexture(
            int tier,
            boolean half
    ) {
        String name = switch (tier) {
            case 1 -> "diamond";
            case 2 -> "netherite";
            case 3 -> "ethaxium";
            default -> throw new IllegalArgumentException(
                    "Unknown armor tier: " + tier
            );
        };

        return name + (half ? "_half" : "_full");
    }

    @Unique
    private static void abyssalcraft$blitArmorTexture(
            GuiGraphicsExtractor graphics,
            int x,
            int y,
            String textureName
    ) {
        Identifier texture = Identifier.fromNamespaceAndPath(
                Constants.MOD_ID,
                "textures/gui/sprites/hud/armor/" + textureName + ".png"
        );

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
    private static void abyssalcraft$blitArmorTexture(
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
}