package me.FusionDev.FusionPixelmon.guis.shops;

import com.pixelmonmod.pixelmon.api.enums.ExperienceGainType;
import me.FusionDev.FusionPixelmon.inventory.InvItem;
import me.FusionDev.FusionPixelmon.inventory.InvPage;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;

public class LevelShop extends Shops.BaseShop {
    public LevelShop(Shops shops) {
        super(shops);
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
                .setSelectedOption(Shops.Options.LEVEL);

        InvPage page = builder.build();

        InvItem emptyItem = new InvItem(ItemTypes.STAINED_GLASS_PANE, "").setKey(Keys.DYE_COLOR, DyeColors.BLACK);
        page.setBackground(emptyItem);

        InvItem item1 = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "§a§lAdd Levels").setKey(Keys.DYE_COLOR, DyeColors.LIME);
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
                int levels = (int) shops.getSelectedOptions().getOrDefault(Shops.Options.LEVEL, 0);
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
                    shops.getSelectedOptions().put(Shops.Options.LEVEL, levels + add);
                }
                builder.setSelectedItem(item1.itemStack);
            });
        }

        InvItem item2 = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "§c§lRemove Levels").setKey(Keys.DYE_COLOR, DyeColors.RED);
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
                int levels = (int) shops.getSelectedOptions().getOrDefault(Shops.Options.LEVEL, 0);
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
                    shops.getSelectedOptions().put(Shops.Options.LEVEL, levels - add);
                }
                builder.setSelectedItem(item2.itemStack);
            });
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        int levels = (int) value;
        return (levels > 0) ? levels * 100 : Math.abs(levels) * 10;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Add Level", 100 + " per level");
        addPriceSummary("Remove Level", 10 + " per level");
    }

    @Override
    public void purchaseAction(Object value) {
        int levels = (int) value;
        if (levels > 0) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                for (int i = 1; i <= levels; i++)
                    shops.pokemon.getLevelContainer().awardEXP(shops.pokemon.getExperienceToLevelUp(), ExperienceGainType.BATTLE);
            }).start();
        } else shops.pokemon.setLevel(shops.pokemon.getLevel() + levels);
    }
}
