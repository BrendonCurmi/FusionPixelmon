package me.fusiondev.fusionpixelmon.spigot.modules.masterball;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class SpigotMasterballModule {
    public SpigotMasterballModule(Plugin plugin) {
        ItemStack result = (ItemStack) FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType("master_ball").to().getRaw();
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "master_ball"), result);

        ItemStack dye = new ItemStack(Material.INK_SACK);
        dye.setDurability(DyeColor.PURPLE.getDyeData());

        recipe.shape("PPP", "OBO", "DDD");
        recipe.setIngredient('P', dye.getData());
        recipe.setIngredient('O', Material.OBSIDIAN);
        recipe.setIngredient('B', Material.STONE_BUTTON);
        recipe.setIngredient('D', Material.DIAMOND);

        Bukkit.addRecipe(recipe);
    }
}
