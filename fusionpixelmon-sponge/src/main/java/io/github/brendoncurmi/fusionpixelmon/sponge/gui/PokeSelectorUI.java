package io.github.brendoncurmi.fusionpixelmon.sponge.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.brendoncurmi.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import io.github.brendoncurmi.fusionpixelmon.sponge.api.pixelmon.PixelmonAPI;
import io.github.brendoncurmi.fusionpixelmon.impl.pixelmon.PokemonWrapper;
import io.github.brendoncurmi.fusionpixelmon.sponge.SpongeAdapter;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory.InvInventory;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory.InvPage;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Creates and opens an inventory GUI which allows selecting of pokemon
 * from the Player's current party of pokemon.
 */
public class PokeSelectorUI {

    private Pokemon selectedPokemon;

    public PokeSelectorUI(Player player, String name, String id, Consumer<Pokemon> consumer) {
        List<InvPage> pages = new ArrayList<>();
        InvPage pagePokeSelect = new InvPage("§8" + name, id, 1);

        InvItem partyItem;
        PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty((EntityPlayerMP) player);
        for (int i = 0; i < 6; i++) {
            Pokemon pokemon = partyStorage.get(i);
            if (pokemon != null && !pokemon.isEgg()) {
                IPokemonWrapper pokemonWrapper = new PokemonWrapper(pokemon);
                partyItem = new InvItem(PixelmonAPI.getPokeSprite(pokemon, true), pokemonWrapper.getTitle());
                partyItem.setLoreWait(
                        pokemonWrapper.getAbility(),
                        pokemonWrapper.getNature(),
                        "",
                        pokemonWrapper.getGender(),
                        pokemonWrapper.getSize(),
                        "",
                        pokemonWrapper.getIVs(),
                        ""
                );
                if (pokemonWrapper.hasTexture()) partyItem.appendLore(pokemonWrapper.getTexture());
                if (pokemon.getPokerus() != null) partyItem.appendLore(pokemonWrapper.getPokerus());
                partyItem.pushLore(true);

                pagePokeSelect.setItem(i, partyItem, event -> {
                    selectedPokemon = pokemon;
                    consumer.accept(selectedPokemon);
                });
            } else if (pokemon != null) {
                partyItem = new InvItem(ItemTypes.EGG, "§3Unknown");
                pagePokeSelect.setItem(i, partyItem);
            } else {
                partyItem = new InvItem(ItemTypes.STAINED_GLASS_PANE, "§fEmpty party slot").setKey(Keys.DYE_COLOR, DyeColors.WHITE);
                pagePokeSelect.setItem(i, partyItem);
            }
        }

        // Party can have max 6 pokemon but inventory row has 9 slots, so fill remaining space with panes
        InvItem emptyItem = new InvItem(ItemTypes.STAINED_GLASS_PANE, "").setKey(Keys.DYE_COLOR, DyeColors.BLACK);
        pagePokeSelect.setItem(6, emptyItem);
        pagePokeSelect.setItem(7, emptyItem);
        pagePokeSelect.setItem(8, emptyItem);

        pages.add(pagePokeSelect);

        InvInventory inv = new InvInventory();
        inv.add(pages);
        inv.openPage(SpongeAdapter.adapt(player), pagePokeSelect);
    }
}
