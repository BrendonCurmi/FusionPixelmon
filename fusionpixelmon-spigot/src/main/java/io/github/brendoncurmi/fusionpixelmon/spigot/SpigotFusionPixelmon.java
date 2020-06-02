package io.github.brendoncurmi.fusionpixelmon.spigot;

import io.github.brendoncurmi.fusionpixelmon.spigot.impl.inventory.SpigotInvInventory;
import io.github.brendoncurmi.fusionpixelmon.spigot.modules.pokedesigner.commands.PokeDesignerCommand;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class SpigotFusionPixelmon extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("STARTED");
        get("pd", new PokeDesignerCommand());


        getServer().getPluginManager().registerEvents(new SpigotInvInventory(), this);
    }

    private void get(String name, CommandExecutor e) {
        Optional<PluginCommand> pluginCommand = Optional.ofNullable(this.getCommand(name));
        pluginCommand.ifPresent(cmd -> cmd.setExecutor(e));
    }

    @Override
    public void onDisable() {
        System.out.println("ENDED");
    }
}
