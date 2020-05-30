package io.github.brendoncurmi.fusionpixelmon.sponge.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import io.github.brendoncurmi.fusionpixelmon.impl.Grammar;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeAdapter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class GrowthShop extends Shops.BaseShop {
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

        for (GrowthOptions option : GrowthOptions.values()) {
            ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
            itemStack.offer(Keys.DYE_COLOR, option.dyeColor);
            InvItem item = new InvItem(SpongeAdapter.adapt(itemStack), "§3§l" + Grammar.cap(option.name()));
            page.setItem(option.slot, item, event -> {
                if (!shops.pokemon.getGrowth().name().equalsIgnoreCase(option.name()))
                    shops.getSelectedOptions().put(getOption(), option.name());
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem((ItemStack) item.getItemStack().getRaw());
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
