package me.FusionDev.FusionPixelmon.gui;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import me.FusionDev.FusionPixelmon.api.inventory.InvInventory;
import me.FusionDev.FusionPixelmon.api.inventory.InvItem;
import me.FusionDev.FusionPixelmon.api.inventory.InvPage;
import me.FusionDev.FusionPixelmon.api.pixelmon.PixelmonAPI;
import me.FusionDev.FusionPixelmon.api.pixelmon.PokeData;
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
                PokeData pokeData = new PokeData(pokemon);
                partyItem = new InvItem(PixelmonAPI.getPokeSprite(pokemon, true), pokeData.getTitle());
                partyItem.setLoreWait(
                        pokeData.getAbility(),
                        pokeData.getNature(),
                        "",
                        pokeData.getGender(),
                        pokeData.getSize(),
                        "",
                        pokeData.getIVs(),
                        ""
                );
                if (pokeData.hasTexture()) partyItem.appendLore(pokeData.getTexture());
                if (pokemon.getPokerus() != null) partyItem.appendLore(pokeData.getPokerus());
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
        inv.openPage(player, pagePokeSelect);
    }
}
