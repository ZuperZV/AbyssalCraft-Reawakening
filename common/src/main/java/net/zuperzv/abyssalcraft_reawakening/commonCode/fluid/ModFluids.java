package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.types.FluidDefinition;
import net.zuperzv.abyssalcraft_reawakening.services.util.RegistryHandle;

public final class ModFluids {
    private ModFluids() {}
    public static void load() {}

    public static final RegistryHandle<FlowingFluid> SOURCE_POTION =
            Services.REGISTRY.registerFluid(
                    "source_potion",
                    () -> Services.FLUIDS.createSource(
                            potionDefinition()
                    )
            );

    public static final RegistryHandle<FlowingFluid> FLOWING_POTION =
            Services.REGISTRY.registerFluid(
                    "flowing_potion",
                    () -> Services.FLUIDS.createFlowing(
                            potionDefinition()
                    )
            );

    public static final RegistryHandle<LiquidBlock> POTION_BLOCK =
            Services.REGISTRY.registerBlock(
                    "potion_block",
                    properties -> new GlowingLiquidBlock(
                            SOURCE_POTION.get(),
                            properties
                    )
            );

    public static final RegistryHandle<Item> POTION_BUCKET =
            Services.REGISTRY.registerItem(
                    "potion_bucket",
                    properties -> new BucketItem(
                            SOURCE_POTION.get(),
                            properties
                                    .craftRemainder(Items.BUCKET)
                                    .stacksTo(1)
                    )
            );

    public static final RegistryHandle<FlowingFluid> SOURCE_SULFURIC_ARCANUM =
            Services.REGISTRY.registerFluid(
                    "source_sulfuric_arcanum",
                    () -> Services.FLUIDS.createSource(
                    sulfuricArcanumDefinition()
                    )
            );

    public static final RegistryHandle<FlowingFluid> FLOWING_SULFURIC_ARCANUM =
            Services.REGISTRY.registerFluid(
                    "flowing_sulfuric_arcanum",
                    () -> Services.FLUIDS.createFlowing(
                    sulfuricArcanumDefinition()
                    )
            );

    public static final RegistryHandle<LiquidBlock> SULFURIC_ARCANUM_BLOCK =
            Services.REGISTRY.registerBlock(
                    "sulfuric_arcanum",
                    properties -> new GlowingLiquidBlock(
                    SOURCE_SULFURIC_ARCANUM.get(),
                    properties
                    )
            );

    public static final RegistryHandle<Item> SULFURIC_ARCANUM_BUCKET =
            Services.REGISTRY.registerItem(
                    "sulfuric_arcanum_bucket",
                    properties -> new BucketItem(
                    SOURCE_SULFURIC_ARCANUM.get(),
                    properties
                            .craftRemainder(Items.BUCKET)
                            .stacksTo(1)
                    )
            );

    private static FluidDefinition potionDefinition() {
        return new FluidDefinition(
                "potion",

                2,
                1,
                5,

                100.0F,

                () -> SOURCE_POTION.get(),
                () -> FLOWING_POTION.get(),

                () -> POTION_BLOCK.get(),
                () -> POTION_BUCKET.get()
        );
    }

    private static FluidDefinition sulfuricArcanumDefinition() {
        return new FluidDefinition(
                "sulfuric_arcanum",
                2,
                1,
                5,
                100.0F,
                () -> SOURCE_SULFURIC_ARCANUM.get(),
                () -> FLOWING_SULFURIC_ARCANUM.get(),
                () -> SULFURIC_ARCANUM_BLOCK.get(),
                () -> SULFURIC_ARCANUM_BUCKET.get()
        );
    }
}