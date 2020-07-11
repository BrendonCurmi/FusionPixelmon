package me.fusiondev.fusionpixelmon.spigot.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.fusiondev.fusionpixelmon.api.AbstractConfig;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.economy.BankAPI;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Event;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.impl.pixelmon.PokemonWrapper;
import me.fusiondev.fusionpixelmon.spigot.SpigotAdapter;
import me.fusiondev.fusionpixelmon.spigot.api.pixelmon.PixelmonAPI;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotInvInventory;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotItemStack;
import me.fusiondev.fusionpixelmon.spigot.impl.inventory.SpigotItemType;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpigotShops extends Shops {

    static {
        ItemStack emptyStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getWoolData());
        BaseShop.EMPTY_ITEM = new InvItem(SpigotAdapter.adapt(emptyStack), "");


        BaseShop.DEFAULT_SELECTED_ITEM_TYPE = new SpigotItemType(Material.BARRIER);


        BaseShop.backItemStack = new SpigotItemStack(PixelmonAPI.getPixelmonItemStack("eject_button"));
        BaseShop.resetItemStack = new SpigotItemStack(PixelmonAPI.getPixelmonItemStack("trash_can"));
        BaseShop.infoItemStack = new SpigotItemStack(new ItemStack(Material.PAPER));
    }

    public SpigotShops(AbstractPlayer player) {
        super(player);

        for (Options options : Options.values()) {
            Shops.Options opt = Shops.Options.valueOf(options.name());
            opt.setShopClass(options.getShopClass());
            opt.setItemStack(SpigotAdapter.adapt(options.getItemStack()));
        }
    }

    @Override
    public AbstractConfig getShopConfig(Shops.Options option) {
/*        return SpigotFusionPixelmon.getInstance()
                .getConfig()
                .getPokeDesignerConfig()
                .getShopNamed(option.name().toLowerCase());*/
        return null;
    }

    @Override
    public int getPriceOf(Shops.Options option, String key, int defaultPrice) {
        //PokeDesignerConfig.ShopConfig shop = getShopConfig(option);
        //return shop != null ? shop.getPrices().getOrDefault(key, defaultPrice) : defaultPrice;
        return 0;
    }

    @Override
    protected void initShops() {
//        PokeDesignerConfig config = SpigotFusionPixelmon.getInstance().getConfig().getPokeDesignerConfig();
        for (Shops.Options option : Shops.Options.values()) {
//            if (config.existsShop(option.name().toLowerCase()) && config.getShopNamed(option.name().toLowerCase()).isEnabled()) {
                try {
                    shops.putIfAbsent(option, option.getShopClass().getDeclaredConstructor(Shops.class).newInstance(this));
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
//            }
        }
    }

    @Override
    public void launch(Pokemon pokemon, String guiTitle) {
        this.inv = new SpigotInvInventory();
        this.pages = new ArrayList<>();
        this.pokemon = pokemon;

        //PokeDesignerConfig config = SpigotFusionPixelmon.getInstance().getConfig().getPokeDesignerConfig();
        //bank = (Sponge.getServiceManager().isRegistered(EconomyService.class) && config.useCurrency()) ? new EconomyProvider(config.getCurrency()) : new BankAPI(player);
        bank = new BankAPI(player);

        InvPage pagePokeEditor = new InvPage("§8" + guiTitle, SHOP_ID, 6);
        /*pagePokeEditor.setInteractInventoryEventListener(event -> {
            if (event instanceof InteractInventoryEvent.Close) {
                resetSelectedOptions(false);
            }
        });*/
        pagePokeEditor.getEventHandler().add(Event.CLOSE_INVENTORY, (event, player) -> {
            resetSelectedOptions(false);
        });

        ItemStack emptyStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getWoolData());
        InvItem emptyItem = new InvItem(SpigotAdapter.adapt(emptyStack), "");

        InvItem airItem = new InvItem(SpigotAdapter.adapt(new ItemStack(Material.AIR)), "");

        ItemStack confirmInvStack = new ItemStack(Material.INK_SACK, 1, DyeColor.LIME.getDyeData());
        InvItem confirmInvItem = new InvItem(SpigotAdapter.adapt(confirmInvStack), "§a§lConfirm");
        confirmInvItem.setLore("This will take you to", "the final checkout page.");

        ItemStack cancelInvStack = new ItemStack(Material.INK_SACK, 1, DyeColor.RED.getDyeData());
        InvItem cancelInvItem = new InvItem(SpigotAdapter.adapt(cancelInvStack), "§4§lCancel");
        InvItem curr = new InvItem(SpigotAdapter.adapt(PixelmonAPI.getPixelmonItemStack("grass_gem")), "§2Current Balance: §a" + bank.balance(player));

        IPokemonWrapper pokemonWrapper = new PokemonWrapper(pokemon);
        InvItem pokeItem = new InvItem(SpigotAdapter.adapt(PixelmonAPI.getPokeSprite(pokemon, true)), "§b§lSelected Pokemon");
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
            pageCheckout.setBackground(emptyItem);
            pageCheckout.setItemRange(10, 16, airItem);
            pageCheckout.setItemRange(28, 34, airItem);
            pageCheckout.setItem(29, cancelInvItem, event1 -> inv.openPage(player, pagePokeEditor));
            InvItem confirmInvItem1 = confirmInvItem.copyItem();

            int totalCost = calculateCost();

            if (bank.canAfford(player, totalCost)) {
                //todo confirmInvItem1.setKey(Keys.DYE_COLOR, DyeColors.LIME);
                confirmInvItem1.setLore(
                        "Your total cost is: §c" + bank.getCurrencySymbol(totalCost) + "§7.",
                        "",
                        "Clicking this button will confirm your purchase.",
                        "Once clicked, changes cannot be reversed.",
                        "",
                        "Your updated balance will be §a" + bank.getCurrencySymbol(bank.balance(player).intValue() - totalCost) + "§7."
                );
            } else {
                //todo confirmInvItem1.setKey(Keys.DYE_COLOR, DyeColors.GRAY);
                confirmInvItem1.setLore(
                        "Your total cost is: §c" + bank.getCurrencySymbol(totalCost) + "§7.",
                        "",
                        "§c§lYou are not able to make this purchase."
                );
            }

            pageCheckout.setItem(33, confirmInvItem1, event1 -> {
                if (!bank.canAfford(player, totalCost)) {
                    player.sendMessage("§cYou are not able to make this transaction");
                    //((ClickInventoryEvent) (event)).setCancelled(true);
                    //todo handle this
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
                player.sendMessage("§aSuccessfully edited your Pokemon!");
            });
            pageCheckout.setItem(31, curr);


            InvItem purchaseItem = new InvItem(SpigotAdapter.adapt(Material.PAPER), "§a§lPurchasing");
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

    public enum Options {
        LEVEL(LevelShop.class, PixelmonAPI.getPixelmonItemStack("rare_candy")),
        ABILITY(AbilityShop.class, PixelmonAPI.getPixelmonItemStack("ability_capsule")),
        NATURE(NatureShop.class, PixelmonAPI.getPixelmonItemStack("ever_stone")),
        IVEV(IVEVShop.class, PixelmonAPI.getPixelmonItemStack("destiny_knot")),
        GENDER(GenderShop.class, PixelmonAPI.getPixelmonItemStack("full_incense")),
        GROWTH(GrowthShop.class, new ItemStack(Material.INK_SACK, 1, DyeColor.WHITE.getDyeData())),
        SHINY(ShinyShop.class, PixelmonAPI.getPixelmonItemStack("light_ball")),
        POKEBALL(PokeballShop.class, PixelmonAPI.getPixelmonItemStack("poke_ball")),
        FORM(FormShop.class, PixelmonAPI.getPixelmonItemStack("meteorite")),
        EVOLUTION(EvolutionShop.class, PixelmonAPI.getPixelmonItemStack("fire_stone")),
        NICK(NickShop.class, PixelmonAPI.getPixelmonItemStack("ruby"));

        private Class<? extends BaseShop> shopClass;
        private ItemStack itemStack;

        Options(Class<? extends BaseShop> shopClass, ItemStack itemStack) {
            this.shopClass = shopClass;
            this.itemStack = itemStack;
        }

        public Class<? extends BaseShop> getShopClass() {
            return shopClass;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }
}
