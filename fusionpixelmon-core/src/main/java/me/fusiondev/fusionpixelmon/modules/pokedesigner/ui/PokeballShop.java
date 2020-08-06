package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;

public class PokeballShop extends BaseShop {
    public PokeballShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.POKEBALL;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Pokeball Modification", "pokeeditor-pokeball", 6)
                .setInfoItemData("Pokeball Info",
                        "To pick the Pokeball of your Pokemon",
                        "simply select one of the Pokeballs",
                        "on the right.")
                .setSelectedItemName("Selected Pokeball")
                .setSelectedSlot(46)
                .setInfoSlot(48)
                .setResetSlot(50)
                .setBackSlot(52)
                .border(true)
                .setSelectedOption(getOption());

        InvPage page = builder.build();

        //page.setBackground(emptyItem);

        int slot = 9;
        for (EnumPokeballs pokeballs : EnumPokeballs.values()) {
            if (pokeballs == EnumPokeballs.BeastBall && !IPokemonWrapper.isUltraBeast(shops.pokemon)) continue;
            InvItem item = new InvItem(FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType(pokeballs.getFilenamePrefix()), "§3§l" + pokeballs.name());
            page.setItem(slot, item, event -> {
                if (shops.pokemon.getCaughtBall() != pokeballs) shops.getSelectedOptions().put(getOption(), pokeballs);
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item.getItemStack());
            });
            slot++;
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        EnumPokeballs ball = (EnumPokeballs) value;
        int cost = getPriceOf(ConfigKeyConstants.REGULAR, 300);
        if (ball == EnumPokeballs.PremierBall || ball == EnumPokeballs.GSBall)
            cost = getPriceOf(ConfigKeyConstants.SPECIAL, 400);
        else if (ball == EnumPokeballs.MasterBall || ball == EnumPokeballs.ParkBall)
            cost = getPriceOf(ConfigKeyConstants.GOD, 600);
        else if (ball == EnumPokeballs.BeastBall)
            cost = getPriceOf(ConfigKeyConstants.BEAST, 800);
        return cost;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Most Pokeballs", getPriceOf(ConfigKeyConstants.REGULAR, 300));
        addPriceSummary(EnumPokeballs.PremierBall.name(), getPriceOf(ConfigKeyConstants.SPECIAL, 400));
        addPriceSummary(EnumPokeballs.GSBall.name(), getPriceOf(ConfigKeyConstants.SPECIAL, 400));
        addPriceSummary(EnumPokeballs.MasterBall.name(), getPriceOf(ConfigKeyConstants.GOD, 600));
        addPriceSummary(EnumPokeballs.ParkBall.name(), getPriceOf(ConfigKeyConstants.GOD, 600));
        if (IPokemonWrapper.isUltraBeast(shops.pokemon))
            addPriceSummary(EnumPokeballs.BeastBall.name(), getPriceOf(ConfigKeyConstants.BEAST, 800));
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setCaughtBall((EnumPokeballs) value);
    }

    private static class ConfigKeyConstants {
        private static final String REGULAR = "regular";
        private static final String SPECIAL = "special";
        private static final String GOD = "god";
        private static final String BEAST = "beast";
    }
}
