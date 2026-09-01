package net.zuperzv.abyssalcraft_reawakening.commonCode.entity.pipeline;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.zuperzv.abyssalcraft_reawakening.commonCode.mixin.RenderPipelinesAccessor;

public final class GroundlingRenderPipelines {

    public static final RenderPipeline GROUNDLING_DEPTH =
            RenderPipelinesAccessor.abyssalcraft$register(
                    RenderPipeline.builder(
                                    RenderPipelinesAccessor.abyssalcraft$getEntitySnippet()
                            )
                            .withLocation(
                                    Identifier.fromNamespaceAndPath(
                                            "abyssalcraft_reawakening",
                                            "pipeline/groundling_depth"
                                    )
                            )

                            .withColorTargetState(
                                    new ColorTargetState(
                                            java.util.Optional.empty(),
                                            ColorTargetState.WRITE_NONE
                                    )
                            )

                            .withShaderDefine("ALPHA_CUTOUT", 0.1F)

                            .withDepthStencilState(
                                    new DepthStencilState(
                                            CompareOp.LESS_THAN_OR_EQUAL,
                                            true
                                    )
                            )

                            .withCull(true)
                            .build()
            );

    public static final RenderPipeline GROUNDLING_TRANSLUCENT =
            RenderPipelinesAccessor.abyssalcraft$register(
                    RenderPipeline.builder(
                                    RenderPipelinesAccessor.abyssalcraft$getEntitySnippet()
                            )
                            .withLocation(
                                    Identifier.fromNamespaceAndPath(
                                            "abyssalcraft_reawakening",
                                            "pipeline/groundling_translucent"
                                    )
                            )

                            .withColorTargetState(
                                    new ColorTargetState(
                                            BlendFunction.TRANSLUCENT
                                    )
                            )

                            .withDepthStencilState(
                                    new DepthStencilState(
                                            CompareOp.LESS_THAN_OR_EQUAL,
                                            false
                                    )
                            )

                            .withCull(true)
                            .build()
            );

    private GroundlingRenderPipelines() {
    }
}