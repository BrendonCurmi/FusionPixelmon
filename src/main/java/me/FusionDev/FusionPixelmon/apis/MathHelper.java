package me.FusionDev.FusionPixelmon.apis;

public class MathHelper {

    /**
     * Clamps down the specified num to be within the
     * range of the specified min to the specified max.
     * In other words, ensures min is the smallest number
     * num can be, and max is the largest number num can be.
     *
     * @param num the number to clamp.
     * @param min the minimum value.
     * @param max the maximum value.
     * @return the num within the range of min - max, both inclusive.
     */
    public static int clamp(int num, int min, int max) {
        return (num < min) ? min : Math.min(num, max);// Also works Math.max(min, Math.min(max, num));
    }
}
