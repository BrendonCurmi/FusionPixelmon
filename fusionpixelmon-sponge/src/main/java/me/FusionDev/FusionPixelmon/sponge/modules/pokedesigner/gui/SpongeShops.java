package me.FusionDev.FusionPixelmon.sponge.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Event;
import me.fusiondev.fusionpixelmon.impl.pixelmon.PokemonWrapper;
import me.FusionDev.FusionPixelmon.sponge.SpongeAdapter;
import me.FusionDev.FusionPixelmon.sponge.SpongeFusionPixelmon;
import me.fusiondev.fusionpixelmon.api.economy.BankAPI;
import me.FusionDev.FusionPixelmon.sponge.api.economy.EconomyProvider;
import me.FusionDev.FusionPixelmon.sponge.impl.inventory.SpongeInvInventory;
import me.FusionDev.FusionPixelmon.sponge.impl.inventory.SpongeItemStack;
import me.FusionDev.FusionPixelmon.sponge.impl.inventory.SpongeItemType;
import me.FusionDev.FusionPixelmon.sponge.modules.pokedesigner.config.PokeDesignerConfig;
import me.fusiondev.fusionpixelmon.api.ui.Shops;//todo hi
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpongeShops extends Shops {

    static {
        ItemStack emptyStack = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
        emptyStack.offer(Keys.DYE_COLOR, DyeColors.BLACK);
        BaseShop.EMPTY_ITEM = new InvItem(SpongeAdapter.adapt(emptyStack), "");


        BaseShop.DEFAULT_SELECTED_ITEM_TYPE = new SpongeItemType(ItemTypes.BARRIER);


        BaseShop.backItemStack = FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("eject_button");
        BaseShop.resetItemStack = FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("trash_can");
        BaseShop.infoItemStack = new SpongeItemStack(ItemStack.builder().itemType(ItemTypes.PAPER).build());
    }

    public SpongeShops(AbstractPlayer player) {
        super(player);
    }

    @Override
    public PokeDesignerConfig.ShopConfig getShopConfig(Shops.Options option) {
        return SpongeFusionPixelmon.getInstance()
                .getConfig()
                .getPokeDesignerConfig()
                .getShopNamed(option.name().toLowerCase());
    }

    @Override
    public int getPriceOf(Shops.Options option, String key, int defaultPrice) {
        PokeDesignerConfig.ShopConfig shop = getShopConfig(option);
        return shop != null ? shop.getPrices().getOrDefault(key, defaultPrice) : defaultPrice;
    }

    @Override
    protected void initShops() {
        PokeDesignerConfig config = SpongeFusionPixelmon.getInstance().getConfig().getPokeDesignerConfig();
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

    @Override
    public void launch(Pokemon pokemon, String guiTitle) {
        this.inv = new SpongeInvInventory();
        this.pages = new ArrayList<>();
        this.pokemon = pokemon;

        PokeDesignerConfig config = SpongeFusionPixelmon.getInstance().getConfig().getPokeDesignerConfig();
        bank = (Sponge.getServiceManager().isRegistered(EconomyService.class) && config.useCurrency()) ? new EconomyProvider(config.getCurrency()) : new BankAPI(player);

        InvPage pagePokeEditor = new InvPage("§8" + guiTitle, SHOP_ID, 6);
        /*pagePokeEditor.setInteractInventoryEventListener(event -> {
            if (event instanceof InteractInventoryEvent.Close) {
                resetSelectedOptions(false);
            }
        });*/
        pagePokeEditor.getEventHandler().add(Event.CLOSE_INVENTORY, (event, player) -> {
            resetSelectedOptions(false);
        });

        ItemStack emptyStack = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
        emptyStack.offer(Keys.DYE_COLOR, DyeColors.BLACK);
        InvItem emptyItem = new InvItem(SpongeAdapter.adapt(emptyStack), "");

        InvItem airItem = new InvItem(SpongeAdapter.adapt(ItemStack.builder().itemType(ItemTypes.AIR).build()), "");

        ItemStack confirmInvStack = ItemStack.builder().itemType(ItemTypes.DYE).build();
        confirmInvStack.offer(Keys.DYE_COLOR, DyeColors.LIME);
        InvItem confirmInvItem = new InvItem(SpongeAdapter.adapt(confirmInvStack), "§a§lConfirm");
        confirmInvItem.setLore("This will take you to", "the final checkout page.");

        ItemStack cancelInvStack = ItemStack.builder().itemType(ItemTypes.DYE).build();
        cancelInvStack.offer(Keys.DYE_COLOR, DyeColors.RED);
        InvItem cancelInvItem = new InvItem(SpongeAdapter.adapt(cancelInvStack), "§4§lCancel");
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
                    player.sendMessage(Text.of("§cYou are not able to make this transaction"));
                    ((ClickInventoryEvent) (event)).setCancelled(true);
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
                player.sendMessage(Text.of(TextColors.GREEN, "Successfully edited your Pokemon!"));
            });
            pageCheckout.setItem(31, curr);


            InvItem purchaseItem = new InvItem(SpongeAdapter.adapt(ItemTypes.PAPER), "§a§lPurchasing");
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
}
