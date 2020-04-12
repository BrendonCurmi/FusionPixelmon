package me.FusionDev.FusionPixelmon.guis.shops;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import me.FusionDev.FusionPixelmon.inventory.InvItem;
import me.FusionDev.FusionPixelmon.inventory.InvPage;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemTypes;

public class GenderShop extends Shops.BaseShop {
    public GenderShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.GENDER;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Shops.BaseShop.Builder("§0Gender Modification", "pokeeditor-gender", 5)
                .setInfoItemData("Gender Info",
                        "To pick a gender for your Pokemon",
                        "simply select one of the options",
                        "on the right.")
                .setSelectedItemName("Selected Gender")
                .setSelectedOption(getOption());
        InvPage page = builder.build();

        if (shops.pokemon.getGender() == Gender.None) {
            InvItem item1 = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "§b§lNone").setKey(Keys.DYE_COLOR, DyeColors.YELLOW);
            page.setItem(22, item1, event -> builder.setSelectedItem(item1.itemStack));
        } else {
            InvItem item1 = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "§b§lMale").setKey(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE);
            item1.setLore("Click here to select the", "§bMale §7gender.");
            page.setItem(21, item1, event -> {
                if (shops.pokemon.getGender() != Gender.Male) shops.getSelectedOptions().put(getOption(), "§bMale");
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item1.itemStack);
            });


            InvItem item2 = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "§d§lFemale").setKey(Keys.DYE_COLOR, DyeColors.MAGENTA);
            item2.setLore("Click here to select the", "§dFemale §7gender.");
            page.setItem(23, item2, event -> {
                if (shops.pokemon.getGender() != Gender.Female) shops.getSelectedOptions().put(getOption(), "§dFemale");
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item2.itemStack);
            });
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        return getPriceOf(ConfigKeys.CHANGE, 1000);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Gender Change", getPriceOf(ConfigKeys.CHANGE, 1000));
    }

    @Override
    public void purchaseAction(Object value) {
        if (value.toString().contains("Female")) shops.pokemon.setGender(Gender.Female);
        else if (value.toString().contains("Male")) shops.pokemon.setGender(Gender.Male);
    }

    private static class ConfigKeys {
        static final String CHANGE = "change";
    }
}
