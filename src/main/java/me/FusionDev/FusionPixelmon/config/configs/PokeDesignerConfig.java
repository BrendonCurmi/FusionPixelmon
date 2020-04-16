package me.FusionDev.FusionPixelmon.config.configs;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class PokeDesignerConfig {
    @Inject
    @Setting("blacklisted-pokemon")
    public List<EnumSpecies> blacklistedPokemon = ImmutableList.of();

    /**
     * Checks if the specified species is blacklisted from the PokeDesigner.
     * @param species the species to check.
     * @return true if the species is blacklisted; otherwise false.
     */
    public boolean containsBlackListedPokemon(EnumSpecies species) {
        return blacklistedPokemon.contains(species);
    }

    @ConfigSerializable
    public static class ShopConfig {
        @SuppressWarnings("UnstableApiUsage")
        public final static TypeToken<ShopConfig> TYPE = TypeToken.of(ShopConfig.class);

        @Inject
        @Setting
        private boolean enabled;
        @Inject
        @Setting("prices")
        private HashMap<String, Integer> prices;

        public boolean isEnabled() {
            return enabled;
        }

        public HashMap<String, Integer> getPrices() {
            return prices;
        }
    }

    /**
     * The string name of the loaded shops mapped to their shop configs.
     */
    private HashMap<String, ShopConfig> shops = new HashMap<>();

    /**
     * Checks if the PokeDesigner is enabled.
     *
     * @return true if PokeDesigner is enabled; otherwise false.
     */
    public boolean isEnabled() {
        return !shops.isEmpty();
    }

    /**
     * Checks if the specified shop config is loaded.
     *
     * @param name the shop name.
     * @return true if the shop config is loaded/enabled/exists; otherwise false.
     */
    public boolean existsShop(String name) {
        return shops.containsKey(name);
    }

    /**
     * Gets the config of the specified name.
     *
     * @param name the shop name.
     * @return the shop config if found; otherwise null.
     */
    public ShopConfig getShopNamed(String name) {
        return shops.getOrDefault(name, null);
    }

    /**
     * Loads the PokeDesigner configuration from the config file in the specified loader.
     *
     * @param loader the loader.
     * @throws IOException            if an I/O exception occurs.
     * @throws ObjectMappingException if an object mapping exception occurs.
     */
    @SuppressWarnings("UnstableApiUsage")
    public void loadPokeDesignerConfig(HoconConfigurationLoader loader) throws IOException, ObjectMappingException {
        CommentedConfigurationNode configRoot = loader.load();
        Map<Object, ? extends CommentedConfigurationNode> map = configRoot.getNode("pokedesigner", "shops").getChildrenMap();
        for (Object key : map.keySet()) {
            String name = (String) key;
            ShopConfig shopConfig = configRoot.getNode("pokedesigner", "shops", name).getValue(ShopConfig.TYPE);
            if (shopConfig != null) {
                shops.put(name, shopConfig);
            }
        }
        loader.save(configRoot);
    }
}
