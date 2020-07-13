package me.FusionDev.FusionPixelmon.sponge.modules.pokedesigner.gui;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;

public class ShinyShop extends BaseShop {

    public ShinyShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.SHINY;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Shininess Modification", "pokeeditor-shiny", 5)
                .setInfoItemData("Shiny Info",
                        "To select the shininess of",
                        "your Pokemon simply select one",
                        "of the options on the right.")
                .setSelectedItemName("Selected Shininess")
                .setSelectedOption(getOption());
        InvPage page = builder.build();

        InvItem item1 = new InvItem(FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType("light_ball"), "§6§lShiny");
        item1.setLore("Click here to select the", "§6Shiny §7option.");
        page.setItem(21, item1, event -> {
            if (!shops.pokemon.isShiny()) shops.getSelectedOptions().put(getOption(), true);
            else shops.getSelectedOptions().remove(getOption());
            builder.setSelectedItem(item1.getItemStack());
        });

        InvItem item2 = new InvItem(FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType("iron_ball"), "§8§lNon-Shiny");
        item2.setLore("Click here to select the", "§8Non-Shiny §7option.");
        page.setItem(23, item2, event -> {
            if (shops.pokemon.isShiny()) shops.getSelectedOptions().put(getOption(), false);
            else shops.getSelectedOptions().remove(getOption());
            builder.setSelectedItem(item2.getItemStack());
        });
        return page;
    }

    @Override
    public int prices(Object value) {
        return ((boolean) value) ? getPriceOf(ConfigKeys.ADD, 4000) : getPriceOf(ConfigKeys.REMOVE, 125);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Add Shininess", getPriceOf(ConfigKeys.ADD, 4000));
        addPriceSummary("Remove Shininess", getPriceOf(ConfigKeys.REMOVE, 125));
    }

    @Override
    public void purchaseAction(Object value) {
        if (value instanceof Boolean) {
            boolean set = (boolean) value;
            boolean isShiny = shops.pokemon.isShiny();
            if (set != isShiny) {
                shops.pokemon.setShiny(set);
            }
        }
    }

    private static class ConfigKeys {
        static final String ADD = "add";
        static final String REMOVE = "remove";
    }
}
