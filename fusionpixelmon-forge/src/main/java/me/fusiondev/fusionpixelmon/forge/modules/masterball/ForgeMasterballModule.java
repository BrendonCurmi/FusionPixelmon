package me.fusiondev.fusionpixelmon.forge.modules.masterball;

import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ForgeMasterballModule {
    public ForgeMasterballModule() {
        ItemStack dye = new ItemStack(Items.DYE);
        dye.setItemDamage(DyeColor.PURPLE.getDyeData());

        GameRegistry.addShapedRecipe(new ResourceLocation("pixelmon:master_ball"),
                new ResourceLocation(FusionPixelmon.getInstance().getId()),
                (ItemStack) FusionPixelmon.getRegistry().getPixelmonUtils().getPixelmonItemType("master_ball").to().getRaw(),
                "PPP",
                "OBO",
                "DDD",
                'P', dye,
                'O', Blocks.OBSIDIAN,
                'B', Blocks.STONE_BUTTON,
                'D', Items.DIAMOND);
    }
}
