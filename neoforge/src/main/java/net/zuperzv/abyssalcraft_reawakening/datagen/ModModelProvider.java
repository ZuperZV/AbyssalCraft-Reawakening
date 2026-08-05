package net.zuperzv.abyssalcraft_reawakening.datagen;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
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
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.block.ModBlocks;
import net.zuperzv.abyssalcraft_reawakening.services.util.ModWoodTypes;
import net.zuperzv.abyssalcraft_reawakening.init.block.custom.PortalActivatorBlock;
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
        createPortalActivator(blockModels, ModBlocks.ABYSSAL_WASTELAND_ACTIVATOR.block().get());

        //Plants
        createCrossBlockWithItem(blockModels, itemModels, ModBlocks.CORALIUM_TENDRILS.block().get(), BlockModelGenerators.PlantType.NOT_TINTED);
        
        //Tree
            //WITHERWOOD
        generateWoodSet(
                blockModels,
                ModBlocks.WITHERWOOD_LOG.block().get(),
                ModBlocks.WITHERWOOD_WOOD.block().get()
        );

        generateWoodSet(
                blockModels,
                ModBlocks.STRIPPED_WITHERWOOD_LOG.block().get(),
                ModBlocks.STRIPPED_WITHERWOOD_WOOD.block().get()
        );

        generateLeaves(
                blockModels,
                ModBlocks.WITHERWOOD_LEAVES.block().get(),
                -12012255
        );

        blockModels.family(ModBlocks.WITHERWOOD_PLANKS.block().get())
                .stairs(ModBlocks.WITHERWOOD_STAIRS.block().get())
                .slab(ModBlocks.WITHERWOOD_SLAB.block().get())
                .pressurePlate(ModBlocks.WITHERWOOD_PRESSURE_PLATE.block().get())
                .button(ModBlocks.WITHERWOOD_BUTTON.block().get())
                .fence(ModBlocks.WITHERWOOD_FENCE.block().get())
                .fenceGate(ModBlocks.WITHERWOOD_FENCE_GATE.block().get())
                .door(ModBlocks.WITHERWOOD_DOOR.block().get())
                .trapdoor(ModBlocks.WITHERWOOD_TRAPDOOR.block().get());

        generatedBlocks.add(ModBlocks.WITHERWOOD_PLANKS.block().get());
        generatedBlocks.add(ModBlocks.WITHERWOOD_STAIRS.block().get());
        generatedBlocks.add(ModBlocks.WITHERWOOD_SLAB.block().get());
        generatedBlocks.add(ModBlocks.WITHERWOOD_PRESSURE_PLATE.block().get());
        generatedBlocks.add(ModBlocks.WITHERWOOD_BUTTON.block().get());
        generatedBlocks.add(ModBlocks.WITHERWOOD_FENCE.block().get());
        generatedBlocks.add(ModBlocks.WITHERWOOD_FENCE_GATE.block().get());
        generatedBlocks.add(ModBlocks.WITHERWOOD_DOOR.block().get());
        generatedBlocks.add(ModBlocks.WITHERWOOD_TRAPDOOR.block().get());

        createShelf(blockModels, ModBlocks.WITHERWOOD_SHELF.block().get(), ModBlocks.STRIPPED_WITHERWOOD_LOG.block().get());

        addSigns(
                blockModels,
                ModBlocks.WITHERWOOD_PLANKS.block().get(),
                ModBlocks.WITHERWOOD_SIGN.get(),
                ModBlocks.WITHERWOOD_WALL_SIGN.get(),
                ModBlocks.WITHERWOOD_HANGING_SIGN.get(),
                ModBlocks.WITHERWOOD_WALL_HANGING_SIGN.get()
        );
        itemModels.generateFlatItem(ModItems.WITHERWOOD_SIGN.get(), ModelTemplates.FLAT_ITEM);

        /*
        generateSapling(
                blockModels,
                ModBlocks.WITHERWOOD_SAPLING.block().get(),
                ModBlocks.POTTED_WITHERWOOD_SAPLING.block().get()
        );
         */

        //Not Generated
        //generatedItems.add(ModItems.ABYSSALNITE_SWORD.get());
        //declareCustomModelItem(itemModels, ModItems.ABYSSALNITE_SWORD.get());

        for (Item item : getModItems()) {
            if (!(item instanceof BlockItem) &&
                    !generatedItems.contains(item)) {
                generateItem(itemModels, item);
            }
        }

        for (Block block : getModBlocks()) {
            if (!generatedBlocks.contains(block)) {
                generateCubeBlock(blockModels, block);
            }
        }
    }

    private void addSign(
            BlockModelGenerators blockModels,
            Block particleBlock,
            Block standingSign,
            Block wallSign
    ) {
        generatedBlocks.add(standingSign);
        generatedBlocks.add(wallSign);

        TextureMapping textureMapping = TextureMapping.cube(particleBlock);

        MultiVariant model = BlockModelGenerators.plainVariant(
                ModelTemplates.PARTICLE_ONLY.create(
                        standingSign,
                        textureMapping,
                        blockModels.modelOutput
                )
        );

        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(standingSign, model)
        );

        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(wallSign, model)
        );
    }

    private void addSigns(
            BlockModelGenerators blockModels,
            Block particleBlock,
            Block standingSign,
            Block wallSign,
            Block hangingSign,
            Block wallHangingSign
    ) {
        addSign(blockModels, particleBlock, standingSign, wallSign);

        generatedBlocks.add(hangingSign);
        generatedBlocks.add(wallHangingSign);

        blockModels.createHangingSign(
                particleBlock,
                hangingSign,
                wallHangingSign
        );
    }

    private void createShelf(
            BlockModelGenerators blockModels,
            Block block,
            Block strippedLog
    ) {
        generatedBlocks.add(block);

        blockModels.createShelf(block, strippedLog);
    }

    private void generateWoodSet(
            BlockModelGenerators blockModels,
            Block log,
            Block wood
    ) {
        generatedBlocks.add(log);
        generatedBlocks.add(wood);

        blockModels.woodProvider(log)
                .logWithHorizontal(log)
                .wood(wood);
    }

    private void generateLeaves(
            BlockModelGenerators blockModels,
            Block leaves,
            int color
    ) {
        generatedBlocks.add(leaves);

        blockModels.createTintedLeaves(
                leaves,
                TexturedModel.LEAVES,
                color
        );
    }

    private void generateSapling(
            BlockModelGenerators blockModels,
            Block sapling,
            Block pottedSapling
    ) {
        generatedBlocks.add(sapling);
        generatedBlocks.add(pottedSapling);

        blockModels.createPlantWithDefaultItem(
                sapling,
                pottedSapling,
                BlockModelGenerators.PlantType.TINTED
        );

        generatedItems.add(sapling.asItem());
    }

    public void createCrossBlockWithItem(
            BlockModelGenerators blockModels,
            ItemModelGenerators itemModels,
            Block block,
            BlockModelGenerators.PlantType plantType
    ) {
        createCrossBlock(blockModels, block, plantType);

        Item item = block.asItem();
        generatedItems.add(item);

        Identifier itemModel = itemModels.createFlatItemModel(
                item,
                ModelTemplates.FLAT_ITEM
        );

        itemModels.itemModelOutput.accept(
                item,
                ItemModelUtils.plainModel(itemModel)
        );
    }

    public void createCrossBlock(
            BlockModelGenerators blockModels,
            Block block,
            BlockModelGenerators.PlantType plantType
    ) {
        generatedBlocks.add(block);

        TextureMapping textures = plantType.getTextureMapping(block);

        MultiVariant model = plainVariant(
                plantType.getCross().create(
                        block,
                        textures,
                        blockModels.modelOutput
                )
        );

        blockModels.blockStateOutput.accept(
                createSimpleBlock(block, model)
        );
    }

    //TO-DO make the models
    private void createBooleanBlock(
            BlockModelGenerators blockModels,
            Block block,
            BooleanProperty property,
            String falseSuffix,
            String trueSuffix
    ) {
        generatedBlocks.add(block);

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(
                                PropertyDispatch.initial(property)
                                        .select(false,
                                                plainVariant(ModelLocationUtils.getModelLocation(block, falseSuffix)))
                                        .select(true,
                                                plainVariant(ModelLocationUtils.getModelLocation(block, trueSuffix)))
                        )
        );
    }

    private void createPortalActivator(BlockModelGenerators blockModels, Block block) {
        generatedBlocks.add(block);

        // No key
        TextureMapping noKeyTextures = new TextureMapping()
                .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_no_key"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top_no_key"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(ModBlocks.CORALIUM_BRICKS.block().get()));

        // With key
        TextureMapping withKeyTextures = new TextureMapping()
                .put(TextureSlot.FRONT, TextureMapping.getBlockTexture(block, "_with_key"))
                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top_with_key"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(ModBlocks.CORALIUM_BRICKS.block().get()));


        Identifier noKeyModel = ModelTemplates.CUBE_BOTTOM_TOP.create(
                block,
                noKeyTextures,
                blockModels.modelOutput
        );

        Identifier withKeyModel = ModelTemplates.CUBE_BOTTOM_TOP.createWithSuffix(
                block,
                "_with_key",
                withKeyTextures,
                blockModels.modelOutput
        );


        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(
                                PropertyDispatch.initial(PortalActivatorBlock.ON)
                                        .select(
                                                false,
                                                plainVariant(noKeyModel)
                                        )
                                        .select(
                                                true,
                                                plainVariant(withKeyModel)
                                        )
                        )
        );

        blockModels.registerSimpleItemModel(block, noKeyModel);
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
