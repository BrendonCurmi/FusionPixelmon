package me.fusiondev.fusionpixelmon.modules.pokedesigner.ui;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.ui.BaseShop;
import me.fusiondev.fusionpixelmon.api.ui.Shops;
import me.fusiondev.fusionpixelmon.impl.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class EvolutionShop extends BaseShop {
    public EvolutionShop(Shops shops) {
        super(shops);
    }

    @Override
    public Shops.Options getOption() {
        return Shops.Options.EVOLUTION;
    }

    @Override
    public InvPage buildPage() {
        Builder builder = new Builder("§0Evolution Modification", "pokeeditor-evolution", 5)
                .setInfoItemData("Evolution Info",
                        "To modify the evolution of your Pokemon",
                        "simply use the above options.",
                        "",
                        "Note: can only get evolutions obtained by levelling")
                .setSelectedItemName("Selected Evolution")
                .setSelectedSlot(37)
                .setInfoSlot(39)
                .setResetSlot(41)
                .setBackSlot(43)
                .border(true)
                .setSelectedOption(getOption());
        InvPage page = builder.build();

        int i = 9;
        for (PokemonSpec spec : getEvolutionsList(shops.pokemon)) {
            InvItem item = new InvItem(FusionPixelmon.getRegistry().getPixelmonUtils().getPokeSprite(spec.create()), "§f" + spec.name);
            page.setItem(i, item, event -> {
                if (!spec.name.equals(shops.pokemon.getSpecies().name)) {
                    PokemonSpecWrapper wrapper = (PokemonSpecWrapper) shops.getSelectedOptions().getOrDefault(getOption(), new PokemonSpecWrapper());
                    wrapper.setPokemonSpec(spec);
                    shops.getSelectedOptions().put(getOption(), wrapper);
                }
                builder.setSelectedItem(item.getItemStack());
            });
            i++;
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        return getPriceOf(ConfigKeyConstants.CHANGE, 10000);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Change Evolution", getPriceOf(ConfigKeyConstants.CHANGE, 10000));
    }

    @Override
    public void purchaseAction(Object value) {
        PokemonSpecWrapper evolve = (PokemonSpecWrapper) value;
        TimeUtils.setTimeout(() -> shops.pokemon.evolve(evolve.pokemonSpec), 1000);
    }

    private static class ConfigKeyConstants {
        private static final String CHANGE = "change";
    }

    private List<PokemonSpec> getEvolutionsList(Pokemon pokemon) {
        List<PokemonSpec> evolutions = new ArrayList<>();

        // Invert list to get proper evolution order
        PokemonSpec[] preEvolutions = pokemon.getBaseStats().specPreEvolutions;
        if (preEvolutions != null && preEvolutions.length > 0) {
            for (int i = preEvolutions.length - 1; i >= 0; i--) {
                evolutions.add(preEvolutions[i]);
            }
        }

        evolutions.add(FusionPixelmon.getRegistry().getPixelmonUtils().fromPokemon(pokemon, false));

        List<Evolution> currEvolutions = pokemon.getBaseStats().evolutions;
        while (!currEvolutions.isEmpty()) {
            for (Evolution ev : currEvolutions) {
                // Only show evolutions that can be obtained through simple
                // levelling up. Without this, users can cheat by circumventing
                // evolutions that require trading, or holding items, etc.
                if (ev.evoType.equals("leveling") && ev.conditions.isEmpty()) {
                    evolutions.add(ev.to);
                }
                currEvolutions = ev.to.create().getBaseStats().evolutions;
            }
        }
        return evolutions;
    }

    private static class PokemonSpecWrapper {
        private PokemonSpec pokemonSpec;

        private void setPokemonSpec(PokemonSpec pokemonSpec) {
            this.pokemonSpec = pokemonSpec;
        }

        @Override
        public String toString() {
            return pokemonSpec.name;
        }
    }
}
