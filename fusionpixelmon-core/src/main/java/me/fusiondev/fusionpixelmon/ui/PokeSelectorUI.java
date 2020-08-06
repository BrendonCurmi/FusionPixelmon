package me.fusiondev.fusionpixelmon.ui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.Registry;
import me.fusiondev.fusionpixelmon.api.AbstractPlayer;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.inventory.InvInventory;
import me.fusiondev.fusionpixelmon.api.inventory.InvItem;
import me.fusiondev.fusionpixelmon.api.inventory.InvPage;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.pixelmon.IPokemonWrapper;
import me.fusiondev.fusionpixelmon.impl.pixelmon.PokemonWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Creates and opens an inventory GUI which allows selecting of pokemon
 * from the Player's current party of pokemon.
 */
public class PokeSelectorUI {

    private Pokemon selectedPokemon;

    public PokeSelectorUI(AbstractPlayer player, String name, String id, Consumer<Pokemon> consumer) {
        Registry reg = FusionPixelmon.getRegistry();

        List<InvPage> pages = new ArrayList<>();
        InvPage pagePokeSelect = new InvPage("§8" + name, id, 1);

        InvItem partyItem;
        PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player.getUniqueId());
        for (int i = 0; i < 6; i++) {
            Pokemon pokemon = partyStorage.get(i);
            if (pokemon != null && !pokemon.isEgg()) {
                IPokemonWrapper pokemonWrapper = new PokemonWrapper(pokemon);
                partyItem = new InvItem(reg.getPixelmonUtils().getPokeSprite(pokemon, true), pokemonWrapper.getTitle());
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
                partyItem = new InvItem(reg.getItemTypesRegistry().EGG(), "§3Unknown");
                pagePokeSelect.setItem(i, partyItem);
            } else {
                //ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
                //itemStack.offer(Keys.DYE_COLOR, DyeColors.WHITE);
                AbstractItemStack itemStack = reg.getItemTypesRegistry().STAINED_GLASS_PANE().to();
                itemStack.setColour(DyeColor.WHITE);
                partyItem = new InvItem(itemStack, "§fEmpty party slot");
                pagePokeSelect.setItem(i, partyItem);
            }
        }

        // Party can have max 6 pokemon but inventory row has 9 slots, so fill remaining space with panes
        AbstractItemStack emptyStack = reg.getItemTypesRegistry().STAINED_GLASS_PANE().to();
        emptyStack.setColour(DyeColor.BLACK);
        //ItemStack emptyStack = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
        //emptyStack.offer(Keys.DYE_COLOR, DyeColors.BLACK);
        InvItem emptyItem = new InvItem(emptyStack, "");
        pagePokeSelect.setItem(6, emptyItem);
        pagePokeSelect.setItem(7, emptyItem);
        pagePokeSelect.setItem(8, emptyItem);

        pages.add(pagePokeSelect);

        InvInventory inv = reg.getInvInventory();
        inv.add(pages);
        inv.openPage(player, pagePokeSelect);
    }
}
