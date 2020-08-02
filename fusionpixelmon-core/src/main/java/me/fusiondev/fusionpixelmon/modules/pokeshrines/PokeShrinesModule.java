package me.fusiondev.fusionpixelmon.modules.pokeshrines;

import me.fusiondev.fusionpixelmon.FusionPixelmon;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Allows the "breaking" and obtaining of the shrines, altars, and chalices that naturally
 * spawn throughout the world of Pixelmon, since they are made to be unbreakable.<br/>
 * If the player interacts with one of these blocks while holding any of the allowed
 * tools whilst in Survival Mode (and with inventory space), then the block will
 * drop into the player's inventory.
 */
public abstract class PokeShrinesModule {

    /**
     * The blocks which can be obtained using this method.
     */
    protected static final HashMap<String, String> BLOCKS = FusionPixelmon.getInstance().getConfiguration().getPickableShrines();

    /**
     * The list of tools for each tool type that are allowed to validate the interaction.
     */
    private static final HashMap<String, List<String>> ALLOWED_TOOLS = new HashMap<>();

    public PokeShrinesModule() {
        if (ALLOWED_TOOLS.isEmpty()) {
            //Using Collections.singletonList(") instead of Arrays.asList() since only one entry
            ALLOWED_TOOLS.put("pickaxe", Collections.singletonList("diamond_pickaxe"));
            ALLOWED_TOOLS.put("axe", Collections.singletonList("diamond_axe"));
        }
    }

    /**
     * Checks if the specified tool is an allowed tool within the specified tool type.
     *
     * @param type the type of the tool.
     * @param tool the tool.
     * @return true if and only if the tool type exists and the tool is of that type.
     */
    protected boolean isAllowedTool(String type, String tool) {
        tool = tool.toLowerCase().replace("minecraft:", "");
        return ALLOWED_TOOLS.containsKey(type) && ALLOWED_TOOLS.get(type).contains(tool);
    }
}
