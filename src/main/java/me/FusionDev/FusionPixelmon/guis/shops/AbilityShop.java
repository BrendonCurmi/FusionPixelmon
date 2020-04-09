package me.FusionDev.FusionPixelmon.guis.shops;

import me.FusionDev.FusionPixelmon.inventory.InvItem;
import me.FusionDev.FusionPixelmon.inventory.InvPage;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemTypes;

public class AbilityShop extends Shops.BaseShop {
    public AbilityShop(Shops shops) {
        super(shops);
    }

    private int abilitySlot = 0;

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Ability Modification", "pokeeditor-ability", 5)
                .setInfoItemData("Ability Info",
                        "To pick an ability for your Pokemon",
                        "simply select one of the possible",
                        "options on the right.")
                .setSelectedItemName("Selected Ability")
                .setSelectedOption(Shops.Options.ABILITY);
        InvPage page = builder.build();

        int[] slots = new int[]{20, 22, 24};
        DyeColor[] colors = new DyeColor[]{DyeColors.GREEN, DyeColors.YELLOW, DyeColors.RED};
        String[] allAbilities = shops.pokemon.getBaseStats().abilities;
        for (int i = 0; i < allAbilities.length; i++) {
            if (allAbilities[i] == null || (i == 1 && allAbilities[1].equals(allAbilities[0]))) {
                InvItem item = new InvItem(ItemTypes.BARRIER, "§c§lN/A");
                page.setItem(slots[i], item);
            } else {
                String ability = allAbilities[i];
                InvItem item = new InvItem(ItemTypes.STAINED_HARDENED_CLAY, "§3§l" + ability).setKey(Keys.DYE_COLOR, colors[i]);
                int finalI = i;
                page.setItem(slots[i], item, event -> {
                    if (!shops.pokemon.getAbilityName().equalsIgnoreCase(ability)) {
                        abilitySlot = finalI;
                        shops.getSelectedOptions().put(Shops.Options.ABILITY, ability);
                    } else {
                        abilitySlot = 0;
                        shops.getSelectedOptions().remove(Shops.Options.ABILITY);
                    }
                    builder.setSelectedItem(item.itemStack);
                });
            }
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        return (abilitySlot == 2) ? 8000 : 750;
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Regular Ability", 750);
        addPriceSummary("Special Ability", 8000);
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setAbility(value.toString());
    }
}
