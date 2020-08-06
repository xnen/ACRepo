package org.improt.anticheat.util;

import java.util.ArrayList;
import java.util.List;

import static org.improt.anticheat.AntiCheat.NETTY_TIMEOUT_DELAY;

public class VelocityCollector {
    /**
     * Tracks applied server-side player velocity across a span of time to compensate for delayed client reactions to the velocity
     */
    public VelocityCollector() {}

    private List<MutableVelocity> velocityList = new ArrayList<>();

    public void addVelocity(double x, double y, double z) {
        // Grace velocity
        x *= 1.25;
        y *= 1.25;
        z *= 1.25;

        System.out.println("Added velocity (" + x + ", " + y + ", " + z + ")");
        long now = System.currentTimeMillis();
        this.velocityList.add(new MutableVelocity(x, y, z, now));

        this.velocityList.removeIf(velocity -> now - velocity.getTime() >= NETTY_TIMEOUT_DELAY);
    }

    public double[] consumeVelocity(double x, double y, double z) {
        List<MutableVelocity> consumed = new ArrayList<>();

        for (MutableVelocity velocity : this.velocityList) {
            if (velocity.isApplicable(x, y, z)) {
                // Consume and update X,Y,Z with excess
                double[] d = velocity.consume(x, y, z);

                x = d[0];
                y = d[1];
                z = d[2];

                // Velocity was fully consumed, flag for removal
                if (x > 0 && y > 0 && z > 0) {
                    consumed.add(velocity);
                }
            }
        }

        for (MutableVelocity velocity : consumed) {
            this.velocityList.remove(velocity);
        }

        return new double[] { x, y, z };
    }


}
