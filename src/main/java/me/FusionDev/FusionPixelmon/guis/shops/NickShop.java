package me.FusionDev.FusionPixelmon.guis.shops;

import me.FusionDev.FusionPixelmon.apis.Colour;
import me.FusionDev.FusionPixelmon.apis.Grammar;
import me.FusionDev.FusionPixelmon.inventory.InvItem;
import me.FusionDev.FusionPixelmon.inventory.InvPage;
import me.FusionDev.FusionPixelmon.pixelmon.PokeData;
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
                        "To pick a nick colour for your Pokemon",
                        "simply select one of the options",
                        "on the right.")
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
                shops.getSelectedOptions().put(getOption(), option.getColour());
                builder.setSelectedItem(item.itemStack);
            });
            slot++;
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        return getPriceOf(ConfigKeys.CHANGE, 10000);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Nick Change", getPriceOf(ConfigKeys.CHANGE, 10000));
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setNickname("§" + ((Colour) value).getCode() + new PokeData(shops.pokemon).getName());
    }

    private static class ConfigKeys {
        static final String CHANGE = "change";
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
