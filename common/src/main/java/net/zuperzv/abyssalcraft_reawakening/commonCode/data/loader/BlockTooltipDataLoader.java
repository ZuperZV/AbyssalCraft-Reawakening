package net.zuperzv.abyssalcraft_reawakening.commonCode.data.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.zuperzv.abyssalcraft_reawakening.services.Services;

import java.io.InputStreamReader;
import java.util.*;

public final class BlockTooltipDataLoader {

    private static final Gson GSON =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    private static final Map<Identifier, BlockTooltipDefinition> DEFINITIONS =
            new LinkedHashMap<>();

    private static final Map<Block, BlockTooltipDefinition> BY_BLOCK =
            new HashMap<>();

    private BlockTooltipDataLoader() {
    }

    public static void load() {

        MinecraftServer server = Services.SERVER.getCurrentServer();
        if (server == null) {
            clear();
            return;
        }

        loadFromResourceManager(server.getResourceManager());
    }

    public static void loadFromResourceManager(ResourceManager resourceManager) {
        clear();

        Map<Identifier, Resource> resources =
                resourceManager.listResources(
                        "block_tooltips",
                        path -> path.getPath().endsWith(".json")
                );

        for (Map.Entry<Identifier, Resource> resourceEntry : resources.entrySet()) {

            Identifier fileId = resourceEntry.getKey();

            try (InputStreamReader reader =
                         new InputStreamReader(resourceEntry.getValue().open())) {

                JsonObject json =
                        JsonParser.parseReader(reader).getAsJsonObject();

                BlockTooltipDefinition definition =
                        parseDefinition(json, fileId);

                if (definition == null) {
                    continue;
                }

                Optional<Block> optionalBlock =
                        BuiltInRegistries.BLOCK.getOptional(definition.block());

                if (optionalBlock.isEmpty()) {
                    System.err.println(
                            "[BlockTooltip] Unknown block: "
                                    + definition.block()
                                    + " from "
                                    + fileId
                    );
                    continue;
                }

                Block block = optionalBlock.get();

                DEFINITIONS.put(fileId, definition);
                BY_BLOCK.put(block, definition);

                System.out.println(
                        "[BlockTooltip] Loaded: "
                                + definition.block()
                                + " -> "
                                + definition.providers()
                );

            } catch (Exception e) {
                System.err.println(
                        "[BlockTooltip] Failed to load "
                                + fileId
                                + ": "
                                + e.getMessage()
                );
            }
        }

        System.out.println(
                "[BlockTooltip] Loaded "
                        + BY_BLOCK.size()
                        + " block tooltip definitions."
        );
    }

    private static BlockTooltipDefinition parseDefinition(
            JsonObject json,
            Identifier fileId
    ) {
        if (!json.has("block")) {
            System.err.println(
                    "[BlockTooltip] Missing 'block' in "
                            + fileId
            );
            return null;
        }

        Identifier blockId;

        try {
            blockId = Identifier.parse(
                    GsonHelper.getAsString(json, "block")
            );
        } catch (Exception e) {
            System.err.println(
                    "[BlockTooltip] Invalid block id in "
                            + fileId
            );
            return null;
        }

        List<TooltipProviderDefinition> providers =
                new ArrayList<>();

        if (json.has("providers")) {

            JsonArray array =
                    GsonHelper.getAsJsonArray(json, "providers");

            for (JsonElement element : array) {

                if (!element.isJsonPrimitive()) {
                    continue;
                }

                providers.add(
                        new TooltipProviderDefinition(
                                element.getAsString()
                        )
                );
            }
        }

        return new BlockTooltipDefinition(
                blockId,
                List.copyOf(providers)
        );
    }

    public static BlockTooltipDefinition get(Block block) {
        return BY_BLOCK.get(block);
    }

    public static BlockTooltipDefinition get(Identifier id) {
        return DEFINITIONS.get(id);
    }

    public static boolean has(Block block) {
        return BY_BLOCK.containsKey(block);
    }

    public static Collection<BlockTooltipDefinition> getAll() {
        return List.copyOf(DEFINITIONS.values());
    }

    public static void clear() {
        DEFINITIONS.clear();
        BY_BLOCK.clear();
    }

    public record BlockTooltipDefinition(
            Identifier block,
            List<TooltipProviderDefinition> providers
    ) {
    }

    public record TooltipProviderDefinition(
            String id
    ) {
    }
}