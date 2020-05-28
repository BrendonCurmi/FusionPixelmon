package io.github.brendoncurmi.fusionpixelmon.sponge.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import io.github.brendoncurmi.fusionpixelmon.impl.Grammar;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory.InvPage;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemTypes;

public class NatureShop extends Shops.BaseShop {
    public NatureShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.NATURE;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Nature Modification", "pokeeditor-nature", 5)
                .setInfoItemData("Nature Info",
                        "To pick a nature for your Pokemon",
                        "simply select one of the options",
                        "on the right.")
                .setSelectedItemName("Selected Nature")
                .setSelectedOption(getOption());
        InvPage page = builder.build();

        for (NatureOptions option : NatureOptions.values()) {
            InvItem item = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "§3§l" + Grammar.cap(option.name())).setKey(Keys.DYE_COLOR, option.dyeColor);
            item.setLore("  Boosted: §b" + option.boosted, "  Lowered: §c" + option.lowered);
            page.setItem(option.slot, item, event -> {
                if (!shops.pokemon.getNature().name().equalsIgnoreCase(option.name()))
                    shops.getSelectedOptions().put(getOption(), Grammar.cap(option.name()));
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item.getItemStack());
            });
        }

        return page;
    }

    @Override
    public int prices(Object value) {
        return getPriceOf(ConfigKeys.CHANGE, 3000);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Nature Change", getPriceOf(ConfigKeys.CHANGE, 3000));
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setNature(EnumNature.natureFromString(value.toString()));
    }

    private static class ConfigKeys {
        static final String CHANGE = "change";
    }

    public enum NatureOptions {
        HARDY(2, "Attack", "Attack", DyeColors.RED),
        LONELY(11, "Attack", "Defense", DyeColors.RED),
        ADAMANT(20, "Attack", "Special Attack", DyeColors.RED),
        NAUGHTY(29, "Attack", "Special Defense", DyeColors.RED),
        BRAVE(38, "Attack", "Speed", DyeColors.RED),

        BOLD(3, "Defense", "Attack", DyeColors.ORANGE),
        DOCILE(12, "Defense", "Defense", DyeColors.ORANGE),
        IMPISH(21, "Defense", "Special Attack", DyeColors.ORANGE),
        LAX(30, "Defense", "Special Defense", DyeColors.ORANGE),
        RELAXED(39, "Defense", "Speed", DyeColors.ORANGE),

        MODEST(4, "Special Attack", "Attack", DyeColors.PURPLE),
        MILD(13, "Special Attack", "Defense", DyeColors.PURPLE),
        BASHFUL(22, "Special Attack", "Special Attack", DyeColors.PURPLE),
        RASH(31, "Special Attack", "Special Defense", DyeColors.PURPLE),
        QUIET(40, "Special Attack", "Speed", DyeColors.PURPLE),

        CALM(5, "Special Defense", "Attack", DyeColors.YELLOW),
        GENTLE(14, "Special Defense", "Defense", DyeColors.YELLOW),
        CAREFUL(23, "Special Defense", "Special Attack", DyeColors.YELLOW),
        QUIRKY(32, "Special Defense", "Special Defense", DyeColors.YELLOW),
        SASSY(41, "Special Defense", "Speed", DyeColors.YELLOW),

        TIMID(6, "Speed", "Attack", DyeColors.LIGHT_BLUE),
        HASTY(15, "Speed", "Defense", DyeColors.LIGHT_BLUE),
        JOLLY(24, "Speed", "Special Attack", DyeColors.LIGHT_BLUE),
        NAIVE(33, "Speed", "Special Defense", DyeColors.LIGHT_BLUE),
        SERIOUS(42, "Speed", "Speed", DyeColors.LIGHT_BLUE);

        int slot;
        String boosted;
        String lowered;
        DyeColor dyeColor;

        NatureOptions(int slot, String boosted, String lowered, DyeColor dyeColor) {
            this.slot = slot;
            this.boosted = boosted;
            this.lowered = lowered;
            this.dyeColor = dyeColor;
        }
    }
}
