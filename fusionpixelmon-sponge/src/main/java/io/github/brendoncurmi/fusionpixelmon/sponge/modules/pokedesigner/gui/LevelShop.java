package io.github.brendoncurmi.fusionpixelmon.sponge.modules.pokedesigner.gui;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import io.github.brendoncurmi.fusionpixelmon.impl.TimeUtil;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeAdapter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class LevelShop extends Shops.BaseShop {
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

        ItemStack emptyStack = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
        emptyStack.offer(Keys.DYE_COLOR, DyeColors.BLACK);
        InvItem emptyItem = new InvItem(SpongeAdapter.adapt(emptyStack), "");
        page.setBackground(emptyItem);

        ItemStack addStack = ItemStack.builder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
        addStack.offer(Keys.DYE_COLOR, DyeColors.LIME);
        InvItem item1 = new InvItem(SpongeAdapter.adapt(addStack), "§a§lAdd Levels");
        item1.setLore(
                "Click here to increase the",
                "level of your pokemon.",
                "",
                "§aNote:",
                "  Left Click: §a+1 lvl",
                "  Left Click + Shift: §a+10 lvls"
        );
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
                if (event instanceof ClickInventoryEvent.Shift) {
                    add = 10;
                }
                if (shops.pokemon.getLevel() + levels + add > 100) {
                    add = 100 - shops.pokemon.getLevel() - levels;
                    // 100 = lvl + lvls + add
                    // 100 - lvl = lvls + add
                    // 100 - lvl - lvls = add
                }
                if (shops.pokemon.getLevel() + levels + add <= 100) {
                    shops.getSelectedOptions().put(getOption(), levels + add);
                }
                builder.setSelectedItem((ItemStack) item1.getItemStack().getRaw());
            });
        }

        ItemStack removeStack = ItemStack.builder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
        removeStack.offer(Keys.DYE_COLOR, DyeColors.RED);
        InvItem item2 = new InvItem(SpongeAdapter.adapt(removeStack), "§c§lRemove Levels");
        item2.setLore(
                "Click here to decrease the",
                "level of your pokemon.",
                "",
                "§aNote:",
                "  Left Click: §c-1 lvl",
                "  Left Click + Shift: §c-10 lvls"
        );
        for (int slots2 : item2slots) {
            page.setItem(slots2, item2, event -> {
                int levels = (int) shops.getSelectedOptions().getOrDefault(getOption(), 0);
                int add = 1;
                if (event instanceof ClickInventoryEvent.Shift) {
                    add = 10;
                }
                if (shops.pokemon.getLevel() + levels - add < 1) {
                    add = shops.pokemon.getLevel() + levels - 1;
                    // 1 = lvl + lvls - add
                    // 1 + add = lvl + lvls
                    // add = lvl + lvls - 1
                }
                if (shops.pokemon.getLevel() + levels - add > 0) {
                    shops.getSelectedOptions().put(getOption(), levels - add);
                }
                builder.setSelectedItem((ItemStack) item2.getItemStack().getRaw());
            });
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        int levels = (int) value;
        return (levels > 0) ? levels * getPriceOf(ConfigKeys.ADD_LEVEL, 100) : Math.abs(levels) * getPriceOf(ConfigKeys.REMOVE_LEVEL, 10);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Add Level", getPriceOf(ConfigKeys.ADD_LEVEL, 100) + " per level");
        addPriceSummary("Remove Level", getPriceOf(ConfigKeys.REMOVE_LEVEL, 10) + " per level");
    }

    @Override
    public void purchaseAction(Object value) {
        int levels = (int) value;
        if (levels > 0) {
            TimeUtil.setTimeout(() -> {
                for (int i = 1; i <= levels; i++)
                    shops.pokemon.getLevelContainer().awardEXP(shops.pokemon.getExperienceToLevelUp(), ExperienceGainType.BATTLE);
            }, 1000);
        } else shops.pokemon.setLevel(shops.pokemon.getLevel() + levels);
    }

    private static class ConfigKeys {
        static final String ADD_LEVEL = "add-per-level";
        static final String REMOVE_LEVEL = "remove-per-level";
    }
}
