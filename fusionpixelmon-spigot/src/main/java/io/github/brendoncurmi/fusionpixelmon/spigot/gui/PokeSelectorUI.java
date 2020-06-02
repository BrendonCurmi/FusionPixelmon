package io.github.brendoncurmi.fusionpixelmon.spigot.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvInventory;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvItem;
import io.github.brendoncurmi.fusionpixelmon.api.inventory.InvPage;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import io.github.brendoncurmi.fusionpixelmon.impl.pixelmon.PokemonWrapper;
import io.github.brendoncurmi.fusionpixelmon.spigot.SpigotAdapter;
import io.github.brendoncurmi.fusionpixelmon.spigot.api.pixelmon.PixelmonAPI;
import io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory.SpigotInvInventory;
import io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory.SpigotInvItem;
import io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory.SpigotItemStack;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PokeSelectorUI {

    private Pokemon selectedPokemon;

    public PokeSelectorUI(Player player, String name, String id, Consumer<Pokemon> consumer) {
        List<InvPage> pages = new ArrayList<>();
        InvPage pagePokeSelect = new InvPage("§8" + name, id, 1);

        InvItem partyItem;
        PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player.getUniqueId());
        for (int i = 0; i < 6; i++) {
            Pokemon pokemon = partyStorage.get(i);
            if (pokemon != null && !pokemon.isEgg()) {
                IPokemonWrapper pokemonWrapper = new PokemonWrapper(pokemon);
                partyItem = new SpigotInvItem(SpigotAdapter.adapt(PixelmonAPI.getPokeSprite(pokemon, true)), pokemonWrapper.getTitle());
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
                partyItem = new SpigotInvItem(Material.EGG, "§3Unknown");
                pagePokeSelect.setItem(i, partyItem);
            } else {
                ItemStack og = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getDyeData());
                AbstractItemStack stack = new SpigotItemStack(og);
                partyItem = new SpigotInvItem(stack, "§fEmpty party slot");
                pagePokeSelect.setItem(i, partyItem);
            }
        }

        // Party can have max 6 pokemon but inventory row has 9 slots, so fill remaining space with panes
        ItemStack og = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getDyeData());
        AbstractItemStack stack = new SpigotItemStack(og);
        InvItem emptyItem = new SpigotInvItem(stack, "");
        pagePokeSelect.setItem(6, emptyItem);
        pagePokeSelect.setItem(7, emptyItem);
        pagePokeSelect.setItem(8, emptyItem);

        pages.add(pagePokeSelect);

        InvInventory inv = new SpigotInvInventory();
        inv.add(pages);
        inv.openPage(SpigotAdapter.adapt(player), pagePokeSelect);
    }
}
