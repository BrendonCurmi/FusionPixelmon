package me.fusiondev.fusionpixelmon.sponge.modules.pokemodifiers;

import me.fusiondev.fusionpixelmon.modules.pokemodifiers.PokeModifiers;
import me.fusiondev.fusionpixelmon.sponge.SpongeAdapter;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class PokeModifiersListeners {
    @Listener
    public void onClick(InteractItemEvent event) {
        if (event.getSource() instanceof Player) {
            Player player = (Player) event.getSource();
            Optional<ItemStack> i = player.getItemInHand(HandTypes.MAIN_HAND);
            if (i.isPresent()) {
                Optional<Text> name = i.get().get(Keys.DISPLAY_NAME);
                if (name.isPresent() && PokeModifiers.hasModifier(name.get().toPlain(), true)) {
                    PokeModifiers.getModifier(name.get().toPlain(), true).action(SpongeAdapter.adapt(player), () -> {
                        for (int n = 0; n <= 8; n++) {
                            Inventory openInv = ((PlayerInventory) player.getInventory()).getHotbar().query(SlotIndex.of(n));
                            Optional<ItemStack> itemStack = openInv.first().peek();
                            if (itemStack.isPresent() && itemStack.get().equalTo(i.get())) {
                                openInv.first().poll(1);
                                break;
                            }
                        }
                    });
                }
            }
        }
    }
}
