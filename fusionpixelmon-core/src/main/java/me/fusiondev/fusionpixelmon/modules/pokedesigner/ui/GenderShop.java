package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;

public class GenderShop extends BaseShop {
    public GenderShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.GENDER;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Gender Modification", "pokeeditor-gender", 5)
                .setInfoItemData("Gender Info",
                        "To pick a gender for your Pokemon",
                        "simply select one of the options",
                        "on the right.")
                .setSelectedItemName("Selected Gender")
                .setSelectedOption(getOption());
        InvPage page = builder.build();
        Registry reg = FusionPixelmon.getRegistry();

        if (shops.pokemon.getGender() == Gender.None) {
            AbstractItemStack genderStack = reg.getItemTypesRegistry().STAINED_HARDENED_CLAY().to();
            genderStack.setColour(DyeColor.YELLOW);

            //ItemStack genderStack = ItemStack.builder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
            //genderStack.offer(Keys.DYE_COLOR, DyeColors.YELLOW);
            InvItem item1 = new InvItem(genderStack, "§b§lNone");
            page.setItem(22, item1, event -> builder.setSelectedItem(item1.getItemStack()));
        } else {
            AbstractItemStack maleStack = reg.getItemTypesRegistry().STAINED_HARDENED_CLAY().to();
            maleStack.setColour(DyeColor.LIGHT_BLUE);

            //ItemStack maleStack = ItemStack.builder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
            //maleStack.offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE);
            InvItem item1 = new InvItem(maleStack, "§b§lMale");
            item1.setLore("Click here to select the", "§bMale §7gender.");
            page.setItem(21, item1, event -> {
                if (shops.pokemon.getGender() != Gender.Male) shops.getSelectedOptions().put(getOption(), "§bMale");
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item1.getItemStack());
            });

            AbstractItemStack femaleStack = reg.getItemTypesRegistry().STAINED_HARDENED_CLAY().to();
            femaleStack.setColour(DyeColor.MAGENTA);

            //ItemStack femaleStack = ItemStack.builder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
            //femaleStack.offer(Keys.DYE_COLOR, DyeColors.MAGENTA);
            InvItem item2 = new InvItem(femaleStack, "§d§lFemale");
            item2.setLore("Click here to select the", "§dFemale §7gender.");
            page.setItem(23, item2, event -> {
                if (shops.pokemon.getGender() != Gender.Female) shops.getSelectedOptions().put(getOption(), "§dFemale");
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item2.getItemStack());
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
