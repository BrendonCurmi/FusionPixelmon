package me.fusiondev.fusionpixelmon.impl;

public class GrammarUtils {

    /**
     * Capitalizes the first letter of the string, and replaces the
     * underscores with spaces, as {@link #underscoreToSpace(String)}.
     * Example:<br/>
     * <pre>
     *     cap("string") = String
     *     cap("this_string") = This string
     * </pre>
     *
     * @param s the string.
     * @return the string with the first letter capitalized, and underscores
     * replaced with spaces.
     */
    public static String cap(String s) {
        return s.substring(0, 1).toUpperCase() + underscoreToSpace(s.substring(1).toLowerCase());
    }

    /**
     * Replaces the underscore characters in the specified string
     * with a space. Converting snake case to words.
     * Example:<br/>
     * <pre>
     *     underscoreToSpace("this_string") = this string
     * </pre>
     *
     * @param s the string.
     * @return the string with every underscore replaced with a space.
     */
    public static String underscoreToSpace(String s) {
        return s.replace("_", " ");
    }
}
