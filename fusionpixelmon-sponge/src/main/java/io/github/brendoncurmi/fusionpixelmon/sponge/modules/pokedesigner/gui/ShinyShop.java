package io.github.brendoncurmi.fusionpixelmon.sponge.modules.pokedesigner.gui;

import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeAdapter;
import io.github.brendoncurmi.fusionpixelmon.sponge.api.pixelmon.PixelmonAPI;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import org.spongepowered.api.item.inventory.ItemStack;

public class ShinyShop extends Shops.BaseShop {
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

        InvItem item1 = new InvItem(SpongeAdapter.adapt(PixelmonAPI.getPixelmonItemType("light_ball")), "§6§lShiny");
        item1.setLore("Click here to select the", "§6Shiny §7option.");
        page.setItem(21, item1, event -> {
            if (!shops.pokemon.isShiny()) shops.getSelectedOptions().put(getOption(), true);
            else shops.getSelectedOptions().remove(getOption());
            builder.setSelectedItem((ItemStack) item1.getItemStack().getRaw());
        });

        InvItem item2 = new InvItem(SpongeAdapter.adapt(PixelmonAPI.getPixelmonItemType("iron_ball")), "§8§lNon-Shiny");
        item2.setLore("Click here to select the", "§8Non-Shiny §7option.");
        page.setItem(23, item2, event -> {
            if (shops.pokemon.isShiny()) shops.getSelectedOptions().put(getOption(), false);
            else shops.getSelectedOptions().remove(getOption());
            builder.setSelectedItem((ItemStack) item2.getItemStack().getRaw());
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
