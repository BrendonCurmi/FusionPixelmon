package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import me.fusiondev.fusionpixelmon.api.colour.Color;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class MoveShop extends BaseShop {
    public MoveShop(Shops shops) {
        super(shops, true);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.MOVE;
    }

    private InvPage page;

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Move Modification", "pokeeditor-move", 3)
                .setInfoItemData("Move Info",
                        "To pick a move for your Pokemon simply",
                        "click the move you wish to replace and",
                        "then choose the move you wish to",
                        "learn instead.")
                .setSelectedItemName("Selected Move")
                .setSelectedSlot(19)
                .setInfoSlot(21)
                .setResetSlot(23)
                .setBackSlot(25)
                .border(true)
                .setSelectedOption(getOption());
        InvPage page = this.page = builder.build();

        for (int i = 0; i < MoveOption.MOVES_NUM; i++) {
            final int i1 = i;
            AbstractItemStack abstractItemStack;
            if (shops.pokemon.getMoveset().get(i1) != null) {
                abstractItemStack = REG.getItemTypesRegistry().STAINED_GLASS_PANE().to();
                abstractItemStack.setColour(DyeColor.YELLOW);
            } else {
                abstractItemStack = REG.getItemTypesRegistry().STAINED_GLASS_PANE().to();
                abstractItemStack.setColour(DyeColor.WHITE);
            }

            InvItem item = new InvItem(abstractItemStack, "§3§l" +
                    (shops.pokemon.getMoveset().get(i1) != null
                            ? shops.pokemon.getMoveset().get(i1).getActualMove().getLocalizedName()
                            : "Empty"));
            page.setItem((i1 * 2) + 1, item, event -> buildMoveSelectPage(i1, 0));
        }
        return page;
    }

    private void buildMoveSelectPage(int i, int pageNum) {
        int rows = 6;
        String title = shops.pokemon.getMoveset().get(i) != null ? "Replacing " + shops.pokemon.getMoveset().get(i).getActualMove().getLocalizedName() : "Teaching";
        InvPage page = new InvPage(title, "move-" + i, rows);

        int space = 36;
        for (int j = space; j >= space && j <= (rows * 9) - 1; j++) {
            page.setItem(j, EMPTY_ITEM);
        }

        InvItem cancelItem = new InvItem(REG.getItemTypesRegistry().DYE().to().setColour(DyeColor.RED), Color.RED.toString() + Color.BOLD.toString() + "Cancel");
        page.setItem(49, cancelItem, event -> shops.getInv().openPage(shops.player, this.page));

        int pos = 0;
        for (Attack attack : shops.pokemon.getBaseStats().getAllMoves()
                .stream()
                .filter(s -> !shops.pokemon.getMoveset().contains(s))
                .skip(space * pageNum)
                .limit(space)
                .collect(Collectors.toList())) {

            AbstractItemStack tmItem = REG.getPixelmonUtils().getPixelmonItemType("tm1").to();
            InvItem item1 = new InvItem(tmItem, Color.WHITE + attack.getActualMove().getLocalizedName());
            item1.setLore("Teach your Pokemon this move");
            page.setItem(pos++, item1, event -> {
                MoveOption option = (MoveOption) shops.getSelectedOptions().getOrDefault(getOption(), new MoveOption());
                option.set(i, attack);
                shops.getSelectedOptions().put(getOption(), option);
                shops.getInv().openPage(shops.player, this.page);
            });
        }

        if (pageNum != 0) {
            InvItem backItem = new InvItem(REG.getPixelmonUtils().getPixelmonItemStack("trade_holder_left"), "§3§lBack");
            page.setItem(45, backItem, event -> buildMoveSelectPage(i, pageNum - 1));
        }

        int nextPageSize = (int) shops.pokemon.getBaseStats().getAllMoves()
                .stream()
                .filter(s -> !shops.pokemon.getMoveset().contains(s))
                .skip(space * (pageNum + 1))
                .limit(space).count();
        if (nextPageSize != 0) {
            InvItem nextItem = new InvItem(REG.getPixelmonUtils().getPixelmonItemStack("trade_holder_right"), "§3§lNext");
            page.setItem(53, nextItem, event -> buildMoveSelectPage(i, pageNum + 1));
        }

        shops.getInv().openPage(shops.player, page);
    }

    @Override
    public int prices(Object value) {
        MoveOption moves = (MoveOption) value;
        int price = 0;
        for (int i = 0; i < MoveOption.MOVES_NUM; i++)
            if (moves.has(i))
                price += getPriceOf(ConfigKeyConstants.CHANGE_MOVES, 10000);
        return price;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Change Move", getPriceOf(ConfigKeyConstants.CHANGE_MOVES, 10000) + " per move");
    }

    @Override
    public List<String> purchaseSummary(Shops.Options key, Object value) {
        MoveOption moveOption = (MoveOption) value;
        List<String> list = new ArrayList<>();
        list.add("Moves:");
        for (int i = 0; i < MoveOption.MOVES_NUM; i++) {
            StringBuilder builder = new StringBuilder("      " + (i + 1) + ": ");
            builder.append(shops.pokemon.getMoveset().get(i) != null ? shops.pokemon.getMoveset().get(i).getActualMove().getLocalizedName() : "Empty");
            if (moveOption.has(i))
                builder.append(" -> ").append(Color.YELLOW).append(moveOption.get(i).getActualMove().getLocalizedName());
            list.add(builder.toString());
        }
        return list;
    }

    @Override
    public void purchaseAction(Object value) {
        if (!(value instanceof MoveOption)) return;
        for (int i = 0; i < MoveOption.MOVES_NUM; i++) {
            if (((MoveOption) value).has(i)) {
                shops.pokemon.getMoveset().set(i, ((MoveOption) value).get(i));
            }
        }
    }

    private static class ConfigKeyConstants {
        private static final String CHANGE_MOVES = "change-moves";
    }

    private static class MoveOption {
        private static final int MOVES_NUM = 4;
        private final Attack[] MOVES = new Attack[MOVES_NUM];

        public Attack get(int i) {
            return MOVES[i];
        }

        public boolean has(int i) {
            return get(i) != null;
        }

        public void set(int i, Attack attack) {
            MOVES[i] = attack;
        }

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(", ");
            joiner.setEmptyValue("N/A");
            for (Attack attack : MOVES) {
                if (attack == null) continue;
                joiner.add(attack.getActualMove().getLocalizedName());
            }
            return joiner.toString();
        }
    }
}
