package com.github.xnen.anticheat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PluginProxy extends JavaPlugin {
    private AntiCheat anticheatInstance;

    @java.lang.Override
    public void onEnable() {
        this.anticheatInstance = AntiCheat.createInstance(this);
    }

    @java.lang.Override
    public void onDisable() {
        getLogger().info("Shutting down " + this.getName() + "...");

        if (this.anticheatInstance != null) {
            this.anticheatInstance.destroy();
        }
    }

    @java.lang.Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return this.anticheatInstance.pushCommand(sender, command, label, args);
    }

    void disable(String reason) {
        getLogger().info("Disabling " + this.getName() + ": " + reason);
        this.setEnabled(false);
    }
}
