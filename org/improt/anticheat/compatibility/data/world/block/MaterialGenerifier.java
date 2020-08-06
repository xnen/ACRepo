package org.improt.anticheat.compatibility.data.world.block;

import org.bukkit.Material;
import org.improt.anticheat.compatibility.data.world.block.type.BasicMaterial;

import java.util.HashMap;
import java.util.Map;

public abstract class MaterialGenerifier {
    private Map<Material, BasicMaterial[]> materialMap = new HashMap<>();
    public abstract void initMaterials();

    public void add(Material m, BasicMaterial... b) {
        this.materialMap.put(m, b);
    }

    public BasicMaterial[] lookup(Material m) {
        BasicMaterial[] basicMaterial = this.materialMap.get(m);
        return basicMaterial == null ? new BasicMaterial[] { BasicMaterial.SOLID } : basicMaterial;
    }
}
