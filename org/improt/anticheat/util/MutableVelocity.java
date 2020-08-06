package org.improt.anticheat.util;

public class MutableVelocity {

    private final long time;
    private boolean signX, signY, signZ;
    private double x, y, z;

    MutableVelocity(double x, double y, double z, long time) {
        this.signX = x >= 0;
        this.signY = y >= 0;
        this.signZ = z >= 0;

        this.x = Math.abs(x);
        this.y = Math.abs(y);
        this.z = Math.abs(z);

        this.time = time;
    }

    /**
     * @return when the velocity was created
     */
    public long getTime() {
        return time;
    }

    /**
     * Test a velocity to check if it is applicable to this velocity container
     */
    boolean isApplicable(double x, double y, double z) {
        boolean flag0 = (x >= 0 && this.signX) || (x <= 0 && !this.signX);
        boolean flag1 = (y >= 0 && this.signY) || (y <= 0 && !this.signY);
        boolean flag2 = (z >= 0 && this.signZ) || (z <= 0 && !this.signZ);

        return flag0 && flag1 && flag2;
    }

    /**
     * Consume the velocity value down to zero.
     * @return the excess amount if it reaches zero before the amount was consumed
     */
    public double[] consume(double x, double y, double z) {
        final double prevX = x;
        final double prevY = y;
        final double prevZ = z;

        x -= Math.min(x, this.x);
        y -= Math.min(y, this.y);
        z -= Math.min(z, this.z);

        this.x -= prevX - x;
        this.y -= prevY - y;
        this.z -= prevZ - z;

        return new double[] { x, y, z };
    }
}
