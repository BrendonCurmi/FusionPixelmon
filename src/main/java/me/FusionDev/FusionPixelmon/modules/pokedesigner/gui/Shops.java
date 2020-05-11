package me.FusionDev.FusionPixelmon.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.FusionDev.FusionPixelmon.FusionPixelmon;
import me.FusionDev.FusionPixelmon.api.BankAPI;
import me.FusionDev.FusionPixelmon.api.economy.EconomyProvider;
import me.FusionDev.FusionPixelmon.api.economy.IEconomyProvider;
import me.FusionDev.FusionPixelmon.modules.pokedesigner.config.PokeDesignerConfig;
import me.FusionDev.FusionPixelmon.api.pixelmon.PixelmonAPI;
import me.FusionDev.FusionPixelmon.api.inventory.InvInventory;
import me.FusionDev.FusionPixelmon.api.inventory.InvItem;
import me.FusionDev.FusionPixelmon.api.inventory.InvPage;
import me.FusionDev.FusionPixelmon.api.pixelmon.PokeData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Shops {

    /**
     * The player currently using the Shop
     */
    private Player player;

    /**
     * The pokemon the player is currently editing.
     */
    Pokemon pokemon;

    /**
     * The inventory object for the currently open inventory.
     */
    private InvInventory inv;

    /**
     * The ID of the shop inventory page.
     */
    private final String SHOP_ID = "pokeeditor";

    private List<InvPage> pages;// todo check if this is even needed

    /**
     * The inventory GUI page for the main shop.
     */
    InvPage pagePokeEditor;// todo can be converted into local variable

    /**
     * The player's bank account.
     */
    private IEconomyProvider bank;

    public Shops(Player player) {
        this.player = player;
        if (!playerSelectedOptions.containsKey(player)) {
            playerSelectedOptions.put(player, new HashMap<>());
        }
    }

    // also depends on which poke slot!/which pokemon is selected
    /**
     * Stores the selected option/value pairs, for each concurrent player.
     */
    public static HashMap<Player, HashMap<Options, Object>> playerSelectedOptions = new HashMap<>();

    /**
     * Gets the option/value pairs the current player has selected.
     *
     * @return the option/value pairs the current player has selected.
     */
    public HashMap<Options, Object> getSelectedOptions() {
        return playerSelectedOptions.get(player);
    }

    /**
     * Resets (deletes and creates new empty store for) the current player's selected
     * option/value pairs, and selectively closes the player's inventory.
     *
     * @param closeInv whether the player's inventory should be closed (exiting GUI).
     */
    public void resetSelectedOptions(boolean closeInv) {
        resetSelectedOptions(player, closeInv);
    }

    /**
     * Resets (deletes and creates new empty store for) the specified player's selected
     * option/value pairs, and selectively closes the player's inventory.
     *
     * @param player   the player.
     * @param closeInv whether the player's inventory should be closed (exiting GUI).
     */
    public static void resetSelectedOptions(Player player, boolean closeInv) {
        playerSelectedOptions.remove(player);
        playerSelectedOptions.put(player, new HashMap<>());// todo can only initialize if needed
        if (closeInv) player.closeInventory();
    }


    /**
     * Maps the {@link Options} objects to their {@link BaseShop shop} implementations.
     */
    HashMap<Options, BaseShop> shops = new HashMap<>();

    /**
     * Creates and adds the shops.
     */
    private void initShops() {
        PokeDesignerConfig config = FusionPixelmon.getInstance().getConfig().getPokeDesignerConfig();
        for (Options option : Options.values()) {
            if (config.existsShop(option.name().toLowerCase()) && config.getShopNamed(option.name().toLowerCase()).isEnabled()) {
                try {
                    shops.putIfAbsent(option, option.getShopClass().getDeclaredConstructor(Shops.class).newInstance(this));
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Lists the different shop options with their properties to show in the main shop.
     */
    public enum Options {
        LEVEL(LevelShop.class, 11, PixelmonAPI.getPixelmonItemStack("rare_candy"), "Level", "level"),
        ABILITY(AbilityShop.class, 13, PixelmonAPI.getPixelmonItemStack("ability_capsule"), "Ability", "ability"),
        NATURE(NatureShop.class, 15, PixelmonAPI.getPixelmonItemStack("ever_stone"), "Nature", "nature"),
        IVEV(IVEVShop.class, 20, PixelmonAPI.getPixelmonItemStack("destiny_knot"), "IVs/EVs", "IVs/EVs"),
        GENDER(GenderShop.class, 22, PixelmonAPI.getPixelmonItemStack("full_incense"), "Gender", "gender"),
        GROWTH(GrowthShop.class, 24, ItemStack.builder().itemType(ItemTypes.DYE).add(Keys.DYE_COLOR, DyeColors.WHITE).build(), "Growth", "growth"),
        SHINY(ShinyShop.class, 29, PixelmonAPI.getPixelmonItemStack("light_ball"), "Shiny", "shininess"),
        POKEBALL(PokeballShop.class, 31, PixelmonAPI.getPixelmonItemStack("poke_ball"), "Pokeball", "pokeball"),
        FORM(FormShop.class, 33, PixelmonAPI.getPixelmonItemStack("meteorite"), "Form", "form"),
        EVOLUTION(EvolutionShop.class, 4, PixelmonAPI.getPixelmonItemStack("fire_stone"), "Evolution", "evolution"),
        NICK(NickShop.class, 2, PixelmonAPI.getPixelmonItemStack("ruby"), "Nick", "nick colour");

        Class<? extends Shops.BaseShop> shopClass;
        int slot;
        ItemStack itemStack;
        String name;
        String modifyWhat;

        Options(Class<? extends Shops.BaseShop> shopClass, int slot, ItemStack itemStack, String name, String modifyWhat) {
            this.shopClass = shopClass;
            this.slot = slot;
            this.itemStack = itemStack;
            this.name = name;
            this.modifyWhat = modifyWhat;
        }

        public Class<? extends BaseShop> getShopClass() {
            return shopClass;
        }
    }


    /**
     * Calculates the total cost of all the options that the player has selected.
     *
     * @return the total cost of the options the player has selected.
     */
    public int calculateCost() {
        int totalCost = 0;
        for (Map.Entry<Options, Object> entry : getSelectedOptions().entrySet()) {
            if (!shops.containsKey(entry.getKey())) continue;
            totalCost += shops.get(entry.getKey()).prices(entry.getValue());
        }
        return totalCost;
    }

    /**
     * Launch the Shop GUI to display options, other shops, etc.
     *
     * @param pokemon the selected pokemon.
     */
    public void launch(Pokemon pokemon, String guiTitle) {
        this.inv = new InvInventory();
        this.pages = new ArrayList<>();
        this.pokemon = pokemon;

        PokeDesignerConfig config = FusionPixelmon.getInstance().getConfig().getPokeDesignerConfig();
        bank = (Sponge.getServiceManager().isRegistered(EconomyService.class) && config.useCurrency()) ? new EconomyProvider() : new BankAPI(player);

        pagePokeEditor = new InvPage("§8" + guiTitle, SHOP_ID, 6);
        pagePokeEditor.setInteractInventoryEventListener(event -> {
            if (event instanceof InteractInventoryEvent.Close) {
                resetSelectedOptions(false);
            }
        });

        InvItem emptyItem = new InvItem(ItemTypes.STAINED_GLASS_PANE, "").setKey(Keys.DYE_COLOR, DyeColors.BLACK);
        InvItem airItem = new InvItem(ItemStack.builder().itemType(ItemTypes.AIR).build(), "");
        InvItem confirmInvItem = new InvItem(ItemTypes.DYE, "§a§lConfirm").setKey(Keys.DYE_COLOR, DyeColors.LIME);
        confirmInvItem.setLore("This will take you to", "the final checkout page.");
        InvItem cancelInvItem = new InvItem(ItemTypes.DYE, "§4§lCancel").setKey(Keys.DYE_COLOR, DyeColors.RED);
        InvItem curr = new InvItem(PixelmonAPI.getPixelmonItemStack("grass_gem"), "§2Current Balance: §a" + bank.balance(player));

        PokeData pokeData = new PokeData(pokemon);
        InvItem pokeItem = new InvItem(PixelmonAPI.getPokeSprite(pokemon, true), "§b§lSelected Pokemon");
        pokeItem.setLoreWait(
                pokeData.getTitle(),
                pokeData.getAbility(),
                pokeData.getNature(),
                "",
                pokeData.getGender(),
                pokeData.getSize(),
                pokeData.getCaughtBall(),
                pokeData.getForm(),
                "",
                pokeData.getIVs(),
                "",
                pokeData.getEVs(),
                ""
        );
        if (!pokemon.getCustomTexture().isEmpty()) pokeItem.appendLore(pokeData.getCustomTexture());
        if (pokemon.getPokerus() != null) pokeItem.appendLore(pokeData.getPokerus());
        pokeItem.pushLore(true);

        // Left
        pagePokeEditor.setItem(9, airItem);
        pagePokeEditor.setItem(18, pokeItem);
        pagePokeEditor.setItem(27, airItem);

        // Center
        pagePokeEditor.setItemRange(2, 6, airItem);
        pagePokeEditor.setItemRange(11, 15, airItem);
        pagePokeEditor.setItemRange(20, 24, airItem);
        pagePokeEditor.setItemRange(29, 33, airItem);

        // Right

        // Init shops
        initShops();


        pagePokeEditor.setItem(17, confirmInvItem, event -> {
            InvPage pageCheckout = new InvPage("§8Checkout", "pokecheckout", 5);
            pageCheckout.setBackground(emptyItem);
            pageCheckout.setItemRange(10, 16, airItem);
            pageCheckout.setItemRange(28, 34, airItem);
            pageCheckout.setItem(29, cancelInvItem, event1 -> inv.openPage(player, pagePokeEditor));
            InvItem confirmInvItem1 = confirmInvItem.copyItem();

            int totalCost = calculateCost();

            if (bank.canAfford(player, totalCost)) {
                confirmInvItem1.setKey(Keys.DYE_COLOR, DyeColors.LIME);
                confirmInvItem1.setLore(
                        "Your total cost is: §c" + bank.getCurrencySymbol(totalCost) + "§7.",
                        "",
                        "Clicking this button will confirm your purchase.",
                        "Once clicked, changes cannot be reversed.",
                        "",
                        "Your updated balance will be §a" + bank.getCurrencySymbol(bank.balance(player).intValue() - totalCost) + "§7."
                );
            } else {
                confirmInvItem1.setKey(Keys.DYE_COLOR, DyeColors.GRAY);
                confirmInvItem1.setLore(
                        "Your total cost is: §c" + bank.getCurrencySymbol(totalCost) + "§7.",
                        "",
                        "§c§lYou are not able to make this purchase."
                );
            }

            pageCheckout.setItem(33, confirmInvItem1, event1 -> {
                if (!bank.canAfford(player, totalCost)) {
                    player.sendMessage(Text.of("§cYou are not able to make this transaction"));
                    event.setCancelled(true);
                    return;
                }

                bank.withdraw(player, totalCost);
                // might not need to reset while closing, cause closing event handles it
                HashMap<Options, Object> hash = new HashMap<>(getSelectedOptions());
                resetSelectedOptions(true);
                player.closeInventory();
                for (Map.Entry<Options, Object> e : hash.entrySet()) {
                    Object result = e.getValue();
                    shops.get(e.getKey()).purchaseAction(result);
                }
                player.sendMessage(Text.of(TextColors.GREEN, "Successfully edited your Pokemon!"));
            });
            pageCheckout.setItem(31, curr);


            InvItem purchaseItem = new InvItem(ItemTypes.PAPER, "§a§lPurchasing");
            List<String> lore = new ArrayList<>();
            for (Map.Entry<Options, Object> entry : getSelectedOptions().entrySet()) {
                if (shops.get(entry.getKey()).hasPurchaseSummary) {
                    lore.addAll(shops.get(entry.getKey()).purchaseSummary(entry.getKey(), entry.getValue()));
                } else {
                    lore.add(entry.getKey().name + ": §e" + entry.getValue().toString());
                }
            }
            purchaseItem.setLore(lore);
            pageCheckout.setItem(14, purchaseItem);
            pageCheckout.setItem(12, pokeItem);
            inv.openPage(player, pageCheckout);
        });
        pagePokeEditor.setItem(26, airItem);
        pagePokeEditor.setItem(35, cancelInvItem, event -> {
            resetSelectedOptions(true);
            player.closeInventory();
        });

        // Bottom
        pagePokeEditor.setRunnable(() -> {
            char col = 'c';
            if (bank.balance(player).intValue() > calculateCost()) col = 'a';
            curr.setLore("§" + col + "Current Cost: " + calculateCost());
            pagePokeEditor.setItem(49, curr);
        });

        pagePokeEditor.setBackground(emptyItem);

        // Items
        pages.add(pagePokeEditor);

        for (Map.Entry<Options, BaseShop> entry : shops.entrySet()) {
            InvItem item = new InvItem(ItemStack.builder().fromItemStack(entry.getKey().itemStack).build(), "§3§l" + entry.getKey().name);
            item.setLore(
                    "§7Click here if you wish to",
                    "modify your Pokemon's " + entry.getKey().modifyWhat + ".",
                    "",
                    "§aPrices:",
                    entry.getValue().getPricesSummary()
            );
            InvPage page = entry.getValue().buildPage();
            pagePokeEditor.setItem(entry.getKey().slot, item, event -> inv.openPage(player, page));
            pages.add(page);
        }
        inv.add(pages);
        inv.openPage(player, pagePokeEditor);
    }

    /**
     * The superclass base for the shop implementations.
     */
    public static abstract class BaseShop {
        Shops shops;
        /**
         * Whether the shop/option has a custom purchase summary
         * defined using {@link #purchaseSummary(Options, Object)}.
         */
        boolean hasPurchaseSummary;

        BaseShop(Shops shops) {
            this(shops, false);
        }

        BaseShop(Shops shops, boolean hasPurchaseSummary) {
            this.shops = shops;
            this.hasPurchaseSummary = hasPurchaseSummary;
            priceSummaries();
        }

        /**
         * Method variable for getting the option of that Shop.
         *
         * @return the option of the shop.
         */
        public abstract Options getOption();

        /**
         * Builds the shop page.
         *
         * @return the shop page object.
         */
        public abstract InvPage buildPage();

        /**
         * Gets the config object of the Shop.
         *
         * @return the config object of the shop.
         */
        public PokeDesignerConfig.ShopConfig getShopConfig() {
            return FusionPixelmon.getInstance().getConfig().getPokeDesignerConfig().getShopNamed(getOption().name().toLowerCase());
        }

        /**
         * Gets the price of the specified key from the shop config, or the specified
         * default price if cannot.
         *
         * @param key          the config key.
         * @param defaultPrice the default price.
         * @return the price of the key from the shop config; or the defaultPrice if cant.
         */
        public int getPriceOf(String key, int defaultPrice) {
            PokeDesignerConfig.ShopConfig shop = getShopConfig();
            return shop != null ? shop.getPrices().getOrDefault(key, defaultPrice) : defaultPrice;
        }

        /**
         * Lists and returns the amount/price the specified value costs.
         *
         * @param value the value the player has selected.
         * @return the price of the value.
         */
        public abstract int prices(Object value);

        /**
         * Generates the price summaries and adds them to the shop's item lore.
         * The {@link #addPriceSummary(String, int)} method is used to define
         * the summaries of the prices in this method.
         * This is called once in the shop's superclass constructor.
         */
        protected abstract void priceSummaries();

        /**
         * The item lore containing the price summaries of the shop.
         */
        private List<String> priceLore = new ArrayList<>();

        /**
         * Gets the price summaries lore of the shop.
         *
         * @return the price summaries lore of the shop.
         */
        public List<String> getPricesSummary() {
            return priceLore;
        }

        /**
         * Adds the specified item and its price in a pretty-formatted
         * summary to the shop's item lore.
         *
         * @param item  the selected item.
         * @param price the price of the item.
         */
        protected void addPriceSummary(String item, int price) {
            priceLore.add("  §7" + item + "§7: §a$" + price);
        }

        /**
         * Adds the specified item and its price in a pretty-formatted
         * summary to the shop's item lore.
         *
         * @param item  the selected item.
         * @param price the price of the item.
         */
        protected void addPriceSummary(String item, String price) {
            priceLore.add("  §7" + item + "§7: §a$" + price);
        }

        /**
         * This method is executed upon a successful purchase for each shop.
         *
         * @param value the value of the shop/option the player has purchased.
         */
        public abstract void purchaseAction(Object value);

        /**
         * Allows the overriding of the default purchase summary item
         * lore for this shop/option, with a custom purchase summary.
         *
         * @param key   the option the player is purchasing.
         * @param value the value the player is purchasing.
         * @return the overriding purchase summary for the shop option.
         */
        public List<String> purchaseSummary(Options key, Object value) {
            return null;
        }

        /**
         * The default item to show when displaying what the player is purchasing.
         */
        private static final ItemType DEFAULT_SELECTED_ITEM_TYPE = ItemTypes.BARRIER;
        /**
         * The item that represents an empty slot.
         */
        static final InvItem EMPTY_ITEM = new InvItem(ItemTypes.STAINED_GLASS_PANE, "").setKey(Keys.DYE_COLOR, DyeColors.BLACK);

        public class Builder {
            private String title;
            private String id;
            private int rows;

            private int backSlot = -1;
            InvItem backItem = new InvItem(PixelmonAPI.getPixelmonItemStack("eject_button"), "§4§lBack to main menu");

            private int resetSlot = -1;
            InvItem resetItem = new InvItem(PixelmonAPI.getPixelmonItemStack("trash_can"), "§4§lReset options");

            private int infoSlot = -1;
            private ItemType infoItemStack = ItemTypes.PAPER;
            private InvItem infoItem;

            private int selectedSlot = -1;
            private ItemStack selectedItemStack;
            private String selectedItemName = "Selected Option";

            public Builder(String title, String id, int rows) {
                this.title = title;
                this.id = id;
                this.rows = rows;
            }

            public Builder setBackSlot(int slot) {
                this.backSlot = slot;
                return this;
            }

            Runnable runnable;

            public Builder onReset(Runnable runnable) {
                this.runnable = runnable;
                return this;
            }

            public Builder setResetSlot(int slot) {
                this.resetSlot = slot;
                return this;
            }

            public Builder setInfoSlot(int slot) {
                this.infoSlot = slot;
                return this;
            }

            public Builder setInfoItemData(String name, String... lore) {
                infoItem = new InvItem(infoItemStack, "§3§l" + name);
                infoItem.setLore(lore);
                return this;
            }

            public Builder setSelectedSlot(int slot) {
                this.selectedSlot = slot;
                return this;
            }

            public Builder setSelectedItem(InvPage page, ItemType itemType, String name, String... lore) {
                InvItem item = new InvItem(itemType, name);
                item.setLore(lore);
                page.setItem(selectedSlot, item);
                return this;
            }

            public Builder setSelectedItem(ItemType itemType) {
                this.selectedItemStack = ItemStack.builder().itemType(itemType).build();
                return this;
            }

            public Builder setSelectedItem(ItemStack itemStack) {
                this.selectedItemStack = itemStack;
                return this;
            }

            public Builder setSelectedItemName(String name) {
                this.selectedItemName = name;
                return this;
            }

            private Shops.Options options;

            public Builder setSelectedOption(Shops.Options options) {
                this.options = options;
                return this;
            }

            boolean hasSelected = true;

            public Builder setSelected(boolean hasSelected) {
                this.hasSelected = hasSelected;
                return this;
            }

            boolean border;

            public Builder border(boolean border) {
                this.border = border;
                return this;
            }

            public InvPage build() {
                // todo check that rows is minimum of 2 due to right side options
                InvPage page = new InvPage("§cDesigner §7>> §8" + title, id, rows);
                page.setInteractInventoryEventListener(event -> {
                    if (event instanceof InteractInventoryEvent.Close) {
                        Shops.resetSelectedOptions((Player) event.getSource(), false);
                    }
                });


                if (border) {
                    page.setItemRange(0, 8, EMPTY_ITEM);
                    if (rows > 1) page.setItemRange((rows - 1) * 9, ((rows - 1) * 9) + 8, EMPTY_ITEM);
                    if (rows > 2) page.setItemRange((rows - 2) * 9, ((rows - 2) * 9) + 8, EMPTY_ITEM);
                } else {
                    page.setItemRange(0, 8, EMPTY_ITEM);
                    if (rows > 1) page.setItemRange((rows - 1) * 9, ((rows - 1) * 9) + 8, EMPTY_ITEM);

                    // Empty bars
                    for (int row = 1; row <= rows - 2; row++) {
                        page.setItem((row * 9) + 1, EMPTY_ITEM);
                        page.setItem((row * 9) + 7, EMPTY_ITEM);
                    }
                }


                if (backSlot == -1) {
                    int row = 1;
                    backSlot = ((row + 1) * 9) - 1;
                }
                page.setItem(backSlot, backItem, event -> {
                    shops.inv.openPage(shops.player, shops.SHOP_ID);//need to have changes persisted unless closed
                });

                if (resetSlot == -1) {
                    int row = rows - 2;
                    resetSlot = ((row + 1) * 9) - 1;
                }
                page.setItem(resetSlot, resetItem, event -> {
                    if (runnable != null) runnable.run();
                    shops.getSelectedOptions().remove(options);
                    selectedItemStack = null;
                });

                // Info Item
                if (infoSlot == -1) {
                    int row = rows - 2;
                    infoSlot = row * 9;
                }
                if (infoItem == null) {
                    infoItem = new InvItem(infoItemStack, "§3§lInfo");
                }
                page.setItem(infoSlot, infoItem);

                // Selected Item
                if (selectedSlot == -1) {
                    int row = 1;
                    selectedSlot = row * 9;
                }

                page.setRunnable(() -> {
                    InvItem selectedItem = (selectedItemStack != null) ? new InvItem(selectedItemStack, "§3§l" + selectedItemName) : new InvItem(DEFAULT_SELECTED_ITEM_TYPE, "§3§l" + selectedItemName);
                    Object value = shops.getSelectedOptions().getOrDefault(options, "N/A");
                    if (value instanceof Integer) {
                        if ((int) value < 0) value = "§c" + value;
                        else value = "§a+" + value;
                    }
                    selectedItem.setLore("Current: §e" + value);
                    page.setItem(selectedSlot, selectedItem);
                });

                //pagePokeEditor.setBackground(emptyItem);
                return page;
            }
        }
    }
}
