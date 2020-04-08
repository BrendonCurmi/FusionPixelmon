package me.FusionDev.FusionPixelmon.guis.shops;

import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import me.FusionDev.FusionPixelmon.pixelmon.PixelmonAPI;
import me.FusionDev.FusionPixelmon.inventory.InvItem;
import me.FusionDev.FusionPixelmon.inventory.InvPage;

public class PokeballShop extends Shops.BaseShop {
    public PokeballShop(Shops shops) {
        super(shops);
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
                .setSelectedOption(Shops.Options.POKEBALL);

        InvPage page = builder.build();

        //page.setBackground(emptyItem);

        int slot = 9;
        for (EnumPokeballs pokeballs : EnumPokeballs.values()) {
            if (pokeballs == EnumPokeballs.BeastBall && !PixelmonAPI.isUltraBeast(shops.pokemon)) continue;
            InvItem item = new InvItem(PixelmonAPI.getPixelmonItemType(pokeballs.getFilenamePrefix()), "§3§l" + pokeballs.name());
            page.setItem(slot, item, event -> {
                if (shops.pokemon.getCaughtBall() != pokeballs) shops.getSelectedOptions().put(Shops.Options.POKEBALL, pokeballs);
                else shops.getSelectedOptions().remove(Shops.Options.POKEBALL);
                builder.setSelectedItem(item.itemStack);
            });
            slot++;
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        EnumPokeballs ball = (EnumPokeballs) value;
        int cost = 300;
        if (ball == EnumPokeballs.PremierBall || ball == EnumPokeballs.GSBall) cost = 400;
        else if (ball == EnumPokeballs.MasterBall || ball == EnumPokeballs.ParkBall) cost = 600;
        else if (ball == EnumPokeballs.BeastBall) cost = 800;
        return cost;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Most Pokeballs", 300);
        addPriceSummary(EnumPokeballs.PremierBall.name(), 400);
        addPriceSummary(EnumPokeballs.GSBall.name(), 400);
        addPriceSummary(EnumPokeballs.MasterBall.name(), 600);
        addPriceSummary(EnumPokeballs.ParkBall.name(), 600);
        if (PixelmonAPI.isUltraBeast(shops.pokemon)) addPriceSummary(EnumPokeballs.BeastBall.name(), 800);
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setCaughtBall((EnumPokeballs) value);
    }
}
