package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

/*
 * MIT License
 * Copyright (c) 2020 BlakeBr0
 *
 * This code is licensed under the "MIT License"
 * https://github.com/BlakeBr0/MysticalCustomization/blob/1.21/LICENSE
 *
 * Modified by: ZuperZ
 */

public class DataItemType {

    private final Identifier id;

    private Identifier texture;
    private Identifier armorTexture;
    private Component displayName;

    private Supplier<? extends Item> essence;

    private boolean enabled;
    private boolean registerEssenceItem;

    public DataItemType(
            Identifier id,
            Identifier texture,
            Identifier armorTexture,
            boolean enabled
    ) {
        this.id = id;
        this.enabled = enabled;
        this.registerEssenceItem = true;
        this.texture = texture;
        this.armorTexture = armorTexture;
    }

    public Identifier getId() {
        return this.id;
    }

    public Identifier getTexture() {
        return this.texture;
    }

    public Identifier getArmorTexture() {
        return this.armorTexture;
    }

    public DataItemType setTexture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    public DataItemType setArmorTexture(Identifier armorTexture) {
        this.armorTexture = armorTexture;
        return this;
    }

    public String getName() {
        return this.id.getPath();
    }

    public String getModId() {
        return this.id.getNamespace();
    }

    public String getNameWithSuffix(String suffix) {
        return this.getName() + "_" + suffix;
    }

    public Component getDisplayName() {
        return this.displayName != null
                ? this.displayName
                : Component.translatable(
                "armadillo_scute.%s.%s".formatted(
                        this.getModId(),
                        this.getName()
                )
        );
    }

    public DataItemType setDisplayName(Component displayName) {
        this.displayName = displayName;
        return this;
    }

    public Item getEssenceItem() {
        return this.essence == null
                ? null
                : this.essence.get();
    }

    public DataItemType setEssenceItem(
            Supplier<? extends Item> essence
    ) {
        return this.setEssenceItem(essence, false);
    }

    public DataItemType setEssenceItem(
            Supplier<? extends Item> essence,
            boolean register
    ) {
        this.essence = essence;
        this.registerEssenceItem = register;
        return this;
    }

    public boolean shouldRegisterEssenceItem() {
        return this.registerEssenceItem;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public DataItemType setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
}