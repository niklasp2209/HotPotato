package de.bukkitnews.hotpotato.achievement;


import org.bukkit.Material;

public enum Achievement {

    FIRST_WIN("DE", Material.GRAY_DYE),
    FIRST_DEATH("DE", Material.GRAY_DYE),
    FAIRPLAY("DE", Material.GRAY_DYE);

    private String displayname;
    private Material material;

    Achievement(String displaynameDE, Material material){
        this.displayname = displaynameDE;
        this.material = material;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getDisplayname() {
        return displayname;
    }
}
