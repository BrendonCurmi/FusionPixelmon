package me.fusiondev.fusionpixelmon.api.ui;

import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * The superclass base for the shop implementations.
 */
public abstract class BaseShop {
    protected Shops shops;
    /**
     * Whether the shop/option has a custom purchase summary
     * defined using {@link #purchaseSummary(Shops.Options, Object)}.
     */
    private boolean hasPurchaseSummary;

    public boolean hasPurchaseSummary() {
        return hasPurchaseSummary;
    }

    protected BaseShop(Shops shops) {
        this(shops, false);
    }

    protected BaseShop(Shops shops, boolean hasPurchaseSummary) {
        this.shops = shops;
        this.hasPurchaseSummary = hasPurchaseSummary;
        priceSummaries();
    }

    /**
     * Method variable for getting the option of that Shop.
     *
     * @return the option of the shop.
     */
    public abstract Shops.Options getOption();

    /**
     * Builds the shop page.
     *
     * @return the shop page object.
     */
    public abstract InvPage buildPage();

    /**
     * Gets the price of the specified key from the shop config, or the specified default price if cannot.
     * @param key the config key.
     * @param defaultPrice the default price.
     * @return the price of the key from the shop config; or the defaultPrice if cant.
     */
    public int getPriceOf(String key, int defaultPrice) {
        return shops.getPriceOf(getOption(), key, defaultPrice);
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
    public List<String> purchaseSummary(Shops.Options key, Object value) {
        return null;
    }

    /**
     * The default item to show when displaying what the player is purchasing.
     */
    public static AbstractItemType DEFAULT_SELECTED_ITEM_TYPE;
    /**
     * The item that represents an empty slot.
     */
    public static InvItem EMPTY_ITEM;

        /*static {
            ItemStack emptyStack = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
            emptyStack.offer(Keys.DYE_COLOR, DyeColors.BLACK);
            EMPTY_ITEM = new InvItem(SpongeAdapter.adapt(emptyStack), "");
        }*/

    public static AbstractItemStack backItemStack;
    public static AbstractItemStack resetItemStack;
    public static AbstractItemStack infoItemStack;


    public class Builder {
        private String title;
        private String id;
        private int rows;

        private int backSlot = -1;

        private int resetSlot = -1;
//        InvItem resetItem = new InvItem(SpongeAdapter.adapt(PixelmonAPI.getPixelmonItemStack("trash_can")), "§4§lReset options");

        private int infoSlot = -1;
        //private ItemType infoItemStack = ItemTypes.PAPER;
        private InvItem infoItem;

        private int selectedSlot = -1;
        //private ItemStack selectedItemStack;
        private AbstractItemStack selectedItemStack;
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

        public Builder setSelectedItem(InvPage page, AbstractItemType itemType, String name, String... lore) {
            InvItem item = new InvItem(itemType, name);
            item.setLore(lore);
            page.setItem(selectedSlot, item);
            return this;
        }

        public Builder setSelectedItem(AbstractItemType itemType) {
            this.selectedItemStack = itemType.to();
            return this;
        }

        public Builder setSelectedItem(AbstractItemStack itemStack) {
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
            page.getEventHandler().add(Event.CLOSE_INVENTORY, (event, player) -> {
                Shops.resetSelectedOptions(player, false);
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

            InvItem backItem = new InvItem(backItemStack, "§4§lBack to main menu");

            page.setItem(backSlot, backItem, event -> {
                shops.inv.openPage(shops.player, shops.SHOP_ID);//need to have changes persisted unless closed
            });

            if (resetSlot == -1) {
                int row = rows - 2;
                resetSlot = ((row + 1) * 9) - 1;
            }
            InvItem resetItem = new InvItem(resetItemStack, "§4§lReset options");
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
