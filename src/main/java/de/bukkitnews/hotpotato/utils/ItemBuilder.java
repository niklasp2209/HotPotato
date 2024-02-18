package de.bukkitnews.hotpotato.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material){
        this.itemStack = new ItemStack(material);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayname(String name){
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder setLore(String... lore){
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemStack build(){
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
