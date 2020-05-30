package io.github.brendoncurmi.fusionpixelmon.sponge.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import io.github.brendoncurmi.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeAdapter;
import io.github.brendoncurmi.fusionpixelmon.sponge.api.pixelmon.PixelmonAPI;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import org.spongepowered.api.item.inventory.ItemStack;

public class PokeballShop extends Shops.BaseShop {
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
            InvItem item = new InvItem(SpongeAdapter.adapt(PixelmonAPI.getPixelmonItemType(pokeballs.getFilenamePrefix())), "§3§l" + pokeballs.name());
            page.setItem(slot, item, event -> {
                if (shops.pokemon.getCaughtBall() != pokeballs) shops.getSelectedOptions().put(getOption(), pokeballs);
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem((ItemStack) item.getItemStack().getRaw());
            });
            slot++;
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        EnumPokeballs ball = (EnumPokeballs) value;
        int cost = getPriceOf(ConfigKeys.REGULAR, 300);
        if (ball == EnumPokeballs.PremierBall || ball == EnumPokeballs.GSBall)
            cost = getPriceOf(ConfigKeys.SPECIAL, 400);
        else if (ball == EnumPokeballs.MasterBall || ball == EnumPokeballs.ParkBall)
            cost = getPriceOf(ConfigKeys.GOD, 600);
        else if (ball == EnumPokeballs.BeastBall)
            cost = getPriceOf(ConfigKeys.BEAST, 800);
        return cost;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Most Pokeballs", getPriceOf(ConfigKeys.REGULAR, 300));
        addPriceSummary(EnumPokeballs.PremierBall.name(), getPriceOf(ConfigKeys.SPECIAL, 400));
        addPriceSummary(EnumPokeballs.GSBall.name(), getPriceOf(ConfigKeys.SPECIAL, 400));
        addPriceSummary(EnumPokeballs.MasterBall.name(), getPriceOf(ConfigKeys.GOD, 600));
        addPriceSummary(EnumPokeballs.ParkBall.name(), getPriceOf(ConfigKeys.GOD, 600));
        if (IPokemonWrapper.isUltraBeast(shops.pokemon))
            addPriceSummary(EnumPokeballs.BeastBall.name(), getPriceOf(ConfigKeys.BEAST, 800));
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setCaughtBall((EnumPokeballs) value);
    }

    private static class ConfigKeys {
        static final String REGULAR = "regular";
        static final String SPECIAL = "special";
        static final String GOD = "god";
        static final String BEAST = "beast";
    }
}
