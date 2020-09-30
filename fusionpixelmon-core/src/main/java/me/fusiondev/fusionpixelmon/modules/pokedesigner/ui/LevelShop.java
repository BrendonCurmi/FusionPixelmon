package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.impl.TimeUtils;

public class LevelShop extends BaseShop {
    public LevelShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.LEVEL;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Level Modification", "pokeeditor-level", 6)
                .setInfoItemData("Level Info",
                        "To modify the levels for your Pokemon",
                        "simply use the above options.")
                .setSelectedItemName("Selected Levels")
                .setSelectedSlot(46)
                .setInfoSlot(48)
                .setResetSlot(50)
                .setBackSlot(52)
                .setSelectedOption(getOption());

        InvPage page = builder.build();
        AbstractItemTypes reg = FusionPixelmon.getRegistry().getItemTypesRegistry();

        AbstractItemStack emptyStack = reg.STAINED_GLASS_PANE().to();
        emptyStack.setColour(DyeColor.BLACK);
        //emptyStack.offer(Keys.DYE_COLOR, DyeColors.BLACK);
        InvItem emptyItem = new InvItem(emptyStack, "");
        page.setBackground(emptyItem);

        AbstractItemStack addStack = reg.STAINED_HARDENED_CLAY().to();
        addStack.setColour(DyeColor.LIME);
        //addStack.offer(Keys.DYE_COLOR, DyeColors.LIME);
        InvItem item1 = new InvItem(addStack, "§a§lAdd Levels");
        if (FusionPixelmon.getModule().equals("forge")) {
            item1.setLore(
                    "Click here to increase the",
                    "level of your pokemon."
            );
        } else {
            item1.setLore(
                    "Click here to increase the",
                    "level of your pokemon.",
                    "",
                    "§aNote:",
                    "  Left Click: §a+1 lvl",
                    "  Left Click + Shift: §a+10 lvls"
            );
        }
        int[] item1slots = new int[]{
                10, 11, 12,
                19, 20, 21,
                28, 29, 30
        };
        int[] item2slots = new int[]{
                14, 15, 16,
                23, 24, 25,
                32, 33, 34
        };

        for (int slots1 : item1slots) {
            page.setItem(slots1, item1, event -> {
                int levels = (int) shops.getSelectedOptions().getOrDefault(getOption(), 0);
                int add = 1;
                if (event.isShift()) add = 10;
                if (shops.pokemon.getLevel() + levels + add > 100) {
                    add = 100 - shops.pokemon.getLevel() - levels;
                    // 100 = lvl + lvls + add
                    // 100 - lvl = lvls + add
                    // 100 - lvl - lvls = add
                }
                if (shops.pokemon.getLevel() + levels + add <= 100) {
                    shops.getSelectedOptions().put(getOption(), levels + add);
                }
                builder.setSelectedItem(item1.getItemStack());
            });
        }

        AbstractItemStack removeStack = reg.STAINED_HARDENED_CLAY().to();
        removeStack.setColour(DyeColor.RED);
        //removeStack.offer(Keys.DYE_COLOR, DyeColors.RED);
        InvItem item2 = new InvItem(removeStack, "§c§lRemove Levels");
        if (FusionPixelmon.getModule().equals("forge")) {
            item2.setLore(
                    "Click here to decrease the",
                    "level of your pokemon."
            );
        } else {
            item2.setLore(
                    "Click here to decrease the",
                    "level of your pokemon.",
                    "",
                    "§aNote:",
                    "  Left Click: §c-1 lvl",
                    "  Left Click + Shift: §c-10 lvls"
            );
        }
        for (int slots2 : item2slots) {
            page.setItem(slots2, item2, event -> {
                int levels = (int) shops.getSelectedOptions().getOrDefault(getOption(), 0);
                int add = 1;
                if (event.isShift()) add = 10;
                if (shops.pokemon.getLevel() + levels - add < 1) {
                    add = shops.pokemon.getLevel() + levels - 1;
                    // 1 = lvl + lvls - add
                    // 1 + add = lvl + lvls
                    // add = lvl + lvls - 1
                }
                if (shops.pokemon.getLevel() + levels - add > 0) {
                    shops.getSelectedOptions().put(getOption(), levels - add);
                }
                builder.setSelectedItem(item2.getItemStack());
            });
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        int levels = (int) value;
        return (levels > 0) ? levels * getPriceOf(ConfigKeyConstants.ADD_LEVEL, 100) : Math.abs(levels) * getPriceOf(ConfigKeyConstants.REMOVE_LEVEL, 10);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Add Level", getPriceOf(ConfigKeyConstants.ADD_LEVEL, 100) + " per level");
        addPriceSummary("Remove Level", getPriceOf(ConfigKeyConstants.REMOVE_LEVEL, 10) + " per level");
    }

    @Override
    public void purchaseAction(Object value) {
        int levels = (int) value;
        if (levels > 0) {
            shops.pokemon.retrieve();
            float health = shops.pokemon.getHealthPercentage();
            shops.pokemon.setHealth(0);
            TimeUtils.setTimeout(() -> {
                for (int i = 1; i <= levels; i++)
                    shops.pokemon.getLevelContainer().awardEXP(shops.pokemon.getExperienceToLevelUp(), ExperienceGainType.BATTLE);
                shops.pokemon.setHealthPercentage(health);
            }, 1000);
        } else shops.pokemon.setLevel(shops.pokemon.getLevel() + levels);
    }

    private static class ConfigKeyConstants {
        private static final String ADD_LEVEL = "add-per-level";
        private static final String REMOVE_LEVEL = "remove-per-level";
    }
}
