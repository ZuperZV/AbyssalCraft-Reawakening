package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.zuperzv.abyssalcraft_reawakening.commonCode.block.entity.custom.PotionFluidBlockEntity;
import org.jetbrains.annotations.Nullable;

public class PotionBucketItem extends BucketItem {
    public PotionBucketItem(net.minecraft.world.level.material.Fluid content, Properties properties) {
        super(content, properties);
    }

    @Override
    public void checkExtraContent(
            @Nullable LivingEntity user,
            Level level,
            ItemStack itemStack,
            BlockPos pos
    ) {
        if (level.getBlockEntity(pos) instanceof PotionFluidBlockEntity blockEntity) {
            PotionContents contents = itemStack.get(DataComponents.POTION_CONTENTS);
            if (contents != null && contents != PotionContents.EMPTY) {
                blockEntity.setPotionContents(contents);
            }
        }
    }
}
