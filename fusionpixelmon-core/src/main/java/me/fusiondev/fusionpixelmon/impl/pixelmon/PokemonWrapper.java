package me.fusiondev.fusionpixelmon.impl.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import me.fusiondev.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import me.fusiondev.fusionpixelmon.impl.GrammarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains information on the Pokemon.
 */
public class PokemonWrapper implements IPokemonWrapper {
    private final Pokemon pokemon;

    public PokemonWrapper(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    @Override
    public String getTitle() {
        return "§b" + getName() + "§r §7: §eLvl " + pokemon.getLevel() + getIfShiny();
    }

    @Override
    public String getIfShiny() {
        return pokemon.isShiny() ? " §7(§6Shiny§7)" : "";
    }

    @Override
    public String getSpeciesName() {
        return pokemon.getSpecies().getPokemonName();
    }

    @Override
    public String getName() {
        return (pokemon.getNickname() == null || pokemon.getNickname().isEmpty()) ? getSpeciesName() : pokemon.getNickname();
    }

    @Override
    public String getAbility() {
        return "§7Ability: §e" + pokemon.getAbilityName();
    }

    @Override
    public String getNature(boolean showMint) {
        // gets the base nature, not the mint nature
        return "§7Nature: §e" + pokemon.getBaseNature().getLocalizedName() + (showMint && pokemon.getMintNature() != null ? " (+ " + pokemon.getMintNature() + " Mint)" : "");
    }

    @Override
    public String getGender() {
        String gender;
        switch (pokemon.getGender()) {
            default:
            case None:
                gender = "§eNone";
                break;
            case Male:
                gender = "§bMale";
                break;
            case Female:
                gender = "§dFemale";
                break;
        }
        return "§7Gender: " + gender;
    }

    @Override
    public String getSize() {
        return "§7Size: §e" + pokemon.getGrowth().name();
    }

    @Override
    public String getForm() {
        return "§7Form: §e" + pokemon.getFormEnum().getLocalizedName();
    }

    @Override
    public String getCaughtBall() {
        return "§7Pokeball: §e" + pokemon.getCaughtBall().name();
    }

    @Override
    public boolean hasSpecialTexture() {
        return pokemon.getForm() > 0 && !pokemon.getFormEnum().isDefaultForm();
    }

    @Override
    public boolean hasTexture() {
        return hasSpecialTexture() || !pokemon.getCustomTexture().isEmpty();
    }

    @Override
    public String getTexture() {
        return "§7Texture: §e"
                + GrammarUtils.cap(hasSpecialTexture()
                ? pokemon.getFormEnum().getLocalizedName()
                : pokemon.getCustomTexture());
    }

    @Override
    public String getPokerus() {
        return "§dPokerus";
    }

    private static final String STATS = "§8(§cHP§8/§cA§8/§cD§8/§cSA§8/§cSD§8/§cSPD§8)";

    public static List<String> getStats(String ivev, HashMap<StatsType, Integer> stats, int[] cache, int max) {
        StringBuilder s = new StringBuilder("    §8(§e");
        StatsType[] types = StatsType.getStatValues();
        int total = 0;
        int newVal;
        for (int i = 0; i < types.length; i++) {
            if (s.length() > 9) s.append("§8/§e");
            newVal = stats.getOrDefault(types[i], 0) + cache[i];
            total += newVal;
            s.append(newVal);
        }
        s.append("§8)");

        List<String> result = new ArrayList<>();
        result.add(ivev + ": " + IPokemonWrapper.beautifyTally(total, max));
        result.add(s.toString());
        result.add("    " + STATS);
        return result;
    }

    @Override
    public List<String> getIVs() {
        int total = 0;
        int max = 186;// 31 max IV * 6 stat types
        for (StatsType stats : StatsType.getStatValues()) total += pokemon.getIVs().getStat(stats);
        List<String> result = new ArrayList<>();
        result.add("IVs: " + IPokemonWrapper.beautifyTally(total, max));
        result.add(
                "    §8(§e" + pokemon.getIVs().getStat(StatsType.HP) +
                        "§8/§e" + pokemon.getIVs().getStat(StatsType.Attack) +
                        "§8/§e" + pokemon.getIVs().getStat(StatsType.Defence) +
                        "§8/§e" + pokemon.getIVs().getStat(StatsType.SpecialAttack) +
                        "§8/§e" + pokemon.getIVs().getStat(StatsType.SpecialDefence) +
                        "§8/§e" + pokemon.getIVs().getStat(StatsType.Speed) + "§8)");
        result.add("    " + STATS);
        return result;
    }

    @Override
    public List<String> getEVs() {
        int total = 0;
        int max = EVStore.MAX_TOTAL_EVS;// 510
        for (StatsType stats : StatsType.getStatValues()) total += pokemon.getEVs().getStat(stats);
        List<String> result = new ArrayList<>();
        result.add("EVs: " + IPokemonWrapper.beautifyTally(total, max));
        result.add(
                "    §8(§e" + pokemon.getEVs().getStat(StatsType.HP) +
                        "§8/§e" + pokemon.getEVs().getStat(StatsType.Attack) +
                        "§8/§e" + pokemon.getEVs().getStat(StatsType.Defence) +
                        "§8/§e" + pokemon.getEVs().getStat(StatsType.SpecialAttack) +
                        "§8/§e" + pokemon.getEVs().getStat(StatsType.SpecialDefence) +
                        "§8/§e" + pokemon.getEVs().getStat(StatsType.Speed) + "§8)");
        result.add("    " + STATS);
        return result;
    }
}
