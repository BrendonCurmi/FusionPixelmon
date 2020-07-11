package me.fusiondev.fusionpixelmon.api.ui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.fusiondev.fusionpixelmon.api.AbstractConfig;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.economy.IEconomyProvider;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;

import java.util.*;

public abstract class Shops {

    /**
     * The player currently using the Shop
     */
    protected AbstractPlayer player;

    /**
     * The pokemon the player is currently editing.
     */
    public Pokemon pokemon;

    /**
     * The inventory object for the currently open inventory.
     */
    protected InvInventory inv;

    /**
     * The ID of the shop inventory page.
     */
    protected final String SHOP_ID = "pokeeditor";

    protected List<InvPage> pages;// todo check if this is even needed

    /**
     * The inventory GUI page for the main shop.
     */
//    InvPage pagePokeEditor;// todo can be converted into local variable

    /**
     * The player's bank account.
     */
    protected IEconomyProvider bank;

    public Shops(AbstractPlayer player) {
        this.player = player;
        if (!playerSelectedOptions.containsKey(player.getUniqueId())) {
            playerSelectedOptions.put(player.getUniqueId(), new HashMap<>());
        }
    }

    // also depends on which poke slot!/which pokemon is selected
    /**
     * Stores the selected option/value pairs, for each concurrent player.
     */
    public static HashMap<UUID, HashMap<Options, Object>> playerSelectedOptions = new HashMap<>();

    /**
     * Gets the option/value pairs the current player has selected.
     *
     * @return the option/value pairs the current player has selected.
     */
    public HashMap<Options, Object> getSelectedOptions() {
        return playerSelectedOptions.get(player.getUniqueId());
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
    public static void resetSelectedOptions(AbstractPlayer player, boolean closeInv) {
        playerSelectedOptions.remove(player.getUniqueId());
        playerSelectedOptions.put(player.getUniqueId(), new HashMap<>());// todo can only initialize if needed
        if (closeInv) player.closeInventory();
    }


    /**
     * Maps the {@link Options} objects to their {@link BaseShop shop} implementations.
     */
    public HashMap<Options, BaseShop> shops = new HashMap<>();

    /**
     * Creates and adds the shops.
     */
    protected abstract void initShops();

    /**
     * Lists the different shop options with their properties to show in the main shop.
     */
    public enum Options {
        LEVEL(11, "Level", "level"),
        ABILITY(13, "Ability", "ability"),
        NATURE(15, "Nature", "nature"),
        IVEV(20, "IVs/EVs", "IVs/EVs"),
        GENDER(22, "Gender", "gender"),
        GROWTH(24, "Growth", "growth"),
        SHINY(29, "Shiny", "shininess"),
        POKEBALL(31, "Pokeball", "pokeball"),
        FORM(33, "Form", "form"),
        EVOLUTION(4, "Evolution", "evolution"),
        NICK(2, "Nick", "nick colour and style");

        int slot;
        String name;
        String modifyWhat;

        Class<? extends BaseShop> shopClass;
        AbstractItemStack itemStack;

        Options(int slot, String name, String modifyWhat) {
            this.slot = slot;
            this.name = name;
            this.modifyWhat = modifyWhat;
        }

        public int getSlot() {
            return slot;
        }

        public String getName() {
            return name;
        }

        public String getModifyWhat() {
            return modifyWhat;
        }


        public Class<? extends BaseShop> getShopClass() {
            return shopClass;
        }

        public void setShopClass(Class<? extends BaseShop> shopClass) {
            this.shopClass = shopClass;
        }

        public AbstractItemStack getItemStack() {
            return itemStack;
        }

        public void setItemStack(AbstractItemStack itemStack) {
            this.itemStack = itemStack;
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
    public abstract void launch(Pokemon pokemon, String guiTitle);


    /**
     * Gets the config object of the Shop.
     *
     * @return the config object of the shop.
     */
    public abstract AbstractConfig getShopConfig(Options option);

    /**
     * Gets the price of the specified key from the shop config, or the specified
     * default price if cannot.
     *
     * @param key          the config key.
     * @param defaultPrice the default price.
     * @return the price of the key from the shop config; or the defaultPrice if cant.
     */
    public abstract int getPriceOf(Options option, String key, int defaultPrice);
}
