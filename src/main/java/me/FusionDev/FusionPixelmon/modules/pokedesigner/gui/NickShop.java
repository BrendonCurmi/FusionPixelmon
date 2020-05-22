package me.FusionDev.FusionPixelmon.modules.pokedesigner.gui;

import me.FusionDev.FusionPixelmon.api.colour.Colour;
import me.FusionDev.FusionPixelmon.api.colour.IColourWrapper;
import me.FusionDev.FusionPixelmon.util.ColourWrapper;
import me.FusionDev.FusionPixelmon.util.Grammar;
import me.FusionDev.FusionPixelmon.api.inventory.InvItem;
import me.FusionDev.FusionPixelmon.api.inventory.InvPage;
import me.FusionDev.FusionPixelmon.api.pixelmon.PokemonWrapper;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

public class NickShop extends Shops.BaseShop {
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
            InvItem item = new InvItem(option.getItemType(), "§" + option.getCode() + Grammar.cap(option.name()));
            if (option.getDyeColor() != null) item.setKey(Keys.DYE_COLOR, option.getDyeColor());
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
        DARK_RED(Colour.DARK_RED, ItemTypes.CONCRETE, DyeColors.RED),
        RED(Colour.RED, ItemTypes.REDSTONE_BLOCK, null),
        GOLD(Colour.GOLD, ItemTypes.GOLD_BLOCK, null),
        YELLOW(Colour.YELLOW, ItemTypes.CONCRETE, DyeColors.YELLOW),
        DARK_GREEN(Colour.DARK_GREEN, ItemTypes.CONCRETE, DyeColors.GREEN),
        GREEN(Colour.GREEN, ItemTypes.CONCRETE, DyeColors.LIME),
        AQUA(Colour.AQUA, ItemTypes.CONCRETE, DyeColors.LIGHT_BLUE),
        DARK_AQUA(Colour.DARK_AQUA, ItemTypes.CONCRETE, DyeColors.CYAN),
        DARK_BLUE(Colour.DARK_BLUE, ItemTypes.CONCRETE, DyeColors.BLUE),
        BLUE(Colour.BLUE, ItemTypes.LAPIS_BLOCK, null),
        LIGHT_PURPLE(Colour.LIGHT_PURPLE, ItemTypes.CONCRETE, DyeColors.MAGENTA),
        DARK_PURPLE(Colour.DARK_PURPLE, ItemTypes.CONCRETE, DyeColors.PURPLE),
        WHITE(Colour.WHITE, ItemTypes.QUARTZ_BLOCK, null),
        GREY(Colour.GREY, ItemTypes.CONCRETE, DyeColors.SILVER),
        DARK_GREY(Colour.DARK_GREY, ItemTypes.CONCRETE, DyeColors.GRAY),
        BLACK(Colour.BLACK, ItemTypes.CONCRETE, DyeColors.BLACK),

        OBFUSCATED(Colour.OBFUSCATED, ItemTypes.DYE, DyeColors.GRAY),
        BOLD(Colour.BOLD, ItemTypes.DYE, DyeColors.GRAY),
        STRIKETHROUGH(Colour.STRIKETHROUGH, ItemTypes.DYE, DyeColors.GRAY),
        UNDERLINE(Colour.UNDERLINE, ItemTypes.DYE, DyeColors.GRAY),
        ITALIC(Colour.ITALIC, ItemTypes.DYE, DyeColors.GRAY),
        RESET(Colour.RESET, ItemTypes.DYE, DyeColors.GRAY);

        private Colour colour;
        private ItemType itemType;
        private DyeColor dyeColor;

        ColourOptions(Colour colour, ItemType itemType, DyeColor dyeColor) {
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

        public ItemType getItemType() {
            return itemType;
        }

        public DyeColor getDyeColor() {
            return dyeColor;
        }
    }
}
