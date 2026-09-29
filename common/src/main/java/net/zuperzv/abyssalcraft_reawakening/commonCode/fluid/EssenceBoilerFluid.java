package net.zuperzv.abyssalcraft_reawakening.commonCode.fluid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record EssenceBoilerFluid(
        Fluid fluid,
        int amount,
        @Nullable PotionContents potionContents
) {

    public static final EssenceBoilerFluid EMPTY =
            new EssenceBoilerFluid(
                    Fluids.EMPTY,
                    0,
                    PotionContents.EMPTY
            );

    public static final MapCodec<EssenceBoilerFluid> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    BuiltInRegistries.FLUID
                            .byNameCodec()
                            .fieldOf("fluid")
                            .forGetter(EssenceBoilerFluid::fluid),

                    Codec.INT
                            .fieldOf("amount")
                            .forGetter(EssenceBoilerFluid::amount),

                    PotionContents.CODEC
                            .optionalFieldOf(
                                    "potion_contents",
                                    PotionContents.EMPTY
                            )
                            .forGetter(EssenceBoilerFluid::potionContents)

            ).apply(instance, EssenceBoilerFluid::new));

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            EssenceBoilerFluid
            > STREAM_CODEC = new StreamCodec<>() {

        private final StreamCodec<
                RegistryFriendlyByteBuf,
                Fluid
                > fluidCodec =
                ByteBufCodecs.registry(Registries.FLUID);

        @Override
        public void encode(
                RegistryFriendlyByteBuf buf,
                EssenceBoilerFluid value
        ) {
            fluidCodec.encode(
                    buf,
                    value.fluid()
            );

            buf.writeVarInt(
                    value.amount()
            );

            boolean hasPotionContents =
                    value.hasPotionContents();

            buf.writeBoolean(
                    hasPotionContents
            );

            if (hasPotionContents) {
                PotionContents.CODEC
                        .encodeStart(
                                NbtOps.INSTANCE,
                                value.potionContents()
                        )
                        .result()
                        .ifPresentOrElse(
                                buf::writeNbt,
                                () -> buf.writeNbt(
                                        new CompoundTag()
                                )
                        );
            }
        }

        @Override
        public EssenceBoilerFluid decode(
                RegistryFriendlyByteBuf buf
        ) {
            Fluid fluid =
                    fluidCodec.decode(buf);

            int amount =
                    buf.readVarInt();

            PotionContents potionContents =
                    PotionContents.EMPTY;

            if (buf.readBoolean()) {
                CompoundTag tag =
                        buf.readNbt();

                if (tag != null) {
                    potionContents =
                            PotionContents.CODEC
                                    .parse(
                                            NbtOps.INSTANCE,
                                            tag
                                    )
                                    .result()
                                    .orElse(
                                            PotionContents.EMPTY
                                    );
                }
            }

            return new EssenceBoilerFluid(
                    fluid,
                    amount,
                    potionContents
            );
        }
    };

    public EssenceBoilerFluid {
        if (fluid == null) {
            fluid = Fluids.EMPTY;
        }

        if (amount < 0) {
            amount = 0;
        }

        if (potionContents == null) {
            potionContents = PotionContents.EMPTY;
        }

        if (amount == 0) {
            fluid = Fluids.EMPTY;
            potionContents = PotionContents.EMPTY;
        }
    }

    public boolean isEmpty() {
        return amount <= 0
                || fluid == Fluids.EMPTY;
    }

    public boolean isSame(Fluid other) {
        return !isEmpty()
                && fluid == other;
    }

    public boolean isSame(EssenceBoilerFluid other) {
        if (other == null
                || isEmpty()
                || other.isEmpty()) {
            return false;
        }

        return fluid == other.fluid()
                && Objects.equals(
                potionContents(),
                other.potionContents()
        );
    }

    public EssenceBoilerFluid withAmount(
            int newAmount
    ) {
        return new EssenceBoilerFluid(
                fluid,
                newAmount,
                potionContents
        );
    }

    public EssenceBoilerFluid copy() {
        return new EssenceBoilerFluid(
                fluid,
                amount,
                potionContents
        );
    }

    public boolean hasPotionContents() {
        return potionContents != null
                && potionContents != PotionContents.EMPTY;
    }
}