package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.impl.GrammarUtils;

public class GrowthShop extends BaseShop {
    public GrowthShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.GROWTH;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Growth Modification", "pokeeditor-growth", 5)
                .setInfoItemData("Growth Info",
                        "To pick a size for your Pokemon",
                        "simply select one of the options",
                        "on the right.")
                .setSelectedItemName("Selected Growth")
                .setSelectedOption(getOption());
        InvPage page = builder.build();
        Registry reg = FusionPixelmon.getRegistry();

        for (GrowthOptions option : GrowthOptions.values()) {
            AbstractItemStack itemStack = reg.getItemTypesRegistry().STAINED_HARDENED_CLAY().to();
            itemStack.setColour(option.dyeColor);
            //ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
            //itemStack.offer(Keys.DYE_COLOR, option.dyeColor);
            InvItem item = new InvItem(itemStack, "§3§l" + GrammarUtils.cap(option.name()));
            page.setItem(option.slot, item, event -> {
                if (!shops.pokemon.getGrowth().name().equalsIgnoreCase(option.name()))
                    shops.getSelectedOptions().put(getOption(), option.name());
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item.getItemStack());
            });
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        String growth = (String) value;
        int cost = getPriceOf(ConfigKeys.REGULAR, 600);
        if (growth.equals(EnumGrowth.Microscopic.name()) || growth.equals(EnumGrowth.Ginormous.name()))
            cost = getPriceOf(ConfigKeys.SPECIAL, 2000);
        return cost;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Most Growths", getPriceOf(ConfigKeys.REGULAR, 600));
        addPriceSummary(EnumGrowth.Microscopic.name(), getPriceOf(ConfigKeys.SPECIAL, 2000));
        addPriceSummary(EnumGrowth.Ginormous.name(), getPriceOf(ConfigKeys.SPECIAL, 2000));
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setGrowth(EnumGrowth.growthFromString(value.toString()));
    }

    private static class ConfigKeys {
        static final String REGULAR = "regular";
        static final String SPECIAL = "special";
    }

    public enum GrowthOptions {
        Microscopic(11, DyeColor.RED),
        Pygmy(20, DyeColor.ORANGE),
        Runt(29, DyeColor.YELLOW),

        Small(13, DyeColor.GREEN),
        Ordinary(22, DyeColor.LIGHT_BLUE),
        Huge(31, DyeColor.PURPLE),

        Giant(15, DyeColor.PINK),
        Enormous(24, DyeColor.GRAY),
        Ginormous(33, DyeColor.BLACK);

        int slot;
        DyeColor dyeColor;

        GrowthOptions(int slot, DyeColor dyeColor) {
            this.slot = slot;
            this.dyeColor = dyeColor;
        }
    }
}
