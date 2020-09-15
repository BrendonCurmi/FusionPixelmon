package me.fusiondev.fusionpixelmon.api.ui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.economy.IEconomyProvider;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import me.fusiondev.fusionpixelmon.api.ui.events.Event;
import me.fusiondev.fusionpixelmon.impl.pixelmon.PokemonWrapper;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.config.PokeDesignerConfig;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.AbilityShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.IVEVShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.LevelShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.NatureShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.GenderShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.GrowthShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.ShinyShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.PokeballShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.FormShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.EvolutionShop;
import me.fusiondev.fusionpixelmon.modules.pokedesigner.ui.NickShop;

import java.lang.reflect.InvocationTargetException;
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
     * The player's bank account.
     */
    protected IEconomyProvider<?, ?> bank;

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
    protected void initShops() {
        PokeDesignerConfig config = FusionPixelmon.getInstance().getConfiguration().getPokeDesignerConfig();
        for (Shops.Options option : Shops.Options.values()) {
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
        LEVEL(11, "Level", "level", LevelShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("rare_candy")),
        ABILITY(13, "Ability", "ability", AbilityShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("ability_capsule")),
        NATURE(15, "Nature", "nature", NatureShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("ever_stone")),
        IVEV(20, "IVs/EVs", "IVs/EVs", IVEVShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("destiny_knot")),
        GENDER(22, "Gender", "gender", GenderShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("full_incense")),
        GROWTH(24, "Growth", "growth", GrowthShop.class, FusionPixelmon.getRegistry().getItemTypesRegistry().DYE().to().setColour(DyeColor.WHITE)),
        SHINY(29, "Shiny", "shininess", ShinyShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("light_ball")),
        POKEBALL(31, "Pokeball", "pokeball", PokeballShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("poke_ball")),
        FORM(33, "Form", "form", FormShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("meteorite")),
        EVOLUTION(4, "Evolution", "evolution", EvolutionShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("fire_stone")),
        NICK(2, "Nick", "nick colour and style", NickShop.class, FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("ruby"));

        private int slot;
        private String name;
        private String modifyWhat;
        private Class<? extends BaseShop> shopClass;
        private AbstractItemStack itemStack;

        Options(int slot, String name, String modifyWhat, Class<? extends BaseShop> shopClass, AbstractItemStack itemStack) {
            this.slot = slot;
            this.name = name;
            this.modifyWhat = modifyWhat;
            this.shopClass = shopClass;
            this.itemStack = itemStack;
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

        public AbstractItemStack getItemStack() {
            return itemStack;
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

    public abstract IEconomyProvider<?, ?> getBank(PokeDesignerConfig config);

    /**
     * Launch the Shop GUI to display options, other shops, etc.
     *
     * @param pokemon the selected pokemon.
     */
    public void launch(Pokemon pokemon, String guiTitle) {
        this.inv = FusionPixelmon.getRegistry().getInvInventory();
        this.pages = new ArrayList<>();
        this.pokemon = pokemon;

        PokeDesignerConfig config = FusionPixelmon.getInstance().getConfiguration().getPokeDesignerConfig();
        bank = getBank(config);

        InvPage pagePokeEditor = new InvPage("§8" + guiTitle, SHOP_ID, 6);
        pagePokeEditor.getEventHandler().add(Event.CLOSE_INVENTORY, (event, player) -> resetSelectedOptions(false));

        Registry reg = FusionPixelmon.getRegistry();

        AbstractItemStack emptyStack = reg.getItemTypesRegistry().STAINED_GLASS_PANE().to().setColour(DyeColor.BLACK);
        InvItem emptyItem = new InvItem(emptyStack, "");

        InvItem airItem = new InvItem(reg.getItemTypesRegistry().AIR().to(), "");

        AbstractItemStack confirmInvStack = reg.getItemTypesRegistry().DYE().to().setColour(DyeColor.LIME);
        InvItem confirmInvItem = new InvItem(confirmInvStack, "§a§lConfirm");
        confirmInvItem.setLore("This will take you to", "the final checkout page.");

        AbstractItemStack cancelInvStack = reg.getItemTypesRegistry().DYE().to().setColour(DyeColor.RED);
        InvItem cancelInvItem = new InvItem(cancelInvStack, "§4§lCancel");
        InvItem curr = new InvItem(FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("grass_gem"), "§2Current Balance: §a" + bank.balance(player));

        IPokemonWrapper pokemonWrapper = new PokemonWrapper(pokemon);
        InvItem pokeItem = new InvItem(FusionPixelmon.getRegistry().getPixelmonUtils().getPokeSprite(pokemon, true), "§b§lSelected Pokemon");
        pokeItem.setLoreWait(
                pokemonWrapper.getTitle(),
                pokemonWrapper.getAbility(),
                pokemonWrapper.getNature(),
                "",
                pokemonWrapper.getGender(),
                pokemonWrapper.getSize(),
                pokemonWrapper.getCaughtBall(),
                pokemonWrapper.getForm(),
                "",
                pokemonWrapper.getIVs(),
                "",
                pokemonWrapper.getEVs(),
                ""
        );
        if (pokemonWrapper.hasTexture()) pokeItem.appendLore(pokemonWrapper.getTexture());
        if (pokemon.getPokerus() != null) pokeItem.appendLore(pokemonWrapper.getPokerus());
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
            pageCheckout.getEventHandler().add(Event.CLOSE_INVENTORY, (event1, player) -> resetSelectedOptions(false));
            pageCheckout.setBackground(emptyItem);
            pageCheckout.setItemRange(10, 16, airItem);
            pageCheckout.setItemRange(28, 34, airItem);
            pageCheckout.setItem(29, cancelInvItem, event1 -> inv.openPage(player, pagePokeEditor));
            InvItem confirmInvItem1 = confirmInvItem.copyItem();

            int totalCost = calculateCost();

            if (bank.canAfford(player, totalCost)) {
                confirmInvItem1.getItemStack().setColour(DyeColor.LIME);
                confirmInvItem1.setLore(
                        "Your total cost is: §c" + bank.getCurrencySymbol(totalCost) + "§7.",
                        "",
                        "Clicking this button will confirm your purchase.",
                        "Once clicked, changes cannot be reversed.",
                        "",
                        "Your updated balance will be §a" + bank.getCurrencySymbol(bank.balance(player).intValue() - totalCost) + "§7."
                );
            } else {
                confirmInvItem1.getItemStack().setColour(DyeColor.GRAY);
                confirmInvItem1.setLore(
                        "Your total cost is: §c" + bank.getCurrencySymbol(totalCost) + "§7.",
                        "",
                        "§c§lYou are not able to make this purchase."
                );
            }

            pageCheckout.setItem(33, confirmInvItem1, event1 -> {
                if (!bank.canAfford(player, totalCost)) {
                    player.sendMessage(Color.RED + "You are not able to make this transaction");
                    return;
                }

                bank.withdraw(player, totalCost);
                // might not need to reset while closing, cause closing event handles it
                HashMap<Shops.Options, Object> hash = new HashMap<>(getSelectedOptions());
                resetSelectedOptions(true);
                player.closeInventory();
                for (Map.Entry<Shops.Options, Object> e : hash.entrySet()) {
                    Object result = e.getValue();
                    shops.get(e.getKey()).purchaseAction(result);
                }
                player.sendMessage(Color.GREEN + "Successfully edited your Pokemon!");
            });
            pageCheckout.setItem(31, curr);


            InvItem purchaseItem = new InvItem(reg.getItemTypesRegistry().PAPER(), "§a§lPurchasing");
            List<String> lore = new ArrayList<>();
            for (Map.Entry<Shops.Options, Object> entry : getSelectedOptions().entrySet()) {
                if (shops.get(entry.getKey()).hasPurchaseSummary()) {
                    lore.addAll(shops.get(entry.getKey()).purchaseSummary(entry.getKey(), entry.getValue()));
                } else {
                    lore.add(entry.getKey().getName() + ": §e" + entry.getValue().toString());
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

        if (FusionPixelmon.getModule().equals("forge")) {
            curr.setLore(Color.DARK_GRAY + "[click to refresh]");
            pagePokeEditor.setItem(49, curr);
        }

        // Bottom
        pagePokeEditor.setRunnable(() -> {
            char col = 'c';
            if (bank.balance(player).intValue() > calculateCost()) col = 'a';
            if (FusionPixelmon.getModule().equals("forge"))
                curr.setLore("§" + col + "Current Cost: " + calculateCost(), Color.DARK_GRAY + "[click to refresh]");
            else
                curr.setLore("§" + col + "Current Cost: " + calculateCost());
            pagePokeEditor.setDynamicItem(49, curr);
        });

        pagePokeEditor.setBackground(emptyItem);

        // Items
        pages.add(pagePokeEditor);

        for (Map.Entry<Shops.Options, BaseShop> entry : shops.entrySet()) {
            InvItem item = new InvItem(entry.getKey().getItemStack(), "§3§l" + entry.getKey().getName());
            item.setLore(
                    "§7Click here if you wish to",
                    "modify your Pokemon's " + entry.getKey().getModifyWhat() + ".",
                    "",
                    "§aPrices:",
                    entry.getValue().getPricesSummary()
            );
            InvPage page = entry.getValue().buildPage();
            pagePokeEditor.setItem(entry.getKey().getSlot(), item, event -> inv.openPage(player, page));
            pages.add(page);
        }
        inv.add(pages);
        inv.openPage(player, pagePokeEditor);
    }


    /**
     * Gets the config object of the Shop.
     *
     * @return the config object of the shop.
     */
    public PokeDesignerConfig.ShopConfig getShopConfig(Shops.Options option) {
        return FusionPixelmon.getInstance()
                .getConfiguration()
                .getPokeDesignerConfig()
                .getShopNamed(option.name().toLowerCase());
    }

    /**
     * Gets the price of the specified key from the shop config, or the specified
     * default price if cannot.
     *
     * @param key          the config key.
     * @param defaultPrice the default price.
     * @return the price of the key from the shop config; or the defaultPrice if cant.
     */
    public int getPriceOf(Shops.Options option, String key, int defaultPrice) {
        PokeDesignerConfig.ShopConfig shop = getShopConfig(option);
        return shop != null ? shop.getPrices().getOrDefault(key, defaultPrice) : defaultPrice;
    }
}
