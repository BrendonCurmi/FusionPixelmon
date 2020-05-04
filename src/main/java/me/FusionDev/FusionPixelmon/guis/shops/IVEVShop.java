package me.FusionDev.FusionPixelmon.guis.shops;

import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import me.FusionDev.FusionPixelmon.apis.Grammar;
import me.FusionDev.FusionPixelmon.apis.MathHelper;
import me.FusionDev.FusionPixelmon.pixelmon.PixelmonAPI;
import me.FusionDev.FusionPixelmon.inventory.InvItem;
import me.FusionDev.FusionPixelmon.inventory.InvPage;
import me.FusionDev.FusionPixelmon.pixelmon.PokeData;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class IVEVShop extends Shops.BaseShop {

    private final int[] IV_CACHE;
    private final int[] EV_CACHE;

    public IVEVShop(Shops shops) {
        super(shops, true);
        IV_CACHE = PixelmonAPI.getIVArray(shops.pokemon);
        EV_CACHE = PixelmonAPI.getEVArray(shops.pokemon);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.IVEV;
    }

    private static InvItem subtractionItem = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "").setKey(Keys.DYE_COLOR, DyeColors.RED);
    private static InvItem additionItem = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "").setKey(Keys.DYE_COLOR, DyeColors.GREEN);

    @Override
    public InvPage buildPage() {
        final IVEVAction[] action = new IVEVAction[1];
        action[0] = new IVEVAction();

        final int ROWS = 6;
        Builder builder = new Builder("§0IV/EV Modification", "pokeeditor-ivef", ROWS)
                .setInfoItemData("IV/EV Info",
                        "To modify the IVs/EVs for your Pokemon",
                        "simply use the options.")
                .setSelectedSlot(-2)
                .onReset(() -> {
                    action[0].IV.clear();
                    action[0].EV.clear();
                })
                .setSelectedOption(getOption());
        InvPage page = builder.build();

        for (int i = 0; i < ROWS; i++) page.setItem(4 + (9 * i), EMPTY_ITEM);

//        action = ((IVEVAction) getSelectedOptions().getOrDefault(Options.IVEV, new IVEVAction()));
        shops.getSelectedOptions().remove(getOption());


        int totalIV = IntStream.of(IV_CACHE).sum();
        int totalEV = IntStream.of(EV_CACHE).sum();

        String[] sub = new String[]{"", "§aNote:", "  Left Click: §c-1", "  Left Click + Shift: §c-10"};
        String[] add = new String[]{"", "§aNote:", "  Left Click: §a+1", "  Left Click + Shift: §a+10"};


        InvItem[][] items = new InvItem[IVEVOption.values().length][5];

        String optName;
        int i1 = 0;
        for (IVEVOption type : IVEVOption.values()) {
            optName = Grammar.underscoreToSpace(type.name());

            items[i1][0] = new InvItem(PixelmonAPI.getPixelmonItemType(type.itemID), "§3§l" + optName);
            page.setItem(i1 * 9, items[i1][0]);

            items[i1][1] = subtractionItem.copy("§c§lRemove " + optName + " EVs");
            items[i1][2] = additionItem.copy("§a§lAdd " + optName + " EVs");
            items[i1][3] = subtractionItem.copy("§c§lRemove " + optName + " IVs");
            items[i1][4] = additionItem.copy("§a§lAdd " + optName + " IVs");
            i1++;
        }


        page.setRunnable(() -> {
            int i = 0;

            int requestedIV = action[0].getRequestedIV();
            int requestedEV = action[0].getRequestedEV();

            for (IVEVOption type : IVEVOption.values()) {
                items[i][1].setLore(
                        lore(PixelmonAPI.beautifyEV(EV_CACHE[i]),
                                PixelmonAPI.beautifyTally(totalEV, EVStore.MAX_TOTAL_EVS),
                                PixelmonAPI.beautifyEV(EV_CACHE[i] + action[0].EV.getOrDefault(type.statsType, 0)),
                                PixelmonAPI.beautifyTally(totalEV + requestedEV, EVStore.MAX_TOTAL_EVS)),
                        sub
                );
                page.setItem((i * 9) + 2, items[i][1], event -> {
                    int delta = 1;
                    if (event instanceof ClickInventoryEvent.Shift) delta = 10;
                    action[0].removeEV(type.statsType, delta);
                    shops.getSelectedOptions().put(getOption(), action[0]);
                    action[0] = (IVEVAction) shops.getSelectedOptions().get(getOption());
                });

                items[i][2].setLore(
                        lore(PixelmonAPI.beautifyEV(EV_CACHE[i]),
                                PixelmonAPI.beautifyTally(totalEV, EVStore.MAX_TOTAL_EVS),
                                PixelmonAPI.beautifyEV(EV_CACHE[i] + action[0].EV.getOrDefault(type.statsType, 0)),
                                PixelmonAPI.beautifyTally(totalEV + requestedEV, EVStore.MAX_TOTAL_EVS)),
                        add
                );
                page.setItem((i * 9) + 3, items[i][2], event -> {
                    int delta = 1;
                    if (event instanceof ClickInventoryEvent.Shift) delta = 10;
                    action[0].addEV(type.statsType, delta, totalEV);
                    shops.getSelectedOptions().put(getOption(), action[0]);
                    action[0] = (IVEVAction) shops.getSelectedOptions().get(getOption());
                });

                items[i][3].setLore(
                        lore(PixelmonAPI.beautifyIV(IV_CACHE[i]),
                                PixelmonAPI.beautifyTally(totalIV, IVStore.MAX_IVS * IV_CACHE.length),
                                PixelmonAPI.beautifyIV(IV_CACHE[i] + action[0].IV.getOrDefault(type.statsType, 0)),
                                PixelmonAPI.beautifyTally(totalIV + requestedIV, IVStore.MAX_IVS * IV_CACHE.length)),
                        sub
                );
                page.setItem((i * 9) + 5, items[i][3], event -> {
                    int delta = 1;
                    if (event instanceof ClickInventoryEvent.Shift) delta = 10;
                    action[0].removeIV(type.statsType, delta);
                    shops.getSelectedOptions().put(getOption(), action[0]);
                    action[0] = (IVEVAction) shops.getSelectedOptions().get(getOption());
                });

                items[i][4].setLore(
                        lore(PixelmonAPI.beautifyIV(IV_CACHE[i]),
                                PixelmonAPI.beautifyTally(totalIV, IVStore.MAX_IVS * IV_CACHE.length),
                                PixelmonAPI.beautifyIV(IV_CACHE[i] + action[0].IV.getOrDefault(type.statsType, 0)),
                                PixelmonAPI.beautifyTally(totalIV + requestedIV, IVStore.MAX_IVS * IV_CACHE.length)),
                        add
                );
                page.setItem((i * 9) + 6, items[i][4], event -> {
                    int delta = 1;
                    if (event instanceof ClickInventoryEvent.Shift) delta = 10;
                    action[0].addIV(type.statsType, delta);
                    shops.getSelectedOptions().put(getOption(), action[0]);
                    action[0] = (IVEVAction) shops.getSelectedOptions().get(getOption());
                });
                i++;
            }
        });
        return page;
    }

    private static String[] lore(String current, String currentTotal, String requested, String requestedTotal) {
        return new String[]{"Current: " + current, "Current Total: " + currentTotal, "", "Requested: " + requested, "Requested Total: " + requestedTotal};
    }


    @Override
    public int prices(Object value) {
        IVEVAction action = ((IVEVAction) value);
        int totalCost = 0;
        for (int i : action.IV.values()) {
            if (i > 0) totalCost += i * getPriceOf(ConfigKeys.ADD_IV, 600);
            else if (i < 0) totalCost += -i * getPriceOf(ConfigKeys.REMOVE_IV, 5);
        }

        for (int i : action.EV.values()) {
            totalCost += Math.abs(i) * getPriceOf(ConfigKeys.CHANGE_EV, 5);
        }
        return totalCost;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Add IV", getPriceOf(ConfigKeys.ADD_IV, 600) + " per IV");
        addPriceSummary("Remove IV", getPriceOf(ConfigKeys.REMOVE_IV, 5) + " per IV");
        String changeEV = getPriceOf(ConfigKeys.CHANGE_EV, 5) + " per EV";
        addPriceSummary("Add EV", changeEV);
        addPriceSummary("Remove EV", changeEV);
    }

    @Override
    public List<String> purchaseSummary(Shops.Options key, Object value) {
        IVEVAction action = (IVEVAction) value;
        List<String> list = new ArrayList<>(PokeData.getStats("IV", action.IV, IV_CACHE, IVStore.MAX_IVS * 6));
        list.add("");
        list.addAll(PokeData.getStats("EV", action.EV, EV_CACHE, EVStore.MAX_TOTAL_EVS));
        return list;
    }

    @Override
    public void purchaseAction(Object value) {
        IVEVShop.IVEVAction action = (IVEVShop.IVEVAction) value;
        int lvl;
        for (Map.Entry<StatsType, Integer> entry : action.EV.entrySet()) {
            lvl = shops.pokemon.getEVs().get(entry.getKey());
            lvl = MathHelper.clamp(lvl + entry.getValue(), 0, EVStore.MAX_EVS);
            shops.pokemon.getEVs().set(entry.getKey(), lvl);
        }
        for (Map.Entry<StatsType, Integer> entry : action.IV.entrySet()) {
            lvl = shops.pokemon.getIVs().get(entry.getKey());
            lvl = MathHelper.clamp(lvl + entry.getValue(), 0, IVStore.MAX_IVS);
            shops.pokemon.getIVs().set(entry.getKey(), lvl);
        }

        // Quickly change and restore the Pokemon's level, to update the stats in the Stats menu
        int level = shops.pokemon.getLevel();
        shops.pokemon.setLevel(level - 1);
        shops.pokemon.setLevel(level);
    }

    private static class ConfigKeys {
        static final String ADD_IV = "add-iv";
        static final String REMOVE_IV = "remove-iv";
        static final String CHANGE_EV = "change-ev";
    }

    enum IVEVOption {
        HP("power_weight", StatsType.HP),
        Attack("power_bracer", StatsType.Attack),
        Defence("power_belt", StatsType.Defence),
        Special_Attack("power_lens", StatsType.SpecialAttack),
        Special_Defence("power_band", StatsType.SpecialDefence),
        Speed("power_anklet", StatsType.Speed),
        ;
        String itemID;
        StatsType statsType;

        IVEVOption(String itemID, StatsType statsType) {
            this.itemID = itemID;
            this.statsType = statsType;
        }
    }

    public class IVEVAction {
        HashMap<StatsType, Integer> IV = new HashMap<>();
        HashMap<StatsType, Integer> EV = new HashMap<>();

        public int getRequestedIV() {
            int total = 0;
            for (int i : IV.values()) total += i;
            return total;
        }

        public int getRequestedEV() {
            int total = 0;
            for (int i : EV.values()) total += i;
            return total;
        }

        public void addIV(StatsType statsType, int i) {
            int lvl = IV.getOrDefault(statsType, 0) + i;
            int iv = shops.pokemon.getIVs().get(statsType);
            if (iv + lvl > IVStore.MAX_IVS) {
                lvl = IVStore.MAX_IVS - iv;
                // 31 = iv + lvl + i
                // lvl + i = 31 - iv
            }

            IV.put(statsType, lvl);
        }

        public void removeIV(StatsType statsType, int i) {
            int lvl = IV.getOrDefault(statsType, 0) - i;
            int iv = shops.pokemon.getIVs().get(statsType);
            if (iv + lvl < 0) {
                lvl = -iv;
                // 0 = iv - lvl
                // lvl = iv
            }
            IV.put(statsType, lvl);
        }

        public void addEV(StatsType statsType, int i, int total) {
            int lvl = EV.getOrDefault(statsType, 0) + i;
            int ev = shops.pokemon.getEVs().get(statsType);
            if (ev + lvl > EVStore.MAX_EVS) {
                lvl = EVStore.MAX_EVS - ev;
                // 252 = iv + lvl
                // lvl = 252 - iv
            }

            if (total + (getRequestedEV() - EV.getOrDefault(statsType, 0)) + lvl > EVStore.MAX_TOTAL_EVS) {
                // 510 = total + req + lvl
                // lvl = 510 - total - req
                lvl = EVStore.MAX_TOTAL_EVS - total - (getRequestedEV() - EV.getOrDefault(statsType, 0));
            }
            EV.put(statsType, lvl);
        }

        public void removeEV(StatsType statsType, int i) {
            int lvl = EV.getOrDefault(statsType, 0) - i;
            int ev = shops.pokemon.getEVs().get(statsType);
            if (ev + lvl < 0) {
                lvl = -ev;
                // 0 = iv - lvl
                // lvl = iv
            }
            EV.put(statsType, lvl);
        }
    }
}
