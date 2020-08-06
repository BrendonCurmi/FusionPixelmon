package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;

public class AbilityShop extends BaseShop {
    public AbilityShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.ABILITY;
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
                .setSelectedOption(getOption());
        InvPage page = builder.build();
        Registry reg = FusionPixelmon.getRegistry();


        int[] slots = new int[]{20, 22, 24};
        DyeColor[] colors = new DyeColor[]{DyeColor.GREEN, DyeColor.YELLOW, DyeColor.RED};
        String[] allAbilities = shops.pokemon.getBaseStats().abilities;
        for (int i = 0; i < allAbilities.length; i++) {
            if (allAbilities[i] == null || (i == 1 && allAbilities[1].equals(allAbilities[0]))) {
                InvItem item = new InvItem(reg.getItemTypesRegistry().BARRIER(), "§c§lN/A");
                page.setItem(slots[i], item);
            } else {
                String ability = allAbilities[i];

                AbstractItemStack itemStack = reg.getItemTypesRegistry().STAINED_HARDENED_CLAY().to();
                itemStack.setColour(colors[i]);

                //ItemStack abilityStack = ItemStack.builder().itemType(ItemTypes.STAINED_HARDENED_CLAY).build();
                //abilityStack.offer(Keys.DYE_COLOR, colors[i]);
                InvItem item = new InvItem(itemStack, "§3§l" + ability);
                int finalI = i;
                page.setItem(slots[i], item, event -> {
                    if (!shops.pokemon.getAbilityName().equalsIgnoreCase(ability)) {
                        abilitySlot = finalI;
                        shops.getSelectedOptions().put(getOption(), ability);
                    } else {
                        abilitySlot = 0;
                        shops.getSelectedOptions().remove(getOption());
                    }
                    builder.setSelectedItem(item.getItemStack());
                });
            }
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        return (abilitySlot == 2) ? getPriceOf(ConfigKeyConstants.SPECIAL, 8000) : getPriceOf(ConfigKeyConstants.REGULAR, 750);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Regular Ability", getPriceOf(ConfigKeyConstants.REGULAR, 750));
        addPriceSummary("Special Ability", getPriceOf(ConfigKeyConstants.SPECIAL, 8000));
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setAbility(value.toString());
    }

    private static class ConfigKeyConstants {
        private static final String REGULAR = "regular";
        private static final String SPECIAL = "special";
    }
}
