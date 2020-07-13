package me.fusiondev.fusionpixelmon.api.pixelmon;

import com.pixelmonmod.pixelmon.enums.EnumPlate;
import me.fusiondev.fusionpixelmon.FusionPixelmon;
import me.fusiondev.fusionpixelmon.api.colour.DyeColor;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemType;
import me.fusiondev.fusionpixelmon.api.items.AbstractItemTypes;

public class ArcPlates {

    private static AbstractItemTypes itemTypes = FusionPixelmon.getRegistry().getItemTypesRegistry();

    /**
     * The Plate entries.
     * Note: The slot values should be in ascending order.
     */
    public enum Plate {
        DRACO(0, 3, EnumPlate.DRACO, DyeColor.RED),
        DREAD(1, 5, EnumPlate.DREAD, DyeColor.CYAN),
        EARTH(2, 7, EnumPlate.EARTH, DyeColor.ORANGE),
        FIST(3, 11, EnumPlate.FIST, DyeColor.GRAY),
        FLAME(4, 13, EnumPlate.FLAME, DyeColor.PINK),
        ICICLE(5, 15, EnumPlate.ICICLE, DyeColor.SILVER),
        INSECT(6, 17, EnumPlate.INSECT, DyeColor.LIME),
        IRON(7, 21, EnumPlate.IRON, DyeColor.WHITE),
        MEADOW(8, 23, EnumPlate.MEADOW, DyeColor.GREEN),
        MIND(9, 25, EnumPlate.MIND, itemTypes.STAINED_GLASS_PANE()),
        PIXIE(10, 29, EnumPlate.PIXIE, DyeColor.PURPLE),
        SKY(11, 31, EnumPlate.SKY, DyeColor.LIGHT_BLUE),
        SPLASH(12, 33, EnumPlate.SPLASH, DyeColor.BLUE),
        SPOOKY(13, 35, EnumPlate.SPOOKY, DyeColor.BLACK),
        STONE(14, 39, EnumPlate.STONE, DyeColor.BROWN),
        TOXIC(15, 41, EnumPlate.TOXIC, DyeColor.MAGENTA),
        ZAP(16, 43, EnumPlate.ZAP, DyeColor.YELLOW);

        public int i;
        public int slot;
        public EnumPlate plate;
        public DyeColor colour;
        public AbstractItemType type;

        Plate(int i, int slot, EnumPlate plate, DyeColor colour) {
            this.i = i;
            this.slot = slot;
            this.plate = plate;
            this.colour = colour;
            this.type = itemTypes.STAINED_GLASS_PANE();
        }

        Plate(int i, int slot, EnumPlate plate, AbstractItemType type) {
            this.i = i;
            this.slot = slot;
            this.plate = plate;
            this.type = type;
        }
    }
}
