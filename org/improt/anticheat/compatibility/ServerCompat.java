package org.improt.anticheat.compatibility;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.event.Event;
import org.improt.anticheat.AntiCheat;
import org.improt.anticheat.compatibility.data.IEventCollector;
import org.improt.anticheat.compatibility.data.IPacketCollector;
import org.improt.anticheat.compatibility.data.player.handler.PlayerCollector;
import org.improt.anticheat.compatibility.data.player.handler.PlayerCollector1151;
import org.improt.anticheat.compatibility.data.world.WorldCollectorVer1151;
import org.improt.anticheat.compatibility.data.world.block.Generifier1151;
import org.improt.anticheat.compatibility.data.world.block.MaterialGenerifier;
import org.improt.anticheat.compatibility.event.EventsVer1151;
import org.improt.anticheat.compatibility.event.IEvents;
import org.improt.anticheat.compatibility.methodwrapper.IMethodWrapper;
import org.improt.anticheat.compatibility.methodwrapper.MethodWrapper1151;
import org.improt.anticheat.compatibility.permission.IPermissions;
import org.improt.anticheat.compatibility.permission.PermissionsVer1151;

import java.util.Arrays;

public class ServerCompat {
    private Version version;

    private IEventCollector[] dataCollectors; // Collection of all event collectors (PLAYER, WORLD)
    private IEventCollector worldCollector;
    private PlayerCollector playerCollector;
    private MaterialGenerifier generifier;

    private IMethodWrapper methodWrapper;
    private IPermissions permissions;
    private IEvents eventBus;

    /**
     * Return a guaranteed permission handler for the current version of the server
     */
    public IPermissions getPermissions() {
        return permissions;
    }

    /**
     * Pass a Bukkit-Related event to the ServerCompat to pass to necessary data classes
     */
    public void passBukkitEvent(Event event) {
        Arrays.stream(this.dataCollectors).forEach(collector -> collector.onEvent(event));
    }

    public IPacketCollector getPlayerDataCollector() {
        return this.playerCollector;
    }
    public IEventCollector getWorldDataCollector() {
        return this.worldCollector;
    }

    public MaterialGenerifier getMaterialGenerifier() {
        return this.generifier;
    }

    public IMethodWrapper getMethodWrapper() {
        return this.methodWrapper;
    }
    public Version getServerVersion() {
        return this.version;
    }

    private void setVersion(Version detectedVersion) {
        this.version = detectedVersion;
    }

    /**
     * Detect server version based on package structure and class existence
     */
    private static Version detectServerVersion() {
        for (Version version : Version.values()) {
            String testClass = version.getTestClass();
            if (testClass == null) continue;

            try { // Test for a class that exists for that version
                Class.forName(version.getTestClass());
            } catch (ClassNotFoundException e) {
                continue;
            }

            // Return the result, there was no ClassNotFoundException
            return version;
        }

        return Version.UNKNOWN;
    }

    // Uncomment As Supported
    public enum Version {
         UNKNOWN(-1, null, null)
//        ,Version1710(0, null, "1.7.10")
//        ,Version188(1, null, "1.8.X")
//        ,Version190(2, null, "1.9.0")
//        ,Version191(3, null, "1.9.1")
//        ,Version193(4, null, "1.9.3")
//        ,Version1100(5, null, "1.10.0")
//        ,Version1110(6, null, "1.11.0")
//        ,Version1111(7, null, "1.11.1")
//        ,Version1120(8, null, "1.12.0")
//        ,Version1121(9, null, "1.12.1")
//        ,Version1122(10, null, "1.12.2")
//        ,Version1130(11, null, "1.13.0")
//        ,Version1131(12, null, "1.13.1")
//        ,Version1132(13, null, "1.13.2")
//        ,Version1140(14, null, "1.14.0")
//        ,Version1141(15, null, "1.14.1")
//        ,Version1142(16, null, "1.14.2")
//        ,Version1143(17, null, "1.14.3")
//        ,Version1144(18, null, "1.14.4")
//        ,Version1150(19, null, "1.15.0")
        ,Version1151(20, "net.minecraft.server.v1_15_R1.IMinecraftServer", "1.15.1")
//        ,Version1152(21, null, "1.15.2")
//        ,Version1160(22, null, "1.16.0")
        ;

        final int version;
        final String testClass;
        final String name;

        Version(int version, String testClass, String name) {
            this.version = version;
            this.testClass = testClass;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String getTestClass() {
            return testClass;
        }

        public int getVersionID() {
            return version;
        }
    }

    public static class Factory {
        /**
         * Creates an instance of ServerCompat based on server's version (as detected in {@See detectServerVersion})
         * @return
         * @throws Exception
         */
        public static ServerCompat createCompat(AntiCheat antiCheat) throws Exception {
            ServerCompat compat = new ServerCompat();
            Version detectedVersion = detectServerVersion();
            switch (detectedVersion) {
                case Version1151:
                    compat.methodWrapper = new MethodWrapper1151();
                    compat.worldCollector = new WorldCollectorVer1151();
                    compat.playerCollector = new PlayerCollector1151();

                    compat.generifier = new Generifier1151();
                    compat.generifier.initMaterials();

                    compat.dataCollectors = new IEventCollector[] { compat.playerCollector, compat.worldCollector };

                    compat.permissions = new PermissionsVer1151();
                    compat.eventBus = new EventsVer1151();
                    compat.eventBus.register(antiCheat);//, ProtocolLibrary.getProtocolManager());

                    compat.setVersion(detectedVersion);
                    return compat;
                default:
                    Version[] versions = Version.values();
                    throw new Exception("Unsupported server version! This plugin supports bukkit " + versions[1].getName() + " - " + versions[versions.length - 1].getName());
            }
        }
    }
}
