package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ConditionalItemModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.data.DyedColorTintSource;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModArmorMaterials;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.services.NeoForgeRegistryHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;
import static net.minecraft.client.data.models.ItemModelGenerators.createFlatModelDispatch;

public class ModModelProvider extends ModelProvider {
    private final Set<Item> generatedItems = new HashSet<>();
    private final Set<Block> generatedBlocks = new HashSet<>();

    public ModModelProvider(PackOutput output) {
        super(output, Constants.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        generateItemWithTintedOverlay(itemModels, ModItems.NECRONOMICON.get(), new DyedColorTintSource());

        //Sword in hand
        generateInHand(itemModels, ModItems.ABYSSALNITE_SWORD.get(), ModelTemplates.FLAT_ITEM, SWORD_IN_HAND);

        //essence
        generateItem(itemModels, ModItems.SHADOW_GEM.get(), "_gray", ModDataComponentTypes.GRAYSCALE.get());
        generateItem(itemModels, ModItems.ABYSSAL_WASTELAND_ESSENCE.get(), "_gray", ModDataComponentTypes.GRAYSCALE.get());
        generateItem(itemModels, ModItems.DREADLANDS_ESSENCE.get(), "_gray", ModDataComponentTypes.GRAYSCALE.get());
        generateItem(itemModels, ModItems.OMOTHOL_ESSENCE.get(), "_gray", ModDataComponentTypes.GRAYSCALE.get());

        //Staff of rendering
        generateInHandWithTintedOverlay(itemModels, ModItems.STAFF_OF_RENDING.get(), new DyedColorTintSource(), FLAT_HANDHELD_TWO_LAYER, FLAT_HANDHELD_IN_HAND_TWO_LAYER);
        generateInHandWithTintedOverlay(itemModels, ModItems.ABYSSAL_WASTELAND_STAFF_OF_RENDING.get(), new DyedColorTintSource(), FLAT_HANDHELD_TWO_LAYER, FLAT_HANDHELD_IN_HAND_TWO_LAYER);
        generateInHandWithTintedOverlay(itemModels, ModItems.DREADLANDS_STAFF_OF_RENDING.get(), new DyedColorTintSource(), FLAT_HANDHELD_TWO_LAYER, FLAT_HANDHELD_IN_HAND_TWO_LAYER);
        generateInHandWithTintedOverlay(itemModels, ModItems.OMOTHOL_STAFF_OF_RENDING.get(), new DyedColorTintSource(), FLAT_HANDHELD_TWO_LAYER, FLAT_HANDHELD_IN_HAND_TWO_LAYER);

        //Rendering Items
        generateItem(itemModels, ModItems.POTENTIAL_ENERGY.get(), "_gray", ModDataComponentTypes.GRAYSCALE.get());

        //Altar
        generateBlockFromParent(blockModels, ModBlocks.STONE_RITUAL_ALTAR.block().get(), "ritual_altar", List.of(TextureSlot.create("0")));
        generateBlockFromParent(blockModels, ModBlocks.STONE_RITUAL_PEDESTAL.block().get(), "ritual_pedestal", List.of(TextureSlot.create("1"), TextureSlot.create("2")));

        //PortalBlock
        createNetherPortalBlock(blockModels, ModBlocks.ABYSSAL_WASTELAND_PORTAL_BLOCK.block().get());

        //Not Generated
        //generatedItems.add(ModItems.ABYSSALNITE_SWORD.get());
        //declareCustomModelItem(itemModels, ModItems.ABYSSALNITE_SWORD.get());

        for (Item item : getModItems()) {
            if (!(item instanceof BlockItem) && !generatedItems.contains(item)) {
                generateItem(itemModels, item);
            }
        }

        for (Block block : getModBlocks()) {
            if (!generatedBlocks.contains(block)) {
                generateCubeBlock(blockModels, block);
            }
        }
    }

    private void generateItem(ItemModelGenerators itemModels, Item item) {
        generatedItems.add(item);

        if (item.getDescriptionId().toLowerCase().contains("spear")) {
            itemModels.generateSpear(item);
            return;
        } else if (isTool(item)) {
            itemModels.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM);
            return;
        } else if (generateTrimmableItem(itemModels, item, false)) {
            return;
        }

        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    private void generateItem(ItemModelGenerators itemModels, Item item, String suffix, DataComponentType<?> componentType) {
        generateItem(itemModels, item, suffix, componentType, ModelTemplates.FLAT_ITEM);
    }

    private void declareCustomModelItem(ItemModelGenerators itemModels, Item item) {
        generatedItems.add(item);
        itemModels.declareCustomModelItem(item);
    }

    private void generateItem(ItemModelGenerators itemModels, Item item, String suffix, DataComponentType<?> componentType, ModelTemplate modelTemplate) {
        generatedItems.add(item);

        ItemModel.Unbaked unbakedDataTablet = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, modelTemplate));
        ItemModel.Unbaked unbakedDataTabletOn = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, suffix, modelTemplate));

        itemModels.itemModelOutput.register(item,
                new ClientItem(new ConditionalItemModel.Unbaked(Optional.empty(), new HasComponent(componentType, false),
                        unbakedDataTabletOn, unbakedDataTablet), new ClientItem.Properties(false, false, 1f)));
    }

    private void generateTrimmableItem(ItemModelGenerators itemModels, Item item, ResourceKey<EquipmentAsset> equipmentAssetId, Identifier slotTrimPrefix, boolean hasDyedLayer) {
        generatedItems.add(item);
        itemModels.generateTrimmableItem(item, equipmentAssetId, slotTrimPrefix, hasDyedLayer);
    }

    private void generateItemWithTintedBaseLayer(ItemModelGenerators itemModels, Item item, int defaultColor) {
        generatedItems.add(item);
        itemModels.generateItemWithTintedBaseLayer(item, defaultColor);
    }

    public void generateItemWithTintedOverlay(ItemModelGenerators itemModels, Item item, ItemTintSource overlayTint) {
        this.generateItemWithTintedOverlay(itemModels, item, "_overlay", overlayTint);
    }

    public void generateItemWithTintedOverlay(ItemModelGenerators itemModels, Item item, ItemTintSource overlayTint, ModelTemplate modelTemplate) {
        this.generateItemWithTintedOverlay(itemModels, item, "_overlay", overlayTint, modelTemplate);
    }

    public void generateItemWithTintedOverlay(ItemModelGenerators itemModels, Item item, String overlaySuffix, ItemTintSource overlayTint) {
        generatedItems.add(item);

        Identifier model = itemModels.generateLayeredItem(
                item,
                TextureMapping.getItemTexture(item),
                TextureMapping.getItemTexture(item, overlaySuffix)
        );

        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.tintedModel(
                        model,
                        new ItemTintSource[] {
                                itemModels.BLANK_LAYER,
                                overlayTint
                        }
                )
        );
    }

    public void generateInHandWithTintedOverlay(
            ItemModelGenerators itemModels,
            Item item,
            ItemTintSource overlayTint,
            ModelTemplate modelTemplate,
            ModelTemplate modelTemplateInHand
    ) {
        generatedItems.add(item);

        Identifier flatModel = modelTemplate.create(
                ModelLocationUtils.getModelLocation(item),
                TextureMapping.layered(
                        TextureMapping.getItemTexture(item),
                        TextureMapping.getItemTexture(item, "_overlay")
                ),
                itemModels.modelOutput
        );

        Identifier inHandModel = modelTemplateInHand.create(
                ModelLocationUtils.getModelLocation(item, "_in_hand"),
                TextureMapping.layered(
                        TextureMapping.getItemTexture(item, "_in_hand"),
                        TextureMapping.getItemTexture(item, "_in_hand_overlay")
                ),
                itemModels.modelOutput
        );


        ItemModel.Unbaked flat = ItemModelUtils.tintedModel(
                flatModel,
                new ItemTintSource[]{
                        itemModels.BLANK_LAYER,
                        overlayTint
                }
        );

        ItemModel.Unbaked inHand = ItemModelUtils.tintedModel(
                inHandModel,
                new ItemTintSource[]{
                        itemModels.BLANK_LAYER,
                        overlayTint
                }
        );


        itemModels.itemModelOutput.accept(
                item,
                createFlatModelDispatch(flat, inHand),
                new ClientItem.Properties(true, false, 1.95F)
        );
    }

    public Identifier generateLayeredItem(ItemModelGenerators itemModels, Item target, Material layer0, Material layer1) {
        generatedItems.add(target);

        return ModelTemplates.TWO_LAYERED_ITEM.create(target, TextureMapping.layered(layer0, layer1), itemModels.modelOutput);
    }

    public void generateItemWithTintedOverlay(ItemModelGenerators itemModels, Item item, String overlaySuffix, ItemTintSource overlayTint, ModelTemplate modelTemplate) {
        generatedItems.add(item);

        Identifier model = modelTemplate.create(
                ModelLocationUtils.getModelLocation(item),
                TextureMapping.layered(
                        TextureMapping.getItemTexture(item),
                        TextureMapping.getItemTexture(item, overlaySuffix)
                ),
                itemModels.modelOutput
        );

        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.tintedModel(
                        model,
                        new ItemTintSource[] {
                                itemModels.BLANK_LAYER,
                                overlayTint
                        }
                )
        );
    }

    public void generateSpear(ItemModelGenerators itemModels, Item item) {
        generatedItems.add(item);

        ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(
                ModelTemplates.SPEAR_IN_HAND.create(item, TextureMapping.layer0(TextureMapping.getItemTexture(item, "_in_hand")), itemModels.modelOutput)
        );
        itemModels.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel), new ClientItem.Properties(true, false, 1.95F));
    }

    public void generateInHand(ItemModelGenerators itemModels, Item item) {
        generateInHand(itemModels, item, ModelTemplates.FLAT_ITEM, ModelTemplates.SPEAR_IN_HAND);
    }

    public void generateInHand(ItemModelGenerators itemModels, Item item, ModelTemplate modelTemplate, ModelTemplate modelTemplateInHand) {
        generatedItems.add(item);

        ItemModel.Unbaked flatModel = ItemModelUtils.plainModel(itemModels.createFlatItemModel(item, modelTemplate));
        ItemModel.Unbaked inHandModel = ItemModelUtils.plainModel(
                modelTemplateInHand.create(item, TextureMapping.layer0(TextureMapping.getItemTexture(item, "_in_hand")), itemModels.modelOutput)
        );
        itemModels.itemModelOutput.accept(item, createFlatModelDispatch(flatModel, inHandModel), new ClientItem.Properties(true, false, 1.95F));
    }

    private boolean generateTrimmableItem(ItemModelGenerators itemModels, Item item, boolean hasDyedLayer) {
        ResourceKey<EquipmentAsset> equipmentAssetId = getEquipmentAssetForItem(item);
        if (equipmentAssetId == null) return false;

        if (item.getDescriptionId().toLowerCase().contains("helmet")) {
            generateTrimmableItem(itemModels, item, equipmentAssetId, ItemModelGenerators.TRIM_PREFIX_HELMET, hasDyedLayer);
            return true;
        } else if  (item.getDescriptionId().toLowerCase().contains("chestplate")) {
            generateTrimmableItem(itemModels, item, equipmentAssetId, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, hasDyedLayer);
            return true;
        } else if  (item.getDescriptionId().toLowerCase().contains("leggings")) {
            generateTrimmableItem(itemModels, item, equipmentAssetId, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, hasDyedLayer);
            return true;
        } else if  (item.getDescriptionId().toLowerCase().contains("boots")) {
            generateTrimmableItem(itemModels, item, equipmentAssetId, ItemModelGenerators.TRIM_PREFIX_BOOTS, hasDyedLayer);
            return true;
        }
        return false;
    }

    private ResourceKey<EquipmentAsset> getEquipmentAssetForItem(Item item) {
        String id = item.getDescriptionId().toLowerCase();

        if (id.contains("abyssalnite")) {
            return ModArmorMaterials.ABYSSALNITE_ASSET;
        } else if (id.contains("refined_coralium")) {
            return ModArmorMaterials.REFINED_CORALIUM_ASSET;
        } else if (id.contains("plated_coralium")) {
            return ModArmorMaterials.PLATED_CORALIUM_ASSET;
        } else if (id.contains("of_the_depths")) {
            return ModArmorMaterials.OF_THE_DEPTHS_ASSET;
        } else if (id.contains("dreadium_samurai")) {
            return ModArmorMaterials.DREADIUM_SAMURAI_ASSET;
        } else if (id.contains("dreadium")) {
            return ModArmorMaterials.DREADIUM_ASSET;
        } else if (id.contains("ethaxium")) {
            return ModArmorMaterials.ETHAXIUM_ASSET;
        }
        return null;
    }

    private boolean isTool(Item item) {
        return item.getDescriptionId().toLowerCase().contains("sword")
                || item.getDescriptionId().toLowerCase().contains("pickaxe")
                || item instanceof AxeItem
                || item instanceof ShovelItem
                || item instanceof HoeItem;
    }

    private void generateBlockFromParent(
            BlockModelGenerators blockModels,
            Block block,
            String parentModel,
            List<TextureSlot> slots
    ) {
        if (slots.isEmpty()) {
            throw new IllegalArgumentException("At least one TextureSlot is required.");
        }

        generatedBlocks.add(block);

        ModelTemplate template = new ModelTemplate(
                Optional.of(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/" + parentModel)),
                Optional.empty(),
                slots.toArray(new TextureSlot[0])
        );

        TextureMapping textures = new TextureMapping();

        for (TextureSlot slot : slots) {
            textures.put(slot, TextureMapping.getBlockTexture(block));
        }

        textures.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block));

        Identifier model = template.create(block, textures, blockModels.modelOutput);

        blockModels.blockStateOutput.accept(
                createSimpleBlock(block, plainVariant(model))
        );
    }

    public void createNetherPortalBlock(BlockModelGenerators blockModels, Block block) {
        generatedBlocks.add(block);

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(
                                PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_AXIS)
                                        .select(
                                                Direction.Axis.X,
                                                plainVariant(ModelLocationUtils.getModelLocation(block, "_ns"))
                                        )
                                        .select(
                                                Direction.Axis.Z,
                                                plainVariant(ModelLocationUtils.getModelLocation(block, "_ew"))
                                        )
                        )
        );
    }

    private void generateCubeBlock(BlockModelGenerators blockModels, Block block) {
        generatedBlocks.add(block);
        blockModels.createTrivialCube(block);
    }

    private Iterable<Block> getModBlocks() {
        return NeoForgeRegistryHelper.BLOCKS.getEntries()
                .stream()
                .map(entry -> (Block) entry.value())
                .toList();
    }

    private Iterable<Item> getModItems() {
        return NeoForgeRegistryHelper.ITEMS.getEntries()
                .stream()
                .map(entry -> (Item) entry.value())
                .toList();
    }

    //ModelTemplates
    public static final ModelTemplate FLAT_HANDHELD_TWO_LAYER =
            new ModelTemplate(
                    Optional.of(Identifier.withDefaultNamespace("item/handheld")),
                    Optional.empty(),
                    TextureSlot.LAYER0,
                    TextureSlot.LAYER1
            );
    public static final ModelTemplate FLAT_HANDHELD_IN_HAND_TWO_LAYER =
            new ModelTemplate(
                    Optional.of(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/in_hand")),
                    Optional.empty(),
                    TextureSlot.LAYER0,
                    TextureSlot.LAYER1
            );
    public static final ModelTemplate SWORD_IN_HAND;
    static {
        SWORD_IN_HAND = createItem("in_hand", "in_hand", TextureSlot.LAYER0);
    }

    public static ModelTemplate createItem(String id, String suffix, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(decorateItemModelLocation(id)), Optional.of(suffix), slots);
    }

    public static Identifier decorateItemModelLocation(final String id) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "item/" + id);
    }
}
