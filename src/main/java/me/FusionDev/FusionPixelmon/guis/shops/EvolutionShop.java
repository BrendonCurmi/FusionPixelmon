package me.FusionDev.FusionPixelmon.guis.shops;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.evolution.Evolution;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import me.FusionDev.FusionPixelmon.apis.Time;
import me.FusionDev.FusionPixelmon.inventory.InvItem;
import me.FusionDev.FusionPixelmon.inventory.InvPage;
import me.FusionDev.FusionPixelmon.pixelmon.PixelmonAPI;

import java.util.ArrayList;
import java.util.List;

public class EvolutionShop extends Shops.BaseShop {
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
            InvItem item = new InvItem(PixelmonAPI.getPokeSprite(spec.create()), "§f" + spec.name);
            page.setItem(i, item, event -> {
                if (!spec.name.equals(shops.pokemon.getSpecies().name)) {
                    PokemonSpecWrapper wrapper = (PokemonSpecWrapper) shops.getSelectedOptions().getOrDefault(getOption(), new PokemonSpecWrapper());
                    wrapper.setPokemonSpec(spec);
                    shops.getSelectedOptions().put(getOption(), wrapper);
                }
                builder.setSelectedItem(item.itemStack);
            });
            i++;
        }
        return page;
    }

    @Override
    public int prices(Object value) {
        return getPriceOf(ConfigKeys.CHANGE, 10000);
    }

    @Override
    protected void priceSummaries() {
        addPriceSummary("Change Evolution", getPriceOf(ConfigKeys.CHANGE, 10000));
    }

    @Override
    public void purchaseAction(Object value) {
        PokemonSpecWrapper evolve = (PokemonSpecWrapper) value;
        Time.setTimeout(() -> shops.pokemon.evolve(evolve.pokemonSpec), 1000);
    }

    private static class ConfigKeys {
        static final String CHANGE = "change";
    }

    private List<PokemonSpec> getEvolutionsList(Pokemon pokemon) {
        List<PokemonSpec> evolutions = new ArrayList<>();

        // Invert list to get proper evolution order
        EnumSpecies[] preEvolutions = pokemon.getBaseStats().preEvolutions;
        for (int i = 0; i < preEvolutions.length; i++) {
            evolutions.add(PokemonSpec.from(preEvolutions[preEvolutions.length - i - 1].name));
        }

        evolutions.add(PixelmonAPI.fromPokemon(pokemon, false));

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

        void setPokemonSpec(PokemonSpec pokemonSpec) {
            this.pokemonSpec = pokemonSpec;
        }

        @Override
        public String toString() {
            return pokemonSpec.name;
        }
    }
}
