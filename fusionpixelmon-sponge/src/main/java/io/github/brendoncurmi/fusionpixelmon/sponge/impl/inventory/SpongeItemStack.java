package io.github.brendoncurmi.fusionpixelmon.sponge.impl.inventory;

import io.github.brendoncurmi.fusionpixelmon.api.Raw;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemStack;
import io.github.brendoncurmi.fusionpixelmon.api.items.AbstractItemType;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.List;

public class SpongeItemStack extends AbstractItemStack {

    ItemStack itemStack;

    public SpongeItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public AbstractItemStack from(AbstractItemType itemType) {
        return null;
    }


    @Override
    public void setName(String name) {

    }

    @Override
    public List<String> getLore() {
        return null;
    }

    @Override
    public void setLore(List<String> itemLore) {

    }

    @Override
    public ItemStack getRaw() {
        //return ItemStack.builder().fromItemStack(itemStack).add(Keys.DISPLAY_NAME, Text.of(name)).build();
        return ItemStack.builder().fromItemStack(itemStack).build();
    }
}
