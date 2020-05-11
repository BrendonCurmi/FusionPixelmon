package me.FusionDev.FusionPixelmon.api.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains information on the Pokemon.
 */
public class PokeData {
    private Pokemon pokemon;

    public PokeData(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public String getTitle() {
        return "§b" + getName() + " §7: §eLvl " + pokemon.getLevel() + getIfShiny();
    }

    public String getIfShiny() {
        return pokemon.isShiny() ? " §7(§6Shiny§7)" : "";
    }

    public String getSpeciesName() {
        return pokemon.getSpecies().getPokemonName();
    }

    public String getName() {
        return (pokemon.getNickname() == null || pokemon.getNickname().isEmpty()) ? getSpeciesName() : pokemon.getNickname();
    }

    public String getAbility() {
        return "§7Ability: §e" + pokemon.getAbilityName();
    }

    public String getNature() {
        return "§7Nature: §e" + pokemon.getNature().getLocalizedName();
    }

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

    public String getSize() {
        return "§7Size: §e" + pokemon.getGrowth().name();
    }

    public String getForm() {
        return "§7Form: §e" + pokemon.getFormEnum().getLocalizedName();
    }

    public String getCaughtBall() {
        return "§7Pokeball: §e" + pokemon.getCaughtBall().name();
    }

    public String getCustomTexture() {
        return "§7Texture: §e" + pokemon.getCustomTexture();
    }

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
        result.add(ivev + ": " + PixelmonAPI.beautifyTally(total, max));
        result.add(s.toString());
        result.add("    " + STATS);
        return result;
    }

    public List<String> getIVs() {
        int total = 0;
        int max = 186;// 31 max IV * 6 stat types
        for (StatsType stats : StatsType.getStatValues()) total += pokemon.getIVs().get(stats);
        List<String> result = new ArrayList<>();
        result.add("IVs: " + PixelmonAPI.beautifyTally(total, max));
        result.add(
                "    §8(§e" + pokemon.getIVs().get(StatsType.HP) +
                        "§8/§e" + pokemon.getIVs().get(StatsType.Attack) +
                        "§8/§e" + pokemon.getIVs().get(StatsType.Defence) +
                        "§8/§e" + pokemon.getIVs().get(StatsType.SpecialAttack) +
                        "§8/§e" + pokemon.getIVs().get(StatsType.SpecialDefence) +
                        "§8/§e" + pokemon.getIVs().get(StatsType.Speed) + "§8)");
        result.add("    " + STATS);
        return result;
    }

    public List<String> getEVs() {
        int total = 0;
        int max = EVStore.MAX_TOTAL_EVS;// 510
        for (StatsType stats : StatsType.getStatValues()) total += pokemon.getEVs().get(stats);
        List<String> result = new ArrayList<>();
        result.add("EVs: " + PixelmonAPI.beautifyTally(total, max));
        result.add(
                "    §8(§e" + pokemon.getEVs().get(StatsType.HP) +
                        "§8/§e" + pokemon.getEVs().get(StatsType.Attack) +
                        "§8/§e" + pokemon.getEVs().get(StatsType.Defence) +
                        "§8/§e" + pokemon.getEVs().get(StatsType.SpecialAttack) +
                        "§8/§e" + pokemon.getEVs().get(StatsType.SpecialDefence) +
                        "§8/§e" + pokemon.getEVs().get(StatsType.Speed) + "§8)");
        result.add("    " + STATS);
        return result;
    }
}
