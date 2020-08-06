package org.improt.anticheat.detection;

import org.bukkit.entity.Player;
import org.improt.anticheat.AntiCheat;
import org.improt.anticheat.compatibility.ServerCompat;
import org.improt.anticheat.compatibility.data.player.container.PlayerContainer;
import org.improt.anticheat.compatibility.permission.IPermissions;

import java.util.Arrays;
import java.util.function.Predicate;

public class DetectionCaller {
    private IDetection[] detections;
    private ServerCompat compat;

    /**
     * Initialize and set up all detections of the plugin
     */
    public void setup() {
        // ...
        compat = AntiCheat.getInstance().getServerCompat();
        detections = new IDetection[] {
            // Register Detections...
        };
    }

    /**
     * @return all initialized detection classes
     */
    public IDetection[] getAllDetections() {
        return detections;
    }


    /**
     * Run a check on the passed event context to determine a result
     */
    public void passContext(PlayerContainer playerContainer, Object context) {
        Arrays.stream(detections)
                .filter(this.permissionPredicate(playerContainer.getContainedPlayer()))
                .filter(this.compatibilityPredicate())
                .filter(this.contextRegistrarPredicate(context))
                .forEach(e -> e.check(playerContainer, context));
    }

    private Predicate<? super IDetection> enabledPredicate() {
        return e -> e.active;
    }

    private Predicate<? super IDetection> contextRegistrarPredicate(Object context) {
        return e -> e.isRegistered(context);
    }

    private Predicate<? super IDetection> compatibilityPredicate() {
        return e -> e.compatibleWith(compat.getServerVersion());
    }

    private Predicate<? super IDetection> permissionPredicate(Player player) {
        return e -> compat.getPermissions().hasPermission(player,IPermissions.EXEMPT + "." + e.getDetectionName());
    }
}
