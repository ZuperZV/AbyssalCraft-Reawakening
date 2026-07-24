package net.zuperzv.abyssalcraft_reawakening.init.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.advancements.AdvancementTab;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.*;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.RecipeType;
import net.zuperzv.abyssalcraft_reawakening.Constants;
import net.zuperzv.abyssalcraft_reawakening.init.item.ModItems;
import net.zuperzv.abyssalcraft_reawakening.init.component.CodexTierData;
import net.zuperzv.abyssalcraft_reawakening.init.component.ModDataComponentTypes;
import net.zuperzv.abyssalcraft_reawakening.init.data.CodexBookmarksData;
import net.zuperzv.abyssalcraft_reawakening.init.data.CodexDataLoader;
import net.zuperzv.abyssalcraft_reawakening.init.mixin.AdvancementTabMixin;
import net.zuperzv.abyssalcraft_reawakening.init.mixin.AdvancementsScreenMixin;
import net.zuperzv.abyssalcraft_reawakening.init.network.SetBookmarksPacket;
import net.zuperzv.abyssalcraft_reawakening.init.screen.Helpers.*;
import net.zuperzv.abyssalcraft_reawakening.services.Services;
import net.zuperzv.abyssalcraft_reawakening.services.util.MouseUtil;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NecronomiconScreen extends AbstractContainerScreen<NecronomiconMenu> {
    public NecronomiconEntry selectedEntry = null;
    public int selectedPage = 0;
    private PageButton forwardButton;
    private PageButton backWardButton;
    private BackPageButton backButton;
    private Button editButton;
    private final boolean playTurnSound;
    public List<NecronomiconEntry> entryList = List.of();
    private ItemStack hoveredStack = ItemStack.EMPTY;
    private final List<ClickableItemRegion> clickableItemRegions = new ArrayList<>();
    private final List<TextLinkRegion> textLinkRegions = new ArrayList<>();
    /*
    private final List<RenderedJeiLayout> renderedJeiLayouts = new ArrayList<>();
    private final Map<String, Optional<IRecipeLayoutDrawable<?>>> jeiLayoutCache = new HashMap<>();
     */

    public static final Identifier BOOK_TEXTURE =
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/book.png");
    public static final Identifier BOOK_TEXTURE_GRAY =
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/book_gray.png");
    private static final Identifier ARROW_TEXTURE =
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/arrow.png");
    private int scrollOffset = 0;

    private float easedIconX = 0f;
    private float easedIconY = 0f;

    private int FabricX = 0; //31
    private int FabricY = 0; //19

    public static final int Z_TOOLTIP = 300;
    public static final int Z_BOOK_EDGE = 200;
    public static final int Z_BOOKMARK_ITEM = 100;

    private final List<String> playerBookmarks = new ArrayList<>();
    private final List<BookmarkButton> bookmarkButtons = new ArrayList<>();
    private BookmarkButton setterButton = null;

    private EditBox searchBox;
    private boolean mouseWasOverSearch = false;
    private List<NecronomiconEntry> searchResults = new ArrayList<>();

    public List<NecronomiconCategory> categories = new ArrayList<>();
    public NecronomiconCategory selectedCategory = null;
    public boolean isInCategoryView = true;

    private AdvancementsScreen advancementsScreen;
    public boolean showAdvancement = true;
    public int advancementX = 30;
    public int advancementY = 18;

    private final int SEARCH_TEX_X_P = 158;
    private final int SEARCH_TEX_Y_P = -16;
    private final int SEARCH_TEX_W_P = 83;
    private final int SEARCH_TEX_H_P = 16;

    private final int SEARCH_FIELD_X_P = 177;
    private final int SEARCH_FIELD_Y_P = -10;
    private final int SEARCH_FIELD_W_P = 56;
    private final int SEARCH_FIELD_H_P = 11;

    private static final int SLOT_WIDTH = 97;
    private static final int SLOT_HEIGHT = 20;
    private static final int SLOT_SPACING = 2;
    private static final int ITEM_SIZE = 16;
    private static final int ITEM_PADDING = 2;
    private static final int LINE_HEIGHT = 10;
    private static final int TEXT_COLOR = 0x282828;
    private static final int LINK_COLOR = 0x1f1f1f;
    private static final int LINK_COLOR_LOCKED = 0x141414;
    private static final int LINK_COLOR_HOVER = 0x3a3a3a;
    private static final int LINK_UNDERLINE_OFFSET = 9;
    private static final int SLOT_BORDER_COLOR = 0xFF1B1B1B;

    private int currentLayer = 0;

    private static final Pattern LINK_PATTERN = Pattern.compile("\\[\\[(.+?)]]");

    public NecronomiconScreen(NecronomiconMenu menu, net.minecraft.world.entity.player.Inventory inv, Component title) {
        super(menu, inv, title, 248, 180);
        this.playTurnSound = true;

        this.entryList = List.copyOf(CodexDataLoader.getAllEntries());
        if (!entryList.isEmpty()) {
            this.selectedEntry = entryList.get(0);
        }
    }

    public boolean setPage(int p_98276_) {
        int i = Mth.clamp(p_98276_, 0, this.selectedEntry.right_side.size() - 1);
        if (i != this.selectedPage) {
            this.selectedPage = i;
            return true;
        } else {
            return false;
        }
    }

    protected boolean forcePage(int p_98295_) {
        return this.setPage(p_98295_);
    }

    @Override
    protected void init() {
        this.categories = CodexDataLoader.getAllCategories();
        this.entryList = List.copyOf(CodexDataLoader.getAllEntries());
        this.selectedPage = 0;

        this.playerBookmarks.clear();
        this.playerBookmarks.addAll(CodexBookmarksData.getBookmarks(this.minecraft.player));
        this.createBookmarkButtons();

        int searchX = SEARCH_FIELD_X_P;
        int searchY = SEARCH_FIELD_Y_P;

        this.searchBox = new EditBox(this.font, searchX, searchY, SEARCH_FIELD_W_P, SEARCH_FIELD_H_P, Component.literal("Search"));
        this.searchBox.setBordered(false);
        this.searchBox.setVisible(false);
        this.searchBox.setTextColor(0x000000);
        this.searchBox.setTextShadow(false);
        this.searchBox.setMaxLength(30);
        this.searchBox.setResponder(this::updateSearchResults);

        this.addRenderableWidget(searchBox);

        this.createMenuControls();
        this.createPageControlButtons();

        if (showAdvancement) {
            ClientAdvancements clientAdvancements = Minecraft.getInstance().player.connection.getAdvancements();
            advancementsScreen = new AdvancementsScreen(clientAdvancements, null);
            advancementsScreen.init(this.width, this.height);

            Identifier netherRootId = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "get_flower");
            var netherNode = clientAdvancements.getTree().get(netherRootId);

            if (netherNode != null) {
                try {
                    var tabsField = AdvancementsScreen.class.getDeclaredField("tabs");
                    var selectedField = AdvancementsScreen.class.getDeclaredField("selectedTab");
                    tabsField.setAccessible(true);
                    selectedField.setAccessible(true);

                    @SuppressWarnings("unchecked")
                    Map<AdvancementHolder, AdvancementTab> tabs =
                            (Map<net.minecraft.advancements.AdvancementHolder, AdvancementTab>) tabsField.get(advancementsScreen);

                    AdvancementTab createdTab = tabs.get(netherNode.holder());
                    if (createdTab == null) {
                        createdTab = AdvancementTab.create(Minecraft.getInstance(), advancementsScreen, tabs.size(), netherNode);
                        tabs.put(netherNode.holder(), createdTab);
                    }

                    selectedField.set(advancementsScreen, createdTab);

                    clientAdvancements.setSelectedTab(netherNode.holder(), true);

                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("netherRootId = null");
            }
        }
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    protected void createMenuControls() {
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).bounds(this.width / 2 - 100, (height - imageHeight) / 2 + 194, 200, 20).build());

        if (canEditCodex()) {
            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;
            /*
            this.editButton = this.addRenderableWidget(Button.builder(Component.literal("Edit"), button -> {
                Services.NETWORK.sendToServer(new RequestCodexEditorPacket());
            }).bounds(x + imageWidth - 52 - 20, y + imageHeight + 4 + 34, 48, 20).build());
             */
        }
    }

    protected void createPageControlButtons() {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        this.forwardButton = this.addRenderableWidget(new PageButton(x + 203, y + 156, true, button -> this.pageForward(), this.playTurnSound));
        this.backWardButton = this.addRenderableWidget(new PageButton(x + 23, y + 156, false, button -> this.pageWardBack(), this.playTurnSound));
        this.backButton = this.addRenderableWidget(new BackPageButton(x + 112, y + 180, button -> this.pageBack(), this.playTurnSound));
        this.updateButtonVisibility();
    }

    protected void pageBack() {
        if (this.selectedEntry != null) {
            this.scrollOffset = 0;
            this.selectedPage = 0;

            this.isInCategoryView = false;
            this.selectedEntry = null;

            this.updateButtonVisibility();

            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
            return;
        }

        if (this.selectedCategory != null) {
            this.isInCategoryView = true;
            this.selectedCategory = null;
            this.scrollOffset = 0;
            this.selectedPage = 0;

            this.updateButtonVisibility();

            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
            return;
        }
    }

    protected void pageWardBack() {

        if (selectedEntry == null) return;

        if (this.selectedPage > 0) {
            this.selectedPage--;
        } else {

            List<NecronomiconEntry> list = getAvailableEntries();
            int index = list.indexOf(selectedEntry);

            if (index > 0) {

                NecronomiconEntry previousEntry = list.get(index - 1);

                CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
                int playerTier = tierData != null ? tierData.getTier() : 0;

                int previousTier = getTierForEntry(previousEntry);

                if (previousTier > playerTier) {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
                    return;
                }

                this.selectedEntry = previousEntry;
                this.selectedPage = Math.max(0, previousEntry.right_side.size() - 1);
                this.isInCategoryView = false;
            }
        }

        scrollOffset = 0;
        this.updateButtonVisibility();
    }

    protected void pageForward() {

        if (selectedEntry == null) return;

        if (this.selectedPage < this.selectedEntry.right_side.size() - 1) {
            this.selectedPage++;
        } else {

            List<NecronomiconEntry> list = getAvailableEntries();
            int index = list.indexOf(selectedEntry);

            if (index < list.size() - 1) {

                NecronomiconEntry nextEntry = list.get(index + 1);

                CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
                int playerTier = tierData != null ? tierData.getTier() : 0;

                int nextTier = getTierForEntry(nextEntry);

                if (nextTier > playerTier) {
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
                    return;
                }

                this.selectedEntry = nextEntry;
                this.selectedPage = 0;
                this.isInCategoryView = false;
            }
        }

        scrollOffset = 0;
        this.updateButtonVisibility();
    }

    public void updateButtonVisibility() {

        this.backButton.visible = !isInCategoryView;

        if (selectedEntry == null || isInCategoryView) {
            this.forwardButton.visible = false;
            this.backWardButton.visible = false;
            return;
        }

        int entryIndex = entryList.indexOf(selectedEntry);

        if (entryIndex == -1) {
            this.forwardButton.visible = false;
            this.backWardButton.visible = false;
            return;
        }

        this.backWardButton.visible =
                selectedPage > 0 || entryIndex > 0;

        this.forwardButton.visible =
                selectedPage < selectedEntry.right_side.size() - 1 ||
                        entryIndex < entryList.size() - 1;
    }

    /*
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else {
            var runtime = JEIPlugin.getJeiRuntime();
            if (runtime != null && !hoveredStack.isEmpty()) {
                var keyMappings = runtime.getKeyMappings();
                InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);

                if (keyMappings.getShowRecipe().isActiveAndMatches(key)) {
                    openJeiForStack(hoveredStack, false); // recipes (R)
                }
                if (keyMappings.getShowUses().isActiveAndMatches(key)) {
                    openJeiForStack(hoveredStack, true); // uses (U)
                }
            }

            switch (keyCode) {
                case 266, GLFW.GLFW_KEY_LEFT:
                    this.backWardButton.onPress();
                    return true;
                case 267, GLFW.GLFW_KEY_RIGHT:
                    this.forwardButton.onPress();
                    return true;
                case GLFW.GLFW_KEY_UP, GLFW.GLFW_KEY_DOWN:
                    this.backButton.onPress();
                    return true;
                default:
                    return false;
            }
        }
    }
     */

    private int getCurrentPageContentHeight(int areaW) {
        if (selectedEntry == null || selectedEntry.right_side == null || selectedEntry.right_side.isEmpty()) return 0;
        if (selectedPage < 0 || selectedPage >= selectedEntry.right_side.size()) return 0;

        NecronomiconPage page = selectedEntry.right_side.get(selectedPage);
        if (page == null || page.modules == null) return 0;

        int drawY = 0;

        for (CodexModule module : page.modules) {
            if ("text".equals(module.module_type)) {
                drawY += measureTextHeight(getModuleText(module), areaW - 4);
                continue;
            }

            if ("recipe".equals(module.module_type) || "furnace_recipe".equals(module.module_type)) {
                drawY += getRecipeModuleHeight(module, areaW - 4);
                continue;
            }

            drawY += LINE_HEIGHT + 2;
        }
        return drawY;
    }

    private int getCategoryOverviewContentHeight() {
        int spacingY = 50;
        return categories.size() * spacingY;
    }

    private int getCategoryEntriesContentHeight() {
        if (selectedCategory == null) return 0;
        int spacingY = 20;
        CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
        int playerTier = tierData != null ? tierData.getTier() : 0;

        int count = 0;
        Pattern tierPattern = Pattern.compile("tier_(\\d+)");
        for (NecronomiconEntry entry : selectedCategory.entries) {
            Matcher matcher = tierPattern.matcher(entry.id);
            int entryTier = matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
            if (entryTier <= playerTier) count++;
        }
        return count * spacingY;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int areaX = x + 138;
        int areaY = y + 44;
        int areaW = 96;
        int areaH = 112;

        if (MouseUtil.isMouseOver(mouseX, mouseY, areaX, areaY, areaW, areaH)) {
            int contentHeight = 0;

            if (isInCategoryView) {
                contentHeight = getCategoryOverviewContentHeight();
            } else if (!isInCategoryView && selectedCategory != null && selectedEntry == null) {
                contentHeight = getCategoryEntriesContentHeight();
            } else if (selectedEntry != null) {
                contentHeight = getCurrentPageContentHeight(areaW);
            }

            int maxScroll = Math.max(0, contentHeight - areaH);
            scrollOffset -= verticalAmount > 0 ? 10 : -10;
            scrollOffset = Mth.clamp(scrollOffset, 0, maxScroll);

            return true;
        }

        if (selectedEntry != null) {
            int entryIndex = entryList.indexOf(selectedEntry);
            if ((verticalAmount > 0) && (this.selectedPage > 0 || entryIndex > 0)) {
                this.pageWardBack();
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                return true;
            } else if ((verticalAmount < 0) && (this.selectedPage < this.selectedEntry.right_side.size() - 1 || entryIndex < entryList.size() - 1)) {
                this.pageForward();
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    /*
    public static void openJeiForStack(ItemStack stack, boolean showUses) {
        IJeiRuntime runtime = JEIPlugin.getJeiRuntime();
        if (runtime == null || stack.isEmpty()) return;

        IFocusFactory focusFactory = runtime.getJeiHelpers().getFocusFactory();
        IRecipesGui recipesGui = runtime.getRecipesGui();

        Optional<ITypedIngredient<ItemStack>> typed = runtime.getJeiHelpers()
                .getIngredientManager()
                .createTypedIngredient(VanillaTypes.ITEM_STACK, stack);

        if (typed.isPresent()) {
            IFocus<ItemStack> focus = focusFactory.createFocus(
                    showUses ? RecipeIngredientRole.INPUT : RecipeIngredientRole.OUTPUT,
                    typed.get()
            );

            recipesGui.show(focus);
        }
    }
     */

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    }

    @Override
    public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float delta) {
        currentLayer = 0;

        this.hoveredStack = ItemStack.EMPTY;
        clearInteractiveRegions();

        super.extractContents(guiGraphics, mouseX, mouseY, delta);
        extractTooltip(guiGraphics, mouseX, mouseY);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        int RenderX = x;
        int RenderY = y;
        int RenderMouseX = mouseX;
        int RenderMouseY = mouseY;
        if(Objects.equals(Services.PLATFORM.getPlatformName(), "Fabric")) {
            RenderX = x - FabricX;
            RenderY = y - FabricY;
            RenderMouseX = mouseX - FabricX;
            RenderMouseY = mouseY - FabricY;
        }

        renderBookmarks(guiGraphics);

        renderSearch(guiGraphics, mouseX, mouseY, x, y);

        renderBook(guiGraphics, x, y);

        //renderJeiOverlays(guiGraphics, mouseX, mouseY);

        renderBg(guiGraphics, delta, mouseX, mouseY);

        if ((isInCategoryView) || (selectedCategory != null && selectedEntry == null)) {
            drawIconAndTitle(guiGraphics, RenderMouseX, RenderMouseY, RenderX, RenderY, getBookItem());
        }

        if (isInCategoryView) {
            renderCategoryOverview(guiGraphics, RenderMouseX, RenderMouseY, RenderX, RenderY);
        }
        else if (selectedCategory != null && selectedEntry == null) {
            renderCategoryEntries(guiGraphics, RenderMouseX, RenderMouseY, RenderX, RenderY);
        }
        else if (selectedEntry != null) {
            drawSelectedPage(guiGraphics, RenderMouseX, RenderMouseY, RenderX, RenderY);
            drawIconAndTitle(guiGraphics, RenderMouseX, RenderMouseY, RenderX, RenderY);
        }

        this.updateButtonVisibility();

        if (hoveredStack != null && !hoveredStack.isEmpty()) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(0, 0);
            applyLayer(guiGraphics, Z_TOOLTIP);
            guiGraphics.setTooltipForNextFrame(this.font, hoveredStack, RenderMouseX, RenderMouseY);
            guiGraphics.pose().popMatrix();
        }

        renderSearchBar(guiGraphics, mouseX, mouseY);
    }

    private void renderSearch(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0, 0);
        applyLayer(guiGraphics, 0);

        int searchy = 0;
        if (!mouseWasOverSearch) {
            searchy = 9;
        }

        int xPos = x + SEARCH_TEX_X_P;
        int yPos = y + SEARCH_TEX_Y_P + searchy;

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE,
                xPos,
                yPos,
                158,
                180,
                83,
                16,
                256,
                256
        );

        drawColoredOverlay(
                guiGraphics,
                xPos,
                yPos,
                158,
                180,
                83,
                16,
                0
        );

        int iconBaseX = x + SEARCH_TEX_X_P + 6;
        int iconBaseY = y + SEARCH_TEX_Y_P + 5 + searchy;

        float[] target = getMouseEasedOffset(iconBaseX, iconBaseY, mouseX, mouseY, 90f, 1f);

        easedIconX += (target[0] - easedIconX) * 0.2f;
        easedIconY += (target[1] - easedIconY) * 0.2f;

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE,
                (int)(iconBaseX + easedIconX),
                (int)(iconBaseY + easedIconY),
                145,
                182,
                12,
                12,
                256,
                256
        );

        drawColoredOverlay(
                guiGraphics,
                (int)(iconBaseX + easedIconX),
                (int)(iconBaseY + easedIconY),
                145,
                182,
                12,
                12,
                0);

        guiGraphics.pose().popMatrix();
    }

    private void renderBook(GuiGraphicsExtractor guiGraphics, int x, int y) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0, 0);
        applyLayer(guiGraphics, Z_BOOK_EDGE);

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE,
                x + 241,
                y,
                241,
                0,
                7,
                180,
                256,
                256
        );

        drawColoredOverlay(
                guiGraphics,
                x + 241,
                y,
                241,
                0,
                7,
                180,
                Z_BOOK_EDGE - 1
        );

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE,
                x + 124,
                y,
                124,
                0,
                124,
                12,
                256,
                256
        );

        drawColoredOverlay(
                guiGraphics,
                x + 124,
                y,
                124,
                0,
                124,
                12,
                Z_BOOK_EDGE - 1
        );
        guiGraphics.pose().popMatrix();
    }


    private void renderCategoryOverview(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        int areaX = x + 138;
        int areaY = y + 44;
        int areaW = SLOT_WIDTH;
        int areaH = 112;
        guiGraphics.enableScissor(areaX, areaY, areaX + areaW, areaY + areaH);

        int drawY = areaY - scrollOffset;

        CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
        int playerTier = tierData != null ? tierData.getTier() : 0;

        for (NecronomiconCategory cat : categories) {

            long unlocked = cat.entries.stream()
                    .filter(e -> {
                        int index = cat.entries.indexOf(e);
                        int tier = cat.tiers.get(index);
                        return tier <= playerTier;
                    })
                    .count();

            if (unlocked == 0) continue;

            guiGraphics.pose().pushMatrix();
            boolean hovered = MouseUtil.isMouseOver(mouseX, mouseY, areaX, drawY, SLOT_WIDTH, SLOT_HEIGHT);

            guiGraphics.pose().translate(0, 0);
            applyLayer(guiGraphics, Z_TOOLTIP - 10);

            guiGraphics.fill(areaX, drawY, areaX + SLOT_WIDTH, drawY + SLOT_HEIGHT, 0xAA202020);

            if (hovered) {
                guiGraphics.fill(areaX - 1, drawY - 1, areaX + SLOT_WIDTH + 1, drawY + SLOT_HEIGHT + 1, 0xAAFFFFFF);
            }

            guiGraphics.item(cat.icon, areaX + ITEM_PADDING, drawY + (SLOT_HEIGHT - ITEM_SIZE) / 2);
            //renderItemWithTooltip(guiGraphics, cat.icon, areaX + ITEM_PADDING, drawY + (SLOT_HEIGHT - ITEM_SIZE) / 2, mouseX, mouseY, false); // this adds a tooltip to the Category Item
            registerClickableItem(cat.icon, areaX + ITEM_PADDING, drawY + (SLOT_HEIGHT - ITEM_SIZE) / 2, ITEM_SIZE, ITEM_SIZE);

            Component message = Component.literal(cat.getDisplayTitle());

            guiGraphics.text(font, message, areaX + ITEM_SIZE + ITEM_PADDING * 2, drawY + (SLOT_HEIGHT - 8) / 2, 0xFFFFFF);

            guiGraphics.pose().popMatrix();

            drawY += SLOT_HEIGHT + SLOT_SPACING;
        }

        guiGraphics.disableScissor();
    }

    private void renderCategoryEntries(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (selectedCategory == null) return;

        int areaX = x + 138;
        int areaY = y + 44;
        int areaW = SLOT_WIDTH;
        int areaH = 112;
        guiGraphics.enableScissor(areaX, areaY, areaX + areaW, areaY + areaH);

        int drawY = areaY - scrollOffset;

        CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
        int playerTier = tierData != null ? tierData.getTier() : 0;

        for (int i = 0; i < selectedCategory.entries.size(); i++) {
            NecronomiconEntry entry = selectedCategory.entries.get(i);
            int entryTierValue = selectedCategory.tiers.get(i);

            if (entryTierValue > playerTier) continue;

            boolean hovered = MouseUtil.isMouseOver(mouseX, mouseY, areaX, drawY, SLOT_WIDTH, SLOT_HEIGHT);

            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(0, 0);
            applyLayer(guiGraphics, Z_TOOLTIP - 10);

            guiGraphics.fill(areaX, drawY, areaX + SLOT_WIDTH, drawY + SLOT_HEIGHT, 0xAA202020);
            if (hovered) {
                guiGraphics.fill(areaX - 1, drawY - 1, areaX + SLOT_WIDTH + 1, drawY + SLOT_HEIGHT + 1, 0xAAFFFFFF);
            }

            ItemStack entryIcon = RecipeHelper.parseItem(entry.icon);
            guiGraphics.item(entryIcon, areaX + ITEM_PADDING, drawY + (SLOT_HEIGHT - ITEM_SIZE) / 2);
            registerClickableItem(entryIcon, areaX + ITEM_PADDING, drawY + (SLOT_HEIGHT - ITEM_SIZE) / 2, ITEM_SIZE, ITEM_SIZE);

            ItemStack stack = getBookItem().copy();
            stack.set(ModDataComponentTypes.CODEX_TIER.get(), new CodexTierData(entryTierValue));

            renderScaledItem(guiGraphics, stack, areaX + ITEM_PADDING + 8, drawY + (SLOT_HEIGHT - ITEM_SIZE) / 2 + 8, Z_TOOLTIP - 5, 10);

            Component entryTitle = getEntryTitleComponent(entry);
            guiGraphics.text(font, entryTitle, areaX + ITEM_SIZE + ITEM_PADDING * 2, drawY + (SLOT_HEIGHT - 8) / 2, 0xFFFFFF);

            guiGraphics.pose().popMatrix();

            drawY += SLOT_HEIGHT + SLOT_SPACING;
        }

        guiGraphics.disableScissor();
    }

    private void drawIconAndTitle(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        if (selectedEntry == null) return;

        int yIcon = y + 11;
        int xIcon = x + 146;

        guiGraphics.text(this.font, getEntryTitleComponent(selectedEntry), x + 14, y + 14, ChatFormatting.DARK_GRAY.getColor(), false);

        if (selectedEntry.icon != null && !selectedEntry.icon.equals("")) {
            ItemStack iconStack = RecipeHelper.parseItem(selectedEntry.icon.toString());
            renderItemWithTooltip(guiGraphics, iconStack, xIcon + 34, yIcon + 7, mouseX, mouseY);
            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    BOOK_TEXTURE,
                    xIcon,
                    yIcon,
                    0,
                    180,
                    84,
                    30,
                    256,
                    256
            );
            drawColoredOverlay(guiGraphics, xIcon, yIcon, 0, 180, 84, 30, 0);
        }
    }


    private void drawIconAndTitle(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y, ItemStack iconStack) {
        int yIcon = y + 11;
        int xIcon = x + 146;

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE,
                xIcon,
                yIcon,
                0,
                180,
                84,
                30,
                256,
                256
        );
        drawColoredOverlay(guiGraphics, xIcon, yIcon, 0, 180, 84, 30, 0);

        renderItemWithTooltip(guiGraphics, iconStack, xIcon + 34, yIcon + 7, mouseX, mouseY, false);

        guiGraphics.text(this.font, Component.translatable(iconStack.getItem().getDescriptionId()), x + 14, y + 14, ChatFormatting.DARK_GRAY.getColor(), false);
    }

    private void drawSelectedPage(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        int areaX = x + 138;
        int areaY = y + 44;
        int areaW = 96;
        int areaH = 112;

        guiGraphics.enableScissor(areaX, areaY, areaX + areaW, areaY + areaH);

        if (selectedEntry != null && selectedEntry.right_side != null && !selectedEntry.right_side.isEmpty()) {
            if (selectedPage < 0 || selectedPage >= selectedEntry.right_side.size()) return;
            NecronomiconPage page = selectedEntry.right_side.get(selectedPage);
            if (page == null || page.modules == null) return;

            int drawY = areaY - scrollOffset;
            int drawX = areaX + 2;

            for (CodexModule module : page.modules) {
                switch (module.module_type) {
                    case "text" -> {
                        drawY = renderTextModule(guiGraphics, module, drawX, drawY, areaW, mouseX, mouseY);
                    }
                    case "recipe" -> {
                        drawY = renderRecipeModule(guiGraphics, module, drawX, drawY, areaX, areaY, areaW, areaH, mouseX, mouseY);
                    }
                    case "furnace_recipe" -> {
                        drawY = renderRecipeModule(guiGraphics, module, drawX, drawY, areaX, areaY, areaW, areaH, mouseX, mouseY);
                    }
                    default -> {
                        guiGraphics.text(this.font, Component.literal("Unknown module type: " + module.module_type), drawX, drawY, 0xFF0000);
                        drawY += LINE_HEIGHT + 2;
                    }
                }
            }
        }

        guiGraphics.disableScissor();
    }

    private int renderTextModule(GuiGraphicsExtractor guiGraphics, CodexModule module, int drawX, int drawY, int areaW, int mouseX, int mouseY) {
        String text = getModuleText(module);
        if (text.isEmpty()) {
            return drawY;
        }

        TextLayout layout = layoutText(text, drawX, drawY, areaW - 4);
        int playerTier = getPlayerTier();

        for (PositionedTextToken token : layout.tokens) {
            if (token.entry == null || !token.isLink) {
                guiGraphics.text(this.font, token.text, token.x, token.y, TEXT_COLOR, false);
                continue;
            }

            boolean locked = isEntryLocked(token.entry, playerTier);
            boolean hovered = MouseUtil.isMouseOver(mouseX, mouseY, token.x, token.y, token.width, LINE_HEIGHT);
            int color = locked ? LINK_COLOR_LOCKED : (hovered ? LINK_COLOR_HOVER : LINK_COLOR);

            guiGraphics.text(this.font, token.text, token.x, token.y, color, false);
            guiGraphics.horizontalLine(token.x, token.x + token.width - 1, token.y + LINK_UNDERLINE_OFFSET, color);
            textLinkRegions.add(new TextLinkRegion(token.entry, token.x, token.y, token.width, LINE_HEIGHT, locked));
        }

        return drawY + layout.height + 4;
    }

    private TextLayout layoutText(String text, int startX, int startY, int maxWidth) {
        List<TextSegment> segments = parseTextSegments(text);
        List<TextToken> tokens = tokenizeSegments(segments);
        List<PositionedTextToken> positioned = new ArrayList<>();

        int cursorX = startX;
        int cursorY = startY;

        for (TextToken token : tokens) {
            if (token.newline) {
                cursorX = startX;
                cursorY += LINE_HEIGHT;
                continue;
            }

            int tokenWidth = this.font.width(token.text);
            if (!token.whitespace && cursorX > startX && cursorX - startX + tokenWidth > maxWidth) {
                cursorX = startX;
                cursorY += LINE_HEIGHT;
            }

            if (token.whitespace && cursorX == startX) {
                continue;
            }

            if (!token.whitespace && !token.text.isEmpty()) {
                positioned.add(new PositionedTextToken(token.text, cursorX, cursorY, tokenWidth, token.entry, token.link));
            }

            cursorX += tokenWidth;
        }

        int height = (cursorY - startY) + LINE_HEIGHT;
        return new TextLayout(positioned, height);
    }

    private List<TextSegment> parseTextSegments(String text) {
        List<TextSegment> segments = new ArrayList<>();
        if (text == null || text.isEmpty()) return segments;

        Matcher matcher = LINK_PATTERN.matcher(text);
        int lastIndex = 0;

        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                segments.add(new TextSegment(text.substring(lastIndex, matcher.start()), null, false));
            }

            String content = matcher.group(1).trim();
            String display = null;
            String ref = content;
            int pipeIndex = content.indexOf('|');
            if (pipeIndex >= 0) {
                display = content.substring(0, pipeIndex).trim();
                ref = content.substring(pipeIndex + 1).trim();
            }

            NecronomiconEntry entry = findEntryForReference(ref);
            if (entry != null) {
                String displayText = (display != null && !display.isEmpty()) ? display : getEntryTitleString(entry);
                if (displayText == null || displayText.isEmpty()) {
                    displayText = ref;
                }
                segments.add(new TextSegment(displayText, entry, true));
            } else {
                segments.add(new TextSegment(matcher.group(0), null, false));
            }

            lastIndex = matcher.end();
        }

        if (lastIndex < text.length()) {
            segments.add(new TextSegment(text.substring(lastIndex), null, false));
        }

        return segments;
    }

    private List<TextToken> tokenizeSegments(List<TextSegment> segments) {
        List<TextToken> tokens = new ArrayList<>();
        for (TextSegment segment : segments) {
            String text = segment.text;
            if (text == null || text.isEmpty()) continue;

            int i = 0;
            while (i < text.length()) {
                char c = text.charAt(i);
                if (c == '\n') {
                    tokens.add(new TextToken("\n", segment.entry, segment.isLink, true, false));
                    i++;
                    continue;
                }

                if (Character.isWhitespace(c)) {
                    int start = i;
                    while (i < text.length()) {
                        char wc = text.charAt(i);
                        if (wc == '\n' || !Character.isWhitespace(wc)) break;
                        i++;
                    }
                    tokens.add(new TextToken(text.substring(start, i), segment.entry, segment.isLink, false, true));
                    continue;
                }

                int start = i;
                while (i < text.length()) {
                    char nc = text.charAt(i);
                    if (Character.isWhitespace(nc) || nc == '\n') break;
                    i++;
                }
                tokens.add(new TextToken(text.substring(start, i), segment.entry, segment.isLink, false, false));
            }
        }
        return tokens;
    }

    private int measureTextHeight(String text, int maxWidth) {
        if (text == null || text.isEmpty()) return 0;
        TextLayout layout = layoutText(text, 0, 0, maxWidth);
        return layout.height + 4;
    }

    private int renderRecipeModule(GuiGraphicsExtractor guiGraphics, CodexModule module, int drawX, int drawY, int areaX, int areaY, int areaW, int areaH, int mouseX, int mouseY) {
        boolean isFurnace = "furnace_recipe".equals(module.module_type);
        String title = isFurnace ? "Furnace Recipe:" : "Crafting Recipe:";
        int titleColor = isFurnace ? 0xFFAA00 : 0xAAAAFF;

        guiGraphics.text(this.font, Component.literal(title), drawX, drawY, titleColor);
        drawY += 12;

        /*
        Optional<IRecipeLayoutDrawable<?>> jeiLayout = getJeiLayoutForModule(module);
        if (jeiLayout.isPresent()) {
            IRecipeLayoutDrawable<?> layout = jeiLayout.get();
            Rect2i rect = layout.getRectWithBorder();
            int layoutWidth = rect.getWidth();
            int layoutHeight = rect.getHeight();
            int maxLayoutWidth = Math.max(1, areaW - 4);
            float layoutScale = Math.min(1.0f, (float) maxLayoutWidth / (float) layoutWidth);
            int scaledLayoutWidth = Math.max(1, Mth.ceil(layoutWidth * layoutScale));
            int scaledLayoutHeight = Math.max(1, Mth.ceil(layoutHeight * layoutScale));
            int layoutX = drawX + Math.max(0, (maxLayoutWidth - scaledLayoutWidth) / 2) + 2;
            int layoutY = drawY;
            int scaledMouseX = Mth.floor(toUnscaledCoordinate(mouseX, layoutX, layoutScale));
            int scaledMouseY = Mth.floor(toUnscaledCoordinate(mouseY, layoutY, layoutScale));

            layout.setPosition(layoutX, layoutY);
            renderedJeiLayouts.add(new RenderedJeiLayout(layout, layoutX, layoutY, layoutScale));
            layout.tick();
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(layoutX, layoutY);
            applyLayer(guiGraphics, 0);
            guiGraphics.pose().scale(layoutScale, layoutScale, 1.0f);
            guiGraphics.pose().translate(-layoutX, -layoutY);
            applyLayer(guiGraphics, 0);
            layout.drawRecipe(guiGraphics, scaledMouseX, scaledMouseY);
            guiGraphics.pose().popMatrix();

            return drawY + scaledLayoutHeight + 6;
        }
         */

        if (isFurnace) {
            ItemStack input = RecipeHelper.parseItem(module.input);
            ItemStack output = RecipeHelper.parseItem(module.output);

            int slotX = drawX;
            int slotY = drawY;
            drawSlotBackground(guiGraphics, slotX, slotY);
            renderItem(guiGraphics, input, slotX + 1, slotY + 1);

            guiGraphics.disableScissor();
            renderItemTooltip(guiGraphics, input, slotX + 1, slotY + 1, mouseX, mouseY);
            guiGraphics.enableScissor(areaX, areaY, areaX + areaW, areaY + areaH);

            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    ARROW_TEXTURE,
                    drawX + 25,
                    drawY + 4,
                    0,
                    0,
                    23,
                    15,
                    23,
                    15
            );

            int outputX = drawX + 50;
            drawSlotBackground(guiGraphics, outputX, slotY);
            renderItem(guiGraphics, output, outputX + 1, slotY + 1);

            guiGraphics.disableScissor();
            renderItemTooltip(guiGraphics, output, outputX + 1, slotY + 1, mouseX, mouseY);
            guiGraphics.enableScissor(areaX, areaY, areaX + areaW, areaY + areaH);

            return drawY + 25;
        }

        List<ItemStack> grid = RecipeHelper.buildCraftingGrid(module);
        ItemStack result = RecipeHelper.parseItem(module.result);

        int slotSize = 18;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                ItemStack stack = index < grid.size() ? grid.get(index) : ItemStack.EMPTY;

                int slotX = drawX + col * slotSize;
                int slotY = drawY + row * slotSize;

                drawSlotBackground(guiGraphics, slotX, slotY);
                renderItem(guiGraphics, stack, slotX + 1, slotY + 1);

                guiGraphics.disableScissor();
                renderItemTooltip(guiGraphics, stack, slotX + 1, slotY + 1, mouseX, mouseY);
                guiGraphics.enableScissor(areaX, areaY, areaX + areaW, areaY + areaH);
            }
        }

        int resultX = drawX + slotSize * 3 + 20;
        int resultY = drawY + slotSize;

        drawSlotBackground(guiGraphics, resultX, resultY);
        renderItem(guiGraphics, result, resultX + 1, resultY + 1);

        guiGraphics.disableScissor();
        renderItemTooltip(guiGraphics, result, resultX + 1, resultY + 1, mouseX, mouseY);
        guiGraphics.enableScissor(areaX, areaY, areaX + areaW, areaY + areaH);

        return drawY + slotSize * 3 + 25;
    }

    private int getRecipeModuleHeight(CodexModule module, int maxWidth) {
        int titleHeight = 12;
        /*
        Optional<IRecipeLayoutDrawable<?>> jeiLayout = getJeiLayoutForModule(module);
        if (jeiLayout.isPresent()) {
            Rect2i rect = jeiLayout.get().getRectWithBorder();
            int layoutWidth = Math.max(1, rect.getWidth());
            float layoutScale = Math.min(1.0f, (float) Math.max(1, maxWidth) / (float) layoutWidth);
            int layoutHeight = Mth.ceil(rect.getHeight() * layoutScale);
            return titleHeight + layoutHeight + 6;
        }
         */

        if ("furnace_recipe".equals(module.module_type)) {
            return titleHeight + 25;
        }

        if ("recipe".equals(module.module_type)) {
            return titleHeight + 18 * 3 + 25;
        }

        return titleHeight + LINE_HEIGHT;
    }

    /*
    private Optional<IRecipeLayoutDrawable<?>> getJeiLayoutForModule(CodexModule module) {
        if (module == null) return Optional.empty();
        if (JEIPlugin.getJeiRuntime() == null) return Optional.empty();

        String key = buildJeiLayoutKey(module);
        return jeiLayoutCache.computeIfAbsent(key, k -> createJeiLayoutForModule(module));
    }
     */

    private String buildJeiLayoutKey(CodexModule module) {
        return String.valueOf(module.module_type) + "|" +
                String.valueOf(module.recipe_type) + "|" +
                String.valueOf(module.result) + "|" +
                String.valueOf(module.input) + "|" +
                String.valueOf(module.output);
    }

    /*
    private Optional<IRecipeLayoutDrawable<?>> createJeiLayoutForModule(CodexModule module) {
        IJeiRuntime runtime = JEIPlugin.getJeiRuntime();
        if (runtime == null) return Optional.empty();

        JeiFocusData focusData = getJeiFocusData(module);
        if (focusData == null || focusData.stack.isEmpty()) return Optional.empty();

        Optional<ITypedIngredient<ItemStack>> typed = runtime.getIngredientManager()
                .createTypedIngredient(VanillaTypes.ITEM_STACK, focusData.stack);
        if (typed.isEmpty()) return Optional.empty();

        IFocusFactory focusFactory = runtime.getJeiHelpers().getFocusFactory();
        IFocus<ItemStack> focus = focusFactory.createFocus(focusData.role, typed.get());
        IFocusGroup focusGroup = focusFactory.createFocusGroup(List.of(focus));

        List<RecipeType<?>> preferredTypes = getPreferredRecipeTypes(module);
        return createJeiLayoutForFocus(focus, focusGroup, preferredTypes);
    }

     */

    /*
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Optional<IRecipeLayoutDrawable<?>> createJeiLayoutForFocus(IFocus<?> focus, IFocusGroup focusGroup, List<RecipeType<?>> preferredTypes) {
        IJeiRuntime runtime = JEIPlugin.getJeiRuntime();
        if (runtime == null) return Optional.empty();

        var recipeManager = runtime.getRecipeManager();
        List<IRecipeCategory<?>> categories = new ArrayList<>();

        for (RecipeType<?> type : preferredTypes) {
            try {
                IRecipeCategory category = recipeManager.getRecipeCategory((RecipeType) type);
                if (category != null) {
                    categories.add(category);
                }
            } catch (Exception ignored) {
            }
        }

        if (categories.isEmpty()) {
            categories.addAll(recipeManager.createRecipeCategoryLookup()
                    .limitFocus(List.of(focus))
                    .get()
                    .toList());
        }

        for (IRecipeCategory category : categories) {
            RecipeType type = category.getRecipeType();
            Optional recipe = recipeManager.createRecipeLookup(type)
                    .limitFocus(List.of(focus))
                    .get()
                    .findFirst();

            if (recipe.isEmpty()) continue;

            Optional layout = recipeManager.createRecipeLayoutDrawable(category, recipe.get(), focusGroup);
            if (layout.isPresent()) {
                return layout;
            }
        }

        return Optional.empty();
    }
    */

    /*
    private void renderJeiOverlays(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        if (renderedJeiLayouts.isEmpty()) return;
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0, 0);
            applyLayer(guiGraphics, Z_TOOLTIP);
        for (RenderedJeiLayout rendered : renderedJeiLayouts) {
            int scaledMouseX = Mth.floor(toUnscaledCoordinate(mouseX, rendered.x, rendered.scale));
            int scaledMouseY = Mth.floor(toUnscaledCoordinate(mouseY, rendered.y, rendered.scale));
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(rendered.x, rendered.y);
            applyLayer(guiGraphics, 0);
            guiGraphics.pose().scale(rendered.scale, rendered.scale, 1.0f);
            guiGraphics.pose().translate(-rendered.x, -rendered.y);
            applyLayer(guiGraphics, 0);
            rendered.layout.drawOverlays(guiGraphics, scaledMouseX, scaledMouseY);
            guiGraphics.pose().popMatrix();
        }
        guiGraphics.pose().popMatrix();
    }

     */

    private List<RecipeType<?>> getPreferredRecipeTypes(CodexModule module) {

        List<RecipeType<?>> types = new ArrayList<>();

        if ("recipe".equals(module.module_type)) {
            String type = module.recipe_type == null ? "" : module.recipe_type.toLowerCase();

            if (!type.isEmpty() && !"crafting_table".equals(type)) {
                /*
                Optional<RecipeType<?>> customType = JEIPlugin.getJeiRuntime()
                        .getRecipeManager()
                        .getRecipeType(Identifier.parse(type));

                if (customType.isPresent()) {
                    types.add(customType.get());
                    return types;
                }
                 */
            }
            types.add(RecipeType.CRAFTING);
            types.add(RecipeType.STONECUTTING);
            types.add(RecipeType.SMITHING);
        }

        else if ("furnace_recipe".equals(module.module_type)) {
            types.add(RecipeType.SMELTING);
            types.add(RecipeType.BLASTING);
            types.add(RecipeType.SMOKING);
            types.add(RecipeType.CAMPFIRE_COOKING);
        }

        return types;
    }

    /*
    private JeiFocusData getJeiFocusData(CodexModule module) {
        if ("furnace_recipe".equals(module.module_type) && module.input != null) {
            ItemStack input = RecipeHelper.parseItem(module.input);
            if (!input.isEmpty()) {
                return new JeiFocusData(input, RecipeIngredientRole.INPUT);
            }
        }

        String resultId = module.result != null ? module.result : module.output;
        ItemStack result = RecipeHelper.parseItem(resultId);
        if (!result.isEmpty()) {
            return new JeiFocusData(result, RecipeIngredientRole.OUTPUT);
        }

        return null;
    }
     */

    private void drawSlotBackground(GuiGraphicsExtractor guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + 18, y + 18, SLOT_BORDER_COLOR);
    }

    private void renderItemWithTooltip(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y, int mouseX, int mouseY) {
        renderItemWithTooltip(guiGraphics, stack, x, y, mouseX, mouseY, true);
    }

    private void renderItemWithTooltip(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y, int mouseX, int mouseY, boolean bagground) {
        if (bagground) {
            guiGraphics.fill(x, y, x + 16, y + 16, 0xFF555555);
        }
        guiGraphics.item(stack, x, y);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0, 0);
        applyLayer(guiGraphics, Z_TOOLTIP + 100);
        guiGraphics.itemDecorations(this.font, stack, x, y, null);
        guiGraphics.pose().popMatrix();

        registerClickableItem(stack, x, y, 16, 16);

        if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16 && !stack.isEmpty()) {
            hoveredStack = stack;
        }
    }

    private void renderItem(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y) {
        guiGraphics.fill(x, y, x + 16, y + 16, 0xFF555555);
        guiGraphics.item(stack, x, y);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0, 0);
        applyLayer(guiGraphics, Z_TOOLTIP + 100);
        guiGraphics.itemDecorations(this.font, stack, x, y, null);
        guiGraphics.pose().popMatrix();

        registerClickableItem(stack, x, y, 16, 16);
    }

    private void renderItemTooltip(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y, int mouseX, int mouseY) {
        if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16 && !stack.isEmpty()) {
            hoveredStack = stack;
        }
    }

    private void clearInteractiveRegions() {
        clickableItemRegions.clear();
        textLinkRegions.clear();
        //renderedJeiLayouts.clear();
    }

    private void registerClickableItem(ItemStack stack, int x, int y, int width, int height) {
        if (stack == null || stack.isEmpty()) return;
        clickableItemRegions.add(new ClickableItemRegion(stack, x, y, width, height));
    }

    /*
    public Optional<IClickableIngredient<?>> getJeiClickableIngredient(double mouseX, double mouseY) {
        IJeiRuntime runtime = JEIPlugin.getJeiRuntime();
        if (runtime == null) return Optional.empty();

        for (int i = renderedJeiLayouts.size() - 1; i >= 0; i--) {
            RenderedJeiLayout rendered = renderedJeiLayouts.get(i);
            double scaledMouseX = toUnscaledCoordinate(mouseX, rendered.x, rendered.scale);
            double scaledMouseY = toUnscaledCoordinate(mouseY, rendered.y, rendered.scale);
            Optional<mezz.jei.api.gui.inputs.RecipeSlotUnderMouse> slotUnderMouse =
                    rendered.layout.getSlotUnderMouse(scaledMouseX, scaledMouseY);
            if (slotUnderMouse.isEmpty()) continue;

            var slot = slotUnderMouse.get().slot();
            Optional<ITypedIngredient<?>> displayed = slot.getDisplayedIngredient();
            if (displayed.isEmpty()) continue;

            Rect2i rect = slot.getAreaIncludingBackground();
            int scaledX = rendered.x + Mth.floor(rect.getX() * rendered.scale);
            int scaledY = rendered.y + Mth.floor(rect.getY() * rendered.scale);
            int scaledWidth = Math.max(1, Mth.ceil(rect.getWidth() * rendered.scale));
            int scaledHeight = Math.max(1, Mth.ceil(rect.getHeight() * rendered.scale));
            Rect2i area = new Rect2i(scaledX, scaledY, scaledWidth, scaledHeight);
            return createClickableIngredient(runtime, displayed.get(), area);
        }

        var ingredientManager = runtime.getIngredientManager();
        for (int i = clickableItemRegions.size() - 1; i >= 0; i--) {
            ClickableItemRegion region = clickableItemRegions.get(i);
            if (!MouseUtil.isMouseOver(mouseX, mouseY, region.x, region.y, region.width, region.height)) continue;

            Rect2i area = new Rect2i(region.x, region.y, region.width, region.height);
            Optional<IClickableIngredient<ItemStack>> clickable =
                    ingredientManager.createClickableIngredient(VanillaTypes.ITEM_STACK, region.stack, area, true);
            if (clickable.isPresent()) {
                return Optional.of(clickable.get());
            }
        }

        return Optional.empty();
    }
     */

    private double toUnscaledCoordinate(double mouseCoordinate, int origin, float scale) {
        if (scale == 0.0f || scale == 1.0f) {
            return mouseCoordinate;
        }
        return origin + ((mouseCoordinate - origin) / scale);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.extractTransparentBackground(guiGraphics);

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE,
                x,
                y,
                0,
                0,
                imageWidth,
                imageHeight,
                256,
                256
        );
        drawColoredOverlay(guiGraphics, x, y, 0, 0, imageWidth, imageHeight, 0);
    }

    protected void renderBg(GuiGraphicsExtractor guiGraphics, float partialTick, int mouseX, int mouseY) {

        if (showAdvancement && advancementsScreen != null) {

            int x = (width - imageWidth) / 2;
            int y = (height - imageHeight) / 2;

            int advX = x + 16;
            int advY = y + 22;
            int advW = 92;
            int advH = 138;

            //if (MouseUtil.isMouseOver(mouseX, mouseY, advX, advY, advW, advH)) {

            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(advancementX, advancementY);
            applyLayer(guiGraphics, 0);

            CustomAdvancementRenderer.renderTooltipsOnly(
                    advancementsScreen,
                    guiGraphics,
                    mouseX - advancementX,
                    mouseY - advancementY,
                    (this.width - 252) / 2,
                    (this.height - 140) / 2,
                    this,
                    x,
                    y
            );
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();

        if (handleTextLinkClick(mouseX, mouseY)) {
            return true;
        }

        if (!searchResults.isEmpty()) {

            int startX = x + SEARCH_FIELD_X_P;
            int startY = y + SEARCH_FIELD_Y_P + SEARCH_FIELD_H_P + 2;
            int widthBox = 90;
            int lineHeight = 10;
            int maxVisible = Math.min(searchResults.size(), 6);

            for (int i = 0; i < maxVisible; i++) {
                int yPos = startY + i * lineHeight;

                if (mouseX >= startX && mouseX <= startX + widthBox &&
                        mouseY >= yPos && mouseY <= yPos + lineHeight) {

                    NecronomiconEntry selected = searchResults.get(i);
                    this.selectedEntry = selected;
                    this.isInCategoryView = false;
                    this.selectedPage = 0;
                    this.scrollOffset = 0;
                    updateButtonVisibility();

                    searchResults.clear();
                    searchBox.setFocused(false);

                    return true;
                }
            }

            if (!isMouseOverSearchArea(mouseX, mouseY, x, y)) {
                searchResults.clear();
                searchBox.setFocused(false);
                return super.mouseClicked(event, doubleClick);
            }
        }

        if (searchBox != null) {

            int fieldX = x + SEARCH_FIELD_X_P;
            int fieldY = y + SEARCH_FIELD_Y_P;

            boolean inField = mouseX >= fieldX && mouseX <= fieldX + SEARCH_FIELD_W_P &&
                    mouseY >= fieldY && mouseY <= fieldY + SEARCH_FIELD_H_P;

            if (!inField) {
                searchBox.setFocused(false);
                searchResults.clear();
            }
        }

        int areaX = x + 138;
        int areaY = y + 44;

        if (isInCategoryView) {
            int drawY = areaY - scrollOffset;

            for (NecronomiconCategory cat : categories) {

                if (mouseX >= areaX && mouseX <= areaX + SLOT_WIDTH &&
                        mouseY >= drawY && mouseY <= drawY + SLOT_HEIGHT) {

                    CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
                    int playerTier = tierData != null ? tierData.getTier() : 0;

                    List<NecronomiconEntry> unlocked = new ArrayList<>();

                    for (int i = 0; i < cat.entries.size(); i++) {
                        if (cat.tiers.get(i) <= playerTier) {
                            unlocked.add(cat.entries.get(i));
                        }
                    }

                    this.selectedCategory = cat;
                    this.selectedEntry = null;
                    this.isInCategoryView = false;

                    this.updateButtonVisibility();
                    this.selectedPage = 0;
                    this.scrollOffset = 0;

                    return true;
                }
                drawY += SLOT_HEIGHT + SLOT_SPACING;
            }
        }

        if (!isInCategoryView && selectedCategory != null && selectedEntry == null) {
            int drawY = areaY - scrollOffset;

            CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
            int playerTier = tierData != null ? tierData.getTier() : 0;

            for (int i = 0; i < selectedCategory.entries.size(); i++) {
                NecronomiconEntry entry = selectedCategory.entries.get(i);
                int entryTierValue = selectedCategory.tiers.get(i);

                if (entryTierValue > playerTier) continue;

                if (mouseX >= areaX && mouseX <= areaX + SLOT_WIDTH &&
                        mouseY >= drawY && mouseY <= drawY + SLOT_HEIGHT) {

                    NecronomiconEntry NecronomiconEntry = entryList.stream().filter(e -> e.id.equals(entry.id)).findFirst().orElse(null);
                    if (NecronomiconEntry == null) continue;

                    this.selectedEntry = NecronomiconEntry;
                    this.selectedPage = 0;
                    this.scrollOffset = 0;
                    this.isInCategoryView = false;
                    this.updateButtonVisibility();
                    return true;
                }
                drawY += SLOT_HEIGHT + SLOT_SPACING;
            }
        }

        if (showAdvancement && this.advancementsScreen != null) {

            int relX = (int) (mouseX - x);
            int relY = (int) (mouseY - y);

            if (relX >= 16 && relY >= 22 && relX < 16 + 92 && relY < 22 + 138) {
                double[] adj = mapToAdvancementCoords(mouseX, mouseY);

                MouseButtonEvent forwarded = new MouseButtonEvent(
                        adj[0],
                        adj[1],
                        event.buttonInfo()
                );

                return this.advancementsScreen.mouseClicked(forwarded, doubleClick);
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    private boolean handleTextLinkClick(double mouseX, double mouseY) {
        if (textLinkRegions.isEmpty() || this.minecraft == null) return false;

        for (int i = textLinkRegions.size() - 1; i >= 0; i--) {
            TextLinkRegion region = textLinkRegions.get(i);
            if (!MouseUtil.isMouseOver(mouseX, mouseY, region.x, region.y, region.width, region.height)) {
                continue;
            }

            if (region.locked) {
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.VILLAGER_NO, 1.0F));
                return true;
            }

            openEntry(region.entry);
            this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
            return true;
        }

        return false;
    }

    private void openEntry(NecronomiconEntry entry) {
        if (entry == null) return;
        this.selectedEntry = entry;
        this.selectedCategory = getCategoryForEntry(entry);
        this.selectedPage = 0;
        this.scrollOffset = 0;
        this.isInCategoryView = false;
        updateButtonVisibility();
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
        if (showAdvancement && this.advancementsScreen != null) {

            int imageWidth = 252;
            int imageHeight = 140;
            int x = (this.width - imageWidth) / 2;
            int y = (this.height - imageHeight) / 2;

            double mouseX = event.x();
            double mouseY = event.y();
            int button = event.button();

            int relX = (int) (mouseX - x);
            int relY = (int) (mouseY - y);

            if (relX >= 16 && relY >= 22 && relX < 16 + 92 && relY < 22 + 138) {
                double[] adj = mapToAdvancementCoords(mouseX, mouseY);
                //return this.advancementsScreen.mouseDragged(adj[0], adj[1], button, dragX, dragY);

                AdvancementTab selected = CustomAdvancementRenderer.getSelectedTab(advancementsScreen);
                AdvancementsScreenMixin screenMixin = (AdvancementsScreenMixin) advancementsScreen;

                if (button != 0) {
                    screenMixin.setIsScrolling(false);
                    return false;
                } else {
                    if (!screenMixin.getIsScrolling()) {
                        screenMixin.setIsScrolling(true);
                    } else if (selected != null) {
                        scroll(dragX, dragY);
                    }

                    return true;
                }
            }
        }

        return super.mouseDragged(event, dragX, dragY);
    }

    public void scroll(double deltaX, double deltaY) {
        AdvancementTab selected = CustomAdvancementRenderer.getSelectedTab(advancementsScreen);
        AdvancementTabMixin tabMixin = (AdvancementTabMixin) selected;

        int viewWidth = 52;
        int viewHeight = 82;

        if (tabMixin.getMaxX() - tabMixin.getMinX() > viewWidth) {
            tabMixin.setScrollX(Mth.clamp(
                    tabMixin.getScrollX() + deltaX,
                    -(tabMixin.getMaxX() - viewWidth),
                    0.0
            ));
        }

        if (tabMixin.getMaxY() - tabMixin.getMinY() > viewHeight) {
            tabMixin.setScrollY(Mth.clamp(
                    tabMixin.getScrollY() + deltaY,
                    -(tabMixin.getMaxY() - viewHeight),
                    0.0
            ));
        }
    }


    private double[] mapToAdvancementCoords(double screenMouseX, double screenMouseY) {
        double guiLeft = (this.width - 252) / 2.0;
        double guiTop  = (this.height - 140) / 2.0;

        double guiX = screenMouseX - guiLeft;
        double guiY = screenMouseY - guiTop;

        return new double[] {
                guiX + this.advancementX,
                guiY + this.advancementY
        };
    }

    private boolean isMouseOverSearchArea(double mouseX, double mouseY, int baseX, int baseY) {
        int fieldX = baseX + SEARCH_FIELD_X_P;
        int fieldY = baseY + SEARCH_FIELD_Y_P;
        int fieldW = SEARCH_FIELD_W_P;
        int fieldH = SEARCH_FIELD_H_P;

        int resultX = fieldX;
        int resultY = fieldY + fieldH + 2;
        int resultW = 90;
        int resultH = 10 * Math.min(searchResults.size(), 6);

        return (mouseX >= fieldX && mouseX <= fieldX + fieldW && mouseY >= fieldY && mouseY <= fieldY + fieldH)
                || (mouseX >= resultX && mouseX <= resultX + resultW && mouseY >= resultY && mouseY <= resultY + resultH);
    }

    protected void closeScreen() {
        this.minecraft.setScreen(null);
    }

    public static boolean hasShiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), 340)
                || InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), 344);
    }

    private void createBookmarkButtons() {
        for (BookmarkButton b : bookmarkButtons) {
            try { this.removeWidget(b); } catch (Exception ignored) {}
        }
        bookmarkButtons.clear();
        if (setterButton != null) {
            try { this.removeWidget(setterButton); } catch (Exception ignored) {}
            setterButton = null;
        }

        int baseX = (width - imageWidth) / 2 + 248;
        int baseY = (height - imageHeight) / 2 + 8;

        int maxBookmarks = Math.min(playerBookmarks.size(), 22);

        for (int bookmarkSize = 0; bookmarkSize < maxBookmarks; bookmarkSize++) {
            final String entryId = playerBookmarks.get(bookmarkSize);
            NecronomiconEntry entry = entryList.stream().filter(e -> e.id.equals(entryId)).findFirst().orElse(null);
            if (entry == null) continue;

            int col = bookmarkSize / 11;
            int row = bookmarkSize % 11;

            int x = baseX + col * 6;
            int y = baseY + row * 15;

            int layerZ = col == 0 ? Z_BOOKMARK_ITEM : Z_BOOKMARK_ITEM / 2;

            BookmarkButton b = new BookmarkButton(x, y, layerZ, btn -> {
                if (hasShiftDown()) {
                    Services.NETWORK.sendToServer(
                            new SetBookmarksPacket(entryId, false)
                    );
                    CodexBookmarksData.removeBookmark(this.minecraft.player, entryId);
                    playerBookmarks.remove(entryId);
                    this.createBookmarkButtons();
                    return;
                }

                if (entry != null) {
                    this.selectedEntry = entry;
                    this.selectedPage = 0;
                    this.scrollOffset = 0;
                    this.isInCategoryView = false;
                    this.updateButtonVisibility();
                }
            });

            this.addRenderableWidget(b);
            bookmarkButtons.add(b);
        }

        if (playerBookmarks.size() < 22) {
            int idx = playerBookmarks.size();
            int col = idx / 11;
            int row = idx % 11;
            int x = baseX + col * 6;
            int y = baseY + row * 15;

            int layerZ = col == 0 ? Z_BOOKMARK_ITEM : Z_BOOKMARK_ITEM / 2;

            setterButton = new BookmarkButton(x, y, layerZ, btn -> {
                if (this.selectedEntry != null && this.minecraft.player != null && !playerBookmarks.contains(this.selectedEntry.id)) {
                    Services.NETWORK.sendToServer(new SetBookmarksPacket(this.selectedEntry.id, true));

                    CodexBookmarksData.addBookmark(this.minecraft.player, this.selectedEntry.id);
                    this.playerBookmarks.add(this.selectedEntry.id);
                    this.createBookmarkButtons();
                }
            });

            this.addRenderableWidget(setterButton);
        }
    }

    private void renderBookmarks(GuiGraphicsExtractor guiGraphics) {
        int baseX = (width - imageWidth) / 2 + 248;
        int baseY = (height - imageHeight) / 2 + 8;

        for (int bookmarkSize = 0; bookmarkSize < bookmarkButtons.size(); bookmarkSize++) {
            String id = playerBookmarks.get(bookmarkSize);
            NecronomiconEntry entry = entryList.stream().filter(e -> e.id.equals(id)).findFirst().orElse(null);
            if (entry == null || entry.icon == null) continue;

            ItemStack iconStack = RecipeHelper.parseItem(entry.icon);
            BookmarkButton b = bookmarkButtons.get(bookmarkSize);

            int col = (bookmarkSize >= 11) ? 1 : 0;
            int row = (bookmarkSize >= 11) ? (bookmarkSize - 12) : bookmarkSize;

            int colx = baseX + (col * 6);
            int coly = baseY + (row * 15);

            if (col == 1) coly += 15;

            if (!b.isHoveredOrFocused()) colx -= 5;

            int layerZ = col == 0 ? Z_BOOKMARK_ITEM : 100;

            renderScaledItem(guiGraphics, iconStack, colx, coly + 3, layerZ, 7);
        }
    }

    private void renderScaledItem(GuiGraphicsExtractor guiGraphics, ItemStack stack, int x, int y, int z, int size) {
        if (stack.isEmpty()) return;

        registerClickableItem(stack, x, y, size, size);

        float scale = size / 16.0f;
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        applyLayer(guiGraphics, z);
        guiGraphics.pose().scale(scale, scale);
        guiGraphics.item(stack, 0, 0);
        guiGraphics.pose().popMatrix();
    }

    private void updateSearchResults(String query) {
        searchResults.clear();
        if (query == null || query.isBlank()) return;

        String lowerQuery = query.toLowerCase();
        for (NecronomiconEntry entry : entryList) {
            boolean matched = false;
            if (entry.search_items != null) {
                for (String tag : entry.search_items) {
                    if (tag.toLowerCase().contains(lowerQuery)) {
                        matched = true;
                        break;
                    }
                }
            }

            if (!matched) {
                String title = getEntryTitleString(entry);
                if (!title.isBlank() && title.toLowerCase().contains(lowerQuery)) {
                    matched = true;
                }
            }

            if (matched) {
                searchResults.add(entry);
            }
        }
    }

    private void renderSearchBar(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int barX = x + SEARCH_TEX_X_P;
        int barY = y + SEARCH_TEX_Y_P;

        boolean mouseOver = MouseUtil.isMouseOver(mouseX, mouseY, barX, barY, SEARCH_TEX_W_P, SEARCH_TEX_H_P);
        if (mouseOver) mouseWasOverSearch = true;

        if (mouseWasOverSearch && !searchBox.isVisible()) {
            searchBox.setVisible(true);
            searchBox.setFocused(true);
        }

        searchBox.setX(x + SEARCH_FIELD_X_P);
        searchBox.setY(y + SEARCH_FIELD_Y_P);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0, 0);
        applyLayer(guiGraphics, Z_TOOLTIP + Z_BOOKMARK_ITEM);
        searchBox.extractRenderState(guiGraphics, mouseX, mouseY, 0);
        guiGraphics.pose().popMatrix();

        if (!searchResults.isEmpty()) {

            renderSearchResults(guiGraphics, x, y, mouseX, mouseY);
        }

        if (showAdvancement && advancementsScreen != null) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(advancementX, advancementY);
            applyLayer(guiGraphics, 200);
            guiGraphics.pose().popMatrix();
        }
    }

    private void renderSearchResults(GuiGraphicsExtractor guiGraphics, int baseX, int baseY, int mouseX, int mouseY) {
        int startX = baseX + SEARCH_FIELD_X_P;
        int startY = baseY + SEARCH_FIELD_Y_P + SEARCH_FIELD_H_P + 2;
        int width = 0;
        int lineHeight = 10;
        int maxVisible = 6;

        int shown = Math.min(searchResults.size(), maxVisible);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0, 0);

        for (int i = 0; i < shown; i++) {
            NecronomiconEntry entry = searchResults.get(i);
            Component title = getEntryTitleComponent(entry);
            int titleWidth = this.font.width(title);
            if (titleWidth > width) {
                width = titleWidth;
            }
        }

        guiGraphics.fill(startX - 2, startY - 2, startX + width, startY + shown * lineHeight + 2, 0xCC000000);

        for (int i = 0; i < shown; i++) {
            NecronomiconEntry entry = searchResults.get(i);
            int yPos = startY + i * lineHeight;
            Component title = getEntryTitleComponent(entry);
            int titleWidth = this.font.width(title);

            if (titleWidth > width) {
                width = titleWidth;
            }

            boolean hover = mouseX >= startX && mouseX <= startX + width && mouseY >= yPos && mouseY <= yPos + lineHeight;
            int color = hover ? 0xFFFFFF55 : 0xFFFFFFFF;

            guiGraphics.text(font, title, startX, yPos, color);
        }

        guiGraphics.pose().popMatrix();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    private float[] getMouseEasedOffset(float baseX, float baseY, double mouseX, double mouseY, float radius, float maxOffset) {
        double dx = mouseX - baseX;
        double dy = mouseY - baseY;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist > radius) return new float[]{0f, 0f};

        float strength = (float) (1.0 - (dist / radius));

        float dirX = (float) (dx / dist);
        float dirY = (float) (dy / dist);

        float offsetX = dirX * strength * maxOffset;
        float offsetY = dirY * strength * maxOffset;

        return new float[]{offsetX, offsetY};
    }

    private void drawColoredOverlay(GuiGraphicsExtractor guiGraphics, int x_p, int y_p, int x, int y, int width, int height, int z_Layer) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        ItemStack stack = getBookItem();

        if (!stack.is(ModItems.NECRONOMICON.get())) return;

        DyedItemColor dyedColor = stack.get(DataComponents.DYED_COLOR);

        int rgb = dyedColor != null ? dyedColor.rgb() : 0x643732;

        int argb = 0xFF000000 | rgb;

        //System.out.println("rgb " + rgb);

        guiGraphics.pose().pushMatrix();

        guiGraphics.pose().translate(0, 0);
        applyLayer(guiGraphics, z_Layer + 1);
        //RenderSystem.setShaderColor(r, g, b, 1.0f);

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                BOOK_TEXTURE_GRAY,
                x_p,
                y_p,
                x,
                y,
                width,
                height,
                256,
                256,
                argb
        );

        //.setShaderColor(1f, 1f, 1f, 1f);
        guiGraphics.pose().popMatrix();
    }

    private ItemStack getBookItem() {
        var minecraft = net.minecraft.client.Minecraft.getInstance();
        if (minecraft.player == null) return ModItems.NECRONOMICON.get().getDefaultInstance();

        ItemStack stack = minecraft.player.getMainHandItem();
        if (stack.isEmpty()) stack = minecraft.player.getOffhandItem();

        if (stack.isEmpty() || !stack.is(ModItems.NECRONOMICON.get())) {
            stack = minecraft.player.getInventory().getItem(minecraft.player.getInventory()
                    .findSlotMatchingItem(new ItemStack(ModItems.NECRONOMICON.get())));
        }

        if (!stack.is(ModItems.NECRONOMICON.get())) return ModItems.NECRONOMICON.get().getDefaultInstance();

        return stack;
    }

    private Component getEntryTitleComponent(NecronomiconEntry entry) {
        if (entry == null) return Component.empty();
        if (entry.title != null && !entry.title.isBlank()) {
            return Component.literal(entry.title);
        }
        if (entry.title_key != null && !entry.title_key.isBlank()) {
            return Component.translatable(entry.title_key);
        }
        return Component.empty();
    }

    private String getEntryTitleString(NecronomiconEntry entry) {
        return getEntryTitleComponent(entry).getString();
    }

    private String getModuleText(CodexModule module) {
        if (module == null) return "";
        if (module.text != null && !module.text.isBlank()) {
            return module.text;
        }
        if (module.text_key != null && !module.text_key.isBlank()) {
            return Component.translatable(module.text_key).getString();
        }
        return "";
    }

    private boolean canEditCodex() {
        return this.minecraft != null
                && this.minecraft.player != null
                && this.minecraft.player.permissions().hasPermission(Permissions.COMMANDS_OWNER);
    }

    private int getPlayerTier() {
        CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
        return tierData != null ? tierData.getTier() : 0;
    }

    private boolean isEntryLocked(NecronomiconEntry entry, int playerTier) {
        int entryTier = getTierForEntry(entry);
        return entryTier >= 0 && entryTier > playerTier;
    }

    private NecronomiconEntry findEntryForReference(String reference) {
        if (reference == null || reference.isBlank()) return null;

        String ref = reference.trim();
        for (NecronomiconEntry entry : entryList) {
            if (entry != null && entry.id != null && entry.id.equalsIgnoreCase(ref)) {
                return entry;
            }
        }

        String normalizedRef = normalizeItemId(ref);
        for (NecronomiconEntry entry : entryList) {
            if (entry == null) continue;

            if (entry.icon != null && normalizeItemId(entry.icon).equalsIgnoreCase(normalizedRef)) {
                return entry;
            }

            if (entry.search_items != null) {
                for (String tag : entry.search_items) {
                    if (tag != null && (tag.equalsIgnoreCase(ref) || tag.equalsIgnoreCase(normalizedRef))) {
                        return entry;
                    }
                }
            }

            String title = getEntryTitleString(entry);
            if (!title.isBlank() && title.equalsIgnoreCase(ref)) {
                return entry;
            }
        }

        return null;
    }

    private String normalizeItemId(String id) {
        if (id == null || id.isBlank()) return "";
        String trimmed = id.trim();
        return trimmed.contains(":") ? trimmed : "minecraft:" + trimmed;
    }

    public int getTierForEntry(NecronomiconEntry entry) {
        if (entry == null || entry.id == null) return -1;
        if (categories == null || categories.isEmpty()) return -1;

        for (NecronomiconCategory category : categories) {
            if (category == null || category.entries == null) continue;

            for (int i = 0; i < category.entries.size(); i++) {
                NecronomiconEntry e = category.entries.get(i);
                if (e == null || e.id == null) continue;
                if (e.id.equals(entry.id)) {
                    if (category.tiers != null && i < category.tiers.size()) {
                        return category.tiers.get(i);
                    } else {
                        return -1;
                    }
                }
            }
        }

        return -1;
    }

    public NecronomiconCategory getCategoryForEntry(NecronomiconEntry entry) {
        if (entry == null || entry.id == null) return null;
        if (categories == null || categories.isEmpty()) return null;

        for (NecronomiconCategory category : categories) {
            if (category == null || category.entries == null) continue;

            for (NecronomiconEntry e : category.entries) {
                if (e == null || e.id == null) continue;

                if (e.id.equals(entry.id)) {
                    return category;
                }
            }
        }

        return null;
    }

    private static class ClickableItemRegion {
        private final ItemStack stack;
        private final int x;
        private final int y;
        private final int width;
        private final int height;

        private ClickableItemRegion(ItemStack stack, int x, int y, int width, int height) {
            this.stack = stack;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    private static class TextLinkRegion {
        private final NecronomiconEntry entry;
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private final boolean locked;

        private TextLinkRegion(NecronomiconEntry entry, int x, int y, int width, int height, boolean locked) {
            this.entry = entry;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.locked = locked;
        }
    }

    /*
    private static class RenderedJeiLayout {
        private final IRecipeLayoutDrawable<?> layout;
        private final int x;
        private final int y;
        private final float scale;

        private RenderedJeiLayout(IRecipeLayoutDrawable<?> layout, int x, int y, float scale) {
            this.layout = layout;
            this.x = x;
            this.y = y;
            this.scale = scale;
        }
    }
     */

    private static class TextSegment {
        private final String text;
        private final NecronomiconEntry entry;
        private final boolean isLink;

        private TextSegment(String text, NecronomiconEntry entry, boolean isLink) {
            this.text = text;
            this.entry = entry;
            this.isLink = isLink;
        }
    }

    private static class TextToken {
        private final String text;
        private final NecronomiconEntry entry;
        private final boolean link;
        private final boolean newline;
        private final boolean whitespace;

        private TextToken(String text, NecronomiconEntry entry, boolean link, boolean newline, boolean whitespace) {
            this.text = text;
            this.entry = entry;
            this.link = link;
            this.newline = newline;
            this.whitespace = whitespace;
        }
    }

    private static class PositionedTextToken {
        private final String text;
        private final int x;
        private final int y;
        private final int width;
        private final NecronomiconEntry entry;
        private final boolean isLink;

        private PositionedTextToken(String text, int x, int y, int width, NecronomiconEntry entry, boolean isLink) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.width = width;
            this.entry = entry;
            this.isLink = isLink;
        }
    }

    private static class TextLayout {
        private final List<PositionedTextToken> tokens;
        private final int height;

        private TextLayout(List<PositionedTextToken> tokens, int height) {
            this.tokens = tokens;
            this.height = height;
        }
    }

    /*
    private static class JeiFocusData {
        private final ItemStack stack;
        private final RecipeIngredientRole role;

        private JeiFocusData(ItemStack stack, RecipeIngredientRole role) {
            this.stack = stack;
            this.role = role;
        }
    }
     */

    /*
    private static <T> Optional<IClickableIngredient<?>> createClickableIngredient(
            IJeiRuntime runtime,
            ITypedIngredient<T> typedIngredient,
            Rect2i area
    ) {
        return runtime.getIngredientManager()
                .createClickableIngredient(typedIngredient.getType(), typedIngredient.getIngredient(), area, true)
                .map(clickable -> (IClickableIngredient<?>) clickable);
    }
     */

    private List<NecronomiconEntry> getAvailableEntries() {

        if (selectedCategory == null) return List.of();

        CodexTierData tierData = getBookItem().getComponents().get(ModDataComponentTypes.CODEX_TIER.get());
        int playerTier = tierData != null ? tierData.getTier() : 0;

        List<NecronomiconEntry> list = new ArrayList<>();

        for (int i = 0; i < selectedCategory.entries.size(); i++) {

            if (selectedCategory.tiers.get(i) <= playerTier) {

                String entryId = selectedCategory.entries.get(i).id;

                NecronomiconEntry entry = entryList.stream()
                        .filter(e -> e.id.equals(entryId))
                        .findFirst()
                        .orElse(null);

                if (entry != null) list.add(entry);
            }
        }

        return list;
    }

    private void applyLayer(GuiGraphicsExtractor guiGraphics, int layer) {
        while (currentLayer < layer) {
            guiGraphics.nextStratum();
            currentLayer++;
        }
    }

    private int guiScale(int value) {
        return (int)((value / 3) * Minecraft.getInstance().getWindow().getGuiScale());
    }
}
