package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;

import java.util.List;

public class FormShop extends BaseShop {
    public FormShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.FORM;
    }

    // todo disclaimer about item types which change form
    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Form Modification", "pokeeditor-form", 6)
                .setInfoItemData("Form Info",
                        "To select a form for your Pokemon",
                        "select one of the above options.")
                .setSelectedItemName("Selected Form")
                .setSelectedSlot(46)
                .setInfoSlot(48)
                .setResetSlot(50)
                .setBackSlot(52)
                .border(true)
                .setSelectedOption(getOption());
        InvPage page = builder.build();

//        InvItem emptyItem = new InvItem(SpongeAdapter.adapt(ItemTypes.STAINED_GLASS_PANE), "").setKey(Keys.DYE_COLOR, DyeColors.BLACK);
//        page.setBackground(emptyItem);

        PokemonSpec spec = PokemonSpec.from(shops.pokemon.getSpecies().getPokemonName());
        spec.boss = null;
        Pokemon pokemon = spec.create();
        pokemon.setShiny(shops.pokemon.isShiny());

//        Pokemon pokemon1 = pokemon;
        int i = 9;
        List<IEnumForm> forms = pokemon.getSpecies().getPossibleForms(true);
        for (IEnumForm form : forms) {
            pokemon.setForm(form);
            InvItem item1 = new InvItem(FusionPixelmon.getRegistry().getPixelmonUtils().getPokeSprite(pokemon), (pokemon.isShiny() ? "§3Shiny " : "§3") + pokemon.getSpecies().getPokemonName() + " §8(§e" + form.getLocalizedName() + "§8)");
            page.setItem(i, item1, event -> {
                if (shops.pokemon.getFormEnum() != form) shops.getSelectedOptions().put(getOption(), form);
                else shops.getSelectedOptions().remove(getOption());
                builder.setSelectedItem(item1.getItemStack());
            });
            i++;
        }
//        pokemon = pokemon1;
        return page;
    }

    @Override
    public int prices(Object value) {
        return getPriceOf(ConfigKeys.CHANGE, 4000);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Form Change", getPriceOf(ConfigKeys.CHANGE, 4000));
    }

    @Override
    public void purchaseAction(Object value) {
        shops.pokemon.setForm((IEnumForm) value);
    }

    private static class ConfigKeys {
        static final String CHANGE = "change";
    }
}
