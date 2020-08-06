package org.improt.anticheat;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.improt.anticheat.compatibility.ServerCompat;
import org.improt.anticheat.compatibility.permission.IPermissions;
import org.improt.anticheat.detection.DetectionCaller;
import org.improt.anticheat.punishment.PunishmentHandler;
import org.improt.anticheat.settings.Settings;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AntiCheat {
    // Maximum network delay a player may have before they time out from the server (Used for lag adjustments)
    public static final long NETTY_TIMEOUT_DELAY = 30000L;

    private PunishmentHandler punishmentHandler;
    private DetectionCaller detectionCaller;
    private IPermissions permissions;
    private PluginProxy pluginProxy;
    private ServerCompat compat;
    private Settings settings;

    private AntiCheat(final PluginProxy proxy) {
        this.pluginProxy = proxy;
    }

    private void init() {
        getLogger().log(Level.INFO, "Initializing " + this.pluginProxy.getName() + "...");

        try {
            getLogger().log(Level.INFO, "Loading configuration...");
            settings = new Settings();
            settings.loadSettings();

            getLogger().log(Level.INFO, "Adapting to server version...");
            compat = ServerCompat.Factory.createCompat(this);

            getLogger().log(Level.INFO, "Setting up detections...");
            detectionCaller = new DetectionCaller();
            detectionCaller.setup();

            getLogger().log(Level.INFO, "Loading player punishments...");
            punishmentHandler = new PunishmentHandler();
            punishmentHandler.init(this.settings);

        } catch (Exception e) {
            this.pluginProxy.disable(e.getMessage());
        }
    }

    public PunishmentHandler getPunishments() { return punishmentHandler; }
    public DetectionCaller getDetectionCaller() {
        return detectionCaller;
    }
    public ServerCompat getServerCompat() {
        return compat;
    }
    public IPermissions getPermissions() {
        return permissions;
    }
    public PluginProxy getPluginProxy() {
        return pluginProxy;
    }
    public Settings getSettings() { return this.settings; }

    /**
     * Handle commands sent by the user
     */
    boolean pushCommand(CommandSender sender, Command command, String label, String[] args) {
        // ...
        return false;
    }

    /**
     * Destroy instance of the plugin (Saving... Final modifications... etc)
     */
    void destroy() {
        // ...
    }

    /** Plugin Proxy's Logger Instance */
    private Logger getLogger() {
        return this.pluginProxy.getLogger();
    }

    public static AntiCheat getInstance() {
        return instance;
    }

    /**
     * Create a new instance of the AntiCheat. If an instance existed previously, it will be destroyed and a new one will be returned.
     */
    static AntiCheat createInstance(PluginProxy proxy) {
        if (instance != null) instance.destroy();

        instance = new AntiCheat(proxy);
        instance.init();

        return instance;
    }

    private static AntiCheat instance;
}
