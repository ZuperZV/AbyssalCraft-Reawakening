package net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
* MIT License
* Copyright (c) 2020 BlakeBr0
*
* This code is licensed under the "MIT License"
* https://github.com/BlakeBr0/MysticalCustomization/blob/1.21/LICENSE
*
* Modified by: ZuperZ
*/

public final class DataItemRegistry {

    private static final DataItemRegistry INSTANCE =
            new DataItemRegistry();

    private final Map<Identifier, DataItemType> dataItemTypes =
            new LinkedHashMap<>();

    private DataItemRegistry() {
    }

    public static DataItemRegistry getInstance() {
        return INSTANCE;
    }

    public void register(DataItemType scute) {
        if (!scute.isEnabled()) {
            return;
        }

        if (this.dataItemTypes.values().stream()
                .anyMatch(existing ->
                        existing.getName().equals(scute.getName()))) {
            return;
        }

        this.dataItemTypes.put(
                scute.getId(),
                scute
        );
    }

    public List<DataItemType> getDataItemTypes() {
        return List.copyOf(
                this.dataItemTypes.values()
        );
    }

    public DataItemType getDataItemTypeById(
            Identifier id
    ) {
        return this.dataItemTypes.get(id);
    }

    public DataItemType getDataItemTypeByName(
            String name
    ) {
        return this.dataItemTypes.values()
                .stream()
                .filter(scute ->
                        name.equals(scute.getName()))
                .findFirst()
                .orElse(null);
    }

    public void registerItems() {

        for (DataItemType scute :
                this.dataItemTypes.values()) {

            if (!scute.isEnabled()) {
                continue;
            }

            registerScuteItems(scute);
        }
    }

    private void registerScuteItems(DataItemType scute) {
        /*
         * DUST
         */
        Services.REGISTRY.registerItem(
                scute.getNameWithSuffix("dust"),
                Item::new
        );
    }
}