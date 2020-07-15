package me.FusionDev.FusionPixelmon.sponge.impl.inventory;

import me.FusionDev.FusionPixelmon.sponge.SpongeAdapter;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemStack;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SpongeItemStack extends AbstractItemStack {

    private ItemStack itemStack;

    public SpongeItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public AbstractItemStack from(AbstractItemType itemType) {
        return null;
    }

    @Override
    public AbstractItemStack setName(String name) {
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(name));
        return this;
    }

    @Override
    public List<String> getLore() {
        if (itemStack.get(Keys.ITEM_LORE).isPresent()) {
            return toStringList(itemStack.get(Keys.ITEM_LORE).get());
        }
        return new ArrayList<>();
    }

    @Override
    public AbstractItemStack setLore(List<String> itemLore) {
        itemStack.offer(Keys.ITEM_LORE, toTextList(itemLore));
        return this;
    }

    @Override
    public AbstractItemStack setColour(Object colour) {
        if (colour instanceof DyeColor) {
            itemStack.offer(Keys.DYE_COLOR, (DyeColor) colour);
        } else if (colour instanceof me.fusiondev.fusionpixelmon.api.colour.DyeColor) {
            itemStack.offer(Keys.DYE_COLOR, SpongeAdapter.adapt((me.fusiondev.fusionpixelmon.api.colour.DyeColor) colour));
        }
        return this;
    }

    @Override
    public ItemStack getRaw() {
        //return ItemStack.builder().fromItemStack(itemStack).add(Keys.DISPLAY_NAME, Text.of(name)).build();
        return itemStack;
    }

    private List<Text> toTextList(List<String> list) {
        List<Text> lore = new ArrayList<>();
        for (String string : list) {
            lore.add(Text.of(string));
        }
        return lore;
    }

    private List<String> toStringList(List<Text> list) {
        List<String> lore = new ArrayList<>();
        for (Text text : list) {
            lore.add(text.toPlain());
        }
        return lore;
    }
}
