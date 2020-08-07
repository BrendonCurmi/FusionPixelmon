package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.colour.IColourWrapper;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.impl.GrammarUtils;
import me.fusiondev.fusionpixelmon.impl.colour.ColourWrapper;
import me.fusiondev.fusionpixelmon.impl.pixelmon.PokemonWrapper;

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
            AbstractItemStack itemStack = option.getItemType().to();
            if (option.getDyeColor() != null) itemStack.setColour(option.getDyeColor());
            //if (option.getDyeColor() != null) itemStack.offer(Keys.DYE_COLOR, option.getDyeColor());
            InvItem item = new InvItem(itemStack, "§" + option.getCode() + GrammarUtils.cap(option.name()));
            page.setItem(slot, item, event -> {
                IColourWrapper wrapper = (IColourWrapper) shops.getSelectedOptions().getOrDefault(getOption(), new ColourWrapper());
                if (option.getColor().isStyle()) wrapper.setStyle(option.getColor());
                else wrapper.setColor(option.getColor());
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
        if (wrapper.hasColour()) cost += getPriceOf(ConfigKeyConstants.CHANGE_COLOUR, 10000);
        if (wrapper.hasStyle()) cost += getPriceOf(ConfigKeyConstants.CHANGE_STYLE, 20000);
        return cost;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Change Colour", getPriceOf(ConfigKeyConstants.CHANGE_COLOUR, 10000));
        addPriceSummary("Change Style", getPriceOf(ConfigKeyConstants.CHANGE_STYLE, 20000));
    }

    @Override
    public void purchaseAction(Object value) {
        String name = new PokemonWrapper(shops.pokemon).getName();
        if (name.contains("§"))
            name = name.substring(name.lastIndexOf("§") + 2);
        shops.pokemon.setNickname(((IColourWrapper) value).getFullCode() + name);
    }

    private static class ConfigKeyConstants {
        private static final String CHANGE_COLOUR = "change-colour";
        private static final String CHANGE_STYLE = "change-style";
    }

    private static AbstractItemTypes reg = REG.getItemTypesRegistry();

    public enum ColourOptions {
        DARK_RED(Color.DARK_RED, reg.CONCRETE(), DyeColor.RED),
        RED(Color.RED, reg.REDSTONE_BLOCK(), null),
        GOLD(Color.GOLD, reg.GOLD_BLOCK(), null),
        YELLOW(Color.YELLOW, reg.CONCRETE(), DyeColor.YELLOW),
        DARK_GREEN(Color.DARK_GREEN, reg.CONCRETE(), DyeColor.GREEN),
        GREEN(Color.GREEN, reg.CONCRETE(), DyeColor.LIME),
        AQUA(Color.AQUA, reg.CONCRETE(), DyeColor.LIGHT_BLUE),
        DARK_AQUA(Color.DARK_AQUA, reg.CONCRETE(), DyeColor.CYAN),
        DARK_BLUE(Color.DARK_BLUE, reg.CONCRETE(), DyeColor.BLUE),
        BLUE(Color.BLUE, reg.LAPIS_BLOCK(), null),
        LIGHT_PURPLE(Color.LIGHT_PURPLE, reg.CONCRETE(), DyeColor.MAGENTA),
        DARK_PURPLE(Color.DARK_PURPLE, reg.CONCRETE(), DyeColor.PURPLE),
        WHITE(Color.WHITE, reg.QUARTZ_BLOCK(), null),
        GRAY(Color.GRAY, reg.CONCRETE(), DyeColor.SILVER),
        DARK_GRAY(Color.DARK_GRAY, reg.CONCRETE(), DyeColor.GRAY),
        BLACK(Color.BLACK, reg.CONCRETE(), DyeColor.BLACK),

        OBFUSCATED(Color.OBFUSCATED, reg.DYE(), DyeColor.GRAY),
        BOLD(Color.BOLD, reg.DYE(), DyeColor.GRAY),
        STRIKETHROUGH(Color.STRIKETHROUGH, reg.DYE(), DyeColor.GRAY),
        UNDERLINE(Color.UNDERLINE, reg.DYE(), DyeColor.GRAY),
        ITALIC(Color.ITALIC, reg.DYE(), DyeColor.GRAY),
        RESET(Color.RESET, reg.DYE(), DyeColor.GRAY);

        private Color color;
        private AbstractItemType itemType;
        private DyeColor dyeColor;

        ColourOptions(Color color, AbstractItemType itemType, DyeColor dyeColor) {
            this.color = color;
            this.itemType = itemType;
            this.dyeColor = dyeColor;
        }

        public Color getColor() {
            return color;
        }

        public char getCode() {
            return color.getCode();
        }

        public AbstractItemType getItemType() {
            return itemType;
        }

        public DyeColor getDyeColor() {
            return dyeColor;
        }
    }
}
