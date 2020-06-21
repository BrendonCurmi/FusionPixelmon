package io.github.brendoncurmi.fusionpixelmon.spigot.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.ui.BaseShop;
import io.github.brendoncurmi.fusionpixelmon.api.ui.Shops;
import io.github.brendoncurmi.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

        if (shops.pokemon.getGender() == Gender.None) {
            AbstractItemStack genderStack = SpigotAdapter.adapt(new ItemStack(Material.STAINED_CLAY));
//            genderStack.offer(Keys.DYE_COLOR, DyeColors.YELLOW);
            genderStack.setColour(DyeColor.YELLOW);
            InvItem item1 = new InvItem(genderStack, "§b§lNone");
            page.setItem(22, item1, event -> builder.setSelectedItem(item1.getItemStack()));
        } else {
            AbstractItemStack maleStack = SpigotAdapter.adapt(new ItemStack(Material.STAINED_CLAY));
//            maleStack.offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE);
            maleStack.setColour(DyeColor.LIGHT_BLUE);
            InvItem item1 = new InvItem(maleStack, "§b§lMale");
            item1.setLore("Click here to select the", "§bMale §7gender.");
            page.setItem(21, item1, event -> {
                if (shops.pokemon.getGender() != Gender.Male) shops.getSelectedOptions().put(getOption(), "§bMale");
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item1.getItemStack());
            });


            AbstractItemStack femaleStack = SpigotAdapter.adapt(new ItemStack(Material.STAINED_CLAY));
//            femaleStack.offer(Keys.DYE_COLOR, DyeColors.MAGENTA);
            femaleStack.setColour(DyeColor.MAGENTA);
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
