package io.github.brendoncurmi.fusionpixelmon.spigot.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.enums.EnumNature;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.ui.BaseShop;
import io.github.brendoncurmi.fusionpixelmon.api.ui.Shops;
import io.github.brendoncurmi.fusionpixelmon.impl.Grammar;
import io.github.brendoncurmi.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NatureShop extends BaseShop {
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
            AbstractItemStack itemStack = SpigotAdapter.adapt(new ItemStack(Material.STAINED_CLAY));
//            itemStack.offer(Keys.DYE_COLOR, option.dyeColor);
            itemStack.setColour(option.dyeColor);
            InvItem item = new InvItem(itemStack, "§3§l" + Grammar.cap(option.name()));
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
        HARDY(2, "Attack", "Attack", DyeColor.RED),
        LONELY(11, "Attack", "Defense", DyeColor.RED),
        ADAMANT(20, "Attack", "Special Attack", DyeColor.RED),
        NAUGHTY(29, "Attack", "Special Defense", DyeColor.RED),
        BRAVE(38, "Attack", "Speed", DyeColor.RED),

        BOLD(3, "Defense", "Attack", DyeColor.ORANGE),
        DOCILE(12, "Defense", "Defense", DyeColor.ORANGE),
        IMPISH(21, "Defense", "Special Attack", DyeColor.ORANGE),
        LAX(30, "Defense", "Special Defense", DyeColor.ORANGE),
        RELAXED(39, "Defense", "Speed", DyeColor.ORANGE),

        MODEST(4, "Special Attack", "Attack", DyeColor.PURPLE),
        MILD(13, "Special Attack", "Defense", DyeColor.PURPLE),
        BASHFUL(22, "Special Attack", "Special Attack", DyeColor.PURPLE),
        RASH(31, "Special Attack", "Special Defense", DyeColor.PURPLE),
        QUIET(40, "Special Attack", "Speed", DyeColor.PURPLE),

        CALM(5, "Special Defense", "Attack", DyeColor.YELLOW),
        GENTLE(14, "Special Defense", "Defense", DyeColor.YELLOW),
        CAREFUL(23, "Special Defense", "Special Attack", DyeColor.YELLOW),
        QUIRKY(32, "Special Defense", "Special Defense", DyeColor.YELLOW),
        SASSY(41, "Special Defense", "Speed", DyeColor.YELLOW),

        TIMID(6, "Speed", "Attack", DyeColor.LIGHT_BLUE),
        HASTY(15, "Speed", "Defense", DyeColor.LIGHT_BLUE),
        JOLLY(24, "Speed", "Special Attack", DyeColor.LIGHT_BLUE),
        NAIVE(33, "Speed", "Special Defense", DyeColor.LIGHT_BLUE),
        SERIOUS(42, "Speed", "Speed", DyeColor.LIGHT_BLUE);

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
