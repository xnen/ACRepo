package com.github.xnen.anticheat.compatibility.data.world.block.type;

public enum BasicMaterial {
      CLIMBABLE(16531L)
    , SLOW_HORIZONTAL(41927L)
    , SLOW_VERTICAL(41927L)
    , SLIPPERY(74636L)
    , BUBBLE_COLUMN(97086L)
    , UNCOLLIDABLE(55903L)
    , SOLID(5903L)
    , WATER(11000L)
    , LAVA(14121L)
    , UNKNOWN(-1)
    ;

    final long uuid;

    BasicMaterial(long uuid) {
          this.uuid = uuid;
    }

    public long getUuid() {
        return uuid;
    }
}