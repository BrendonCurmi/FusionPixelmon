package me.fusiondev.fusionpixelmon.sponge.modules.masterball;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

public class SpongeMasterballModule {
    public SpongeMasterballModule() {
        ItemStack dye = ItemStack.builder().itemType(ItemTypes.DYE).build();
        dye.offer(Keys.DYE_COLOR, DyeColors.PURPLE);
        Sponge.getRegistry().getCraftingRecipeRegistry().register(
                ShapedCraftingRecipe.builder()
                        .aisle("PPP", "OBO", "DDD")
                        .where('P', Ingredient.of(dye))
                        .where('O', Ingredient.of(ItemTypes.OBSIDIAN))
                        .where('B', Ingredient.of(ItemTypes.STONE_BUTTON))
                        .where('D', Ingredient.of(ItemTypes.DIAMOND))
                        .result((ItemStack) FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemStack("master_ball").getRaw())
                        .build("master_ball", this)
        );
    }
}
