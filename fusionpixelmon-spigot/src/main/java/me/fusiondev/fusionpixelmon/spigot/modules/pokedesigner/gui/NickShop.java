package io.github.brendoncurmi.fusionpixelmon.spigot.modules.pokedesigner.gui;

import io.github.brendoncurmi.fusionpixelmon.api.colour.Colour;
import io.github.brendoncurmi.fusionpixelmon.api.colour.IColourWrapper;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.ui.BaseShop;
import io.github.brendoncurmi.fusionpixelmon.api.ui.Shops;
import io.github.brendoncurmi.fusionpixelmon.impl.Grammar;
import io.github.brendoncurmi.fusionpixelmon.impl.colour.ColourWrapper;
import io.github.brendoncurmi.fusionpixelmon.impl.pixelmon.PokemonWrapper;
import io.github.brendoncurmi.fusionpixelmon.spigot.SpigotAdapter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NickShop extends BaseShop {
    public NickShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.NICK;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Nick Modification", "pokeeditor-nick", 6)
                .setInfoItemData("Nick Info",
                        "To pick a nick colour or style for your Pokemon",
                        "simply use the options above.",
                        "Colours and styles can be bought at once.",
                        "Previously bought options dont stack up.")
                .setSelectedItemName("Selected Nick Colour")
                .setSelectedSlot(46)
                .setInfoSlot(48)
                .setResetSlot(50)
                .setBackSlot(52)
                .border(true)
                .setSelectedOption(getOption());
        InvPage page = builder.build();

        int slot = 9;
        for (ColourOptions option : ColourOptions.values()) {
//            if (option.getDyeColor() != null) itemStack.offer(Keys.DYE_COLOR, option.getDyeColor());
            AbstractItemStack itemStack = SpigotAdapter.adapt(new ItemStack(option.getItemType()));
            itemStack.setColour(option.getDyeColor());
            InvItem item = new InvItem(itemStack, "§" + option.getCode() + Grammar.cap(option.name()));
            page.setItem(slot, item, event -> {
                IColourWrapper wrapper = (IColourWrapper) shops.getSelectedOptions().getOrDefault(getOption(), new ColourWrapper());
                if (option.getColour().isStyle()) wrapper.setStyle(option.getColour());
                else wrapper.setColour(option.getColour());
                shops.getSelectedOptions().put(getOption(), wrapper);
                builder.setSelectedItem(item.getItemStack());
            });
            slot++;
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        IColourWrapper wrapper = (IColourWrapper) value;
        int cost = 0;
        if (wrapper.hasColour()) cost += getPriceOf(ConfigKeys.CHANGE_COLOUR, 10000);
        if (wrapper.hasStyle()) cost += getPriceOf(ConfigKeys.CHANGE_STYLE, 20000);
        return cost;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Change Colour", getPriceOf(ConfigKeys.CHANGE_COLOUR, 10000));
        addPriceSummary("Change Style", getPriceOf(ConfigKeys.CHANGE_STYLE, 20000));
    }

    @Override
    public void purchaseAction(Object value) {
        String name = new PokemonWrapper(shops.pokemon).getName();
        if (name.contains("§"))
            name = name.substring(name.lastIndexOf("§") + 2);
        shops.pokemon.setNickname(((IColourWrapper) value).getFullCode() + name);
    }

    private static class ConfigKeys {
        static final String CHANGE_COLOUR = "change-colour";
        static final String CHANGE_STYLE = "change-style";
    }

    public enum ColourOptions {
        DARK_RED(Colour.DARK_RED, Material.CONCRETE, DyeColor.RED),
        RED(Colour.RED, Material.REDSTONE_BLOCK, null),
        GOLD(Colour.GOLD, Material.GOLD_BLOCK, null),
        YELLOW(Colour.YELLOW, Material.CONCRETE, DyeColor.YELLOW),
        DARK_GREEN(Colour.DARK_GREEN, Material.CONCRETE, DyeColor.GREEN),
        GREEN(Colour.GREEN, Material.CONCRETE, DyeColor.LIME),
        AQUA(Colour.AQUA, Material.CONCRETE, DyeColor.LIGHT_BLUE),
        DARK_AQUA(Colour.DARK_AQUA, Material.CONCRETE, DyeColor.CYAN),
        DARK_BLUE(Colour.DARK_BLUE, Material.CONCRETE, DyeColor.BLUE),
        BLUE(Colour.BLUE, Material.LAPIS_BLOCK, null),
        LIGHT_PURPLE(Colour.LIGHT_PURPLE, Material.CONCRETE, DyeColor.MAGENTA),
        DARK_PURPLE(Colour.DARK_PURPLE, Material.CONCRETE, DyeColor.PURPLE),
        WHITE(Colour.WHITE, Material.QUARTZ_BLOCK, null),
        GREY(Colour.GREY, Material.CONCRETE, DyeColor.SILVER),
        DARK_GREY(Colour.DARK_GREY, Material.CONCRETE, DyeColor.GRAY),
        BLACK(Colour.BLACK, Material.CONCRETE, DyeColor.BLACK),

        OBFUSCATED(Colour.OBFUSCATED, Material.INK_SACK, DyeColor.GRAY),
        BOLD(Colour.BOLD, Material.INK_SACK, DyeColor.GRAY),
        STRIKETHROUGH(Colour.STRIKETHROUGH, Material.INK_SACK, DyeColor.GRAY),
        UNDERLINE(Colour.UNDERLINE, Material.INK_SACK, DyeColor.GRAY),
        ITALIC(Colour.ITALIC, Material.INK_SACK, DyeColor.GRAY),
        RESET(Colour.RESET, Material.INK_SACK, DyeColor.GRAY);

        private Colour colour;
        private Material itemType;
        private DyeColor dyeColor;

        ColourOptions(Colour colour, Material itemType, DyeColor dyeColor) {
            this.colour = colour;
            this.itemType = itemType;
            this.dyeColor = dyeColor;
        }

        public Colour getColour() {
            return colour;
        }

        public char getCode() {
            return colour.getCode();
        }

        public Material getItemType() {
            return itemType;
        }

        public DyeColor getDyeColor() {
            return dyeColor;
        }
    }
}
