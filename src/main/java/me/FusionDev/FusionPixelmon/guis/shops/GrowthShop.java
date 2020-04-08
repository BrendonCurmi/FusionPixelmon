package me.FusionDev.FusionPixelmon.guis.shops;

import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import me.FusionDev.FusionPixelmon.apis.Grammar;
import me.FusionDev.FusionPixelmon.inventory.InvItem;
import me.FusionDev.FusionPixelmon.inventory.InvPage;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemTypes;

public class GrowthShop extends Shops.BaseShop {
    public GrowthShop(Shops shops) {
        super(shops);
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Growth Modification", "pokeeditor-growth", 5)
                .setInfoItemData("Growth Info",
                        "To pick a size for your Pokemon",
                        "simply select one of the options",
                        "on the right.")
                .setSelectedItemName("Selected Growth")
                .setSelectedOption(Shops.Options.GROWTH);
        InvPage page = builder.build();

        for (GrowthOptions option : GrowthOptions.values()) {
            InvItem item = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "§3§l" + Grammar.cap(option.name())).setKey(Keys.DYE_COLOR, option.dyeColor);
            page.setItem(option.slot, item, event -> {
                if (!shops.pokemon.getGrowth().name().equalsIgnoreCase(option.name())) shops.getSelectedOptions().put(Shops.Options.GROWTH, option.name());
                else shops.getSelectedOptions().remove(Shops.Options.GROWTH);
                builder.setSelectedItem(item.itemStack);
            });
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        String growth = (String) value;
        int cost = 600;
        if (growth.equals(EnumGrowth.Microscopic.name()) || growth.equals(EnumGrowth.Ginormous.name())) cost = 2000;
        return cost;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Most Growths", 600);
        addPriceSummary(EnumGrowth.Microscopic.name(), 2000);
        addPriceSummary(EnumGrowth.Ginormous.name(), 2000);
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setGrowth(EnumGrowth.growthFromString(value.toString()));
    }

    public enum GrowthOptions {
        Microscopic(11, DyeColors.RED),
        Pygmy(20, DyeColors.ORANGE),
        Runt(29, DyeColors.YELLOW),

        Small(13, DyeColors.GREEN),
        Ordinary(22, DyeColors.LIGHT_BLUE),
        Huge(31, DyeColors.PURPLE),

        Giant(15, DyeColors.PINK),
        Enormous(24, DyeColors.GRAY),
        Ginormous(33, DyeColors.BLACK);

        int slot;
        DyeColor dyeColor;

        GrowthOptions(int slot, DyeColor dyeColor) {
            this.slot = slot;
            this.dyeColor = dyeColor;
        }
    }
}
