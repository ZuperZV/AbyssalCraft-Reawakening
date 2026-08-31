package net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.zuperzv.abyssalcraft_reawakening.commonCode.item.custom.dataDrivenItems.DataItemType;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DataItemJsonLoader {

        private static final List<DataItemType> LOADED_ITEMS =
            new ArrayList<>();

    private DataItemJsonLoader() {
    }

    public static void load() {

        MinecraftServer server = Services.SERVER.getCurrentServer();
        if (server == null) {
            return;
        }

        loadFromResourceManager(server.getResourceManager());
    }

    /**
     * data/<namespace>/data_item/*.json
     */
    public static void loadFromResourceManager(ResourceManager resourceManager) {
        clear();

        Map<Identifier, Resource> files =
                resourceManager.listResources(
                        "data_item",
                        DataItemJsonLoader::isJsonDataFile
                );

        for (Map.Entry<Identifier, Resource> entry : files.entrySet()) {
            loadDataItem(entry.getKey(), entry.getValue());
        }
    }

    private static void loadDataItem(
            Identifier resourceId,
            Resource resource
    ) {
        try (InputStreamReader reader =
                     new InputStreamReader(resource.open())) {

            JsonObject json =
                    JsonParser.parseReader(reader)
                            .getAsJsonObject();

            if (json.has("type")) {
                String type = json.get("type").getAsString();

                if (type.isBlank()) {
                    return;
                }

                if (!type.equals("data_item")) {
                    return;
                }
            }

            String path = resourceId.getPath();

            String scuteName = path
                    .replace("data_item/", "")
                    .replace(".json", "");

            Identifier scuteId =
                    Identifier.fromNamespaceAndPath(
                            resourceId.getNamespace(),
                            scuteName
                    );

            String texture =
                    json.get("texture").getAsString();

            String armorTexture =
                    json.get("armor_texture").getAsString();

            boolean enabled =
                    !json.has("enabled")
                            || json.get("enabled").getAsBoolean();

            DataItemType scute =
                    new DataItemType(
                            scuteId,
                            Identifier.parse(texture),
                            Identifier.parse(armorTexture),
                            enabled
                    );

            if (json.has("display_name")) {
                scute.setDisplayName(
                        Component.literal(
                                json.get("display_name")
                                        .getAsString()
                        )
                );
            }

            LOADED_ITEMS.add(scute);
            System.out.println("LOADED_ITEMS: " + LOADED_ITEMS);

            System.out.println(
                    "[data_itemJsonLoader] Loaded data_item: "
                            + scuteId
            );

        } catch (Exception e) {
            System.err.println(
                    "[data_itemJsonLoader] Error loading data_item "
                            + resourceId
                            + ": "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    private static boolean isJsonDataFile(Identifier id) {
        String path = id.getPath();

        if (!path.endsWith(".json")) {
            return false;
        }

        return !path.endsWith("_schema.json")
                && !path.contains("/schemas/");
    }

    public static List<DataItemType> getLoadedItems() {
        return List.copyOf(LOADED_ITEMS);
    }

    private static void clear() {
        LOADED_ITEMS.clear();
    }
}