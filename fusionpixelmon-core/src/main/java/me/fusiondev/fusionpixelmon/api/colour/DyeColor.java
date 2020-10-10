package me.fusiondev.fusionpixelmon.api.color;

public enum DyeColor {
    WHITE(15, 0),
    ORANGE(14,1),
    MAGENTA(13,2),
    LIGHT_BLUE(12,3),
    YELLOW(11,4),
    LIME(10,5),
    PINK(9,6),
    GRAY(8,7),
    SILVER(7,8),
    CYAN(6,9),
    PURPLE(5,10),
    BLUE(4,11),
    BROWN(3,12),
    GREEN(2,13),
    RED(1,14),
    BLACK(0,15);

    // dyes, banner
    private byte dyeData;
    // stained glass, stained glass pane, stained hardened clay, concrete, concrete powder,carpet, bed,wool
    private byte blockData;

    DyeColor(int dyeData, int blockData) {
        this.dyeData = (byte) dyeData;
        this.blockData = (byte) blockData;
    }

    public byte getDyeData() {
        return dyeData;
    }

    public byte getBlockData() {
        return blockData;
    }
}
