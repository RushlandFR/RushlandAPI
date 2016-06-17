package fr.rushland.api.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.rushland.api.RushlandAPI;

public class Inventories {

    @SuppressWarnings("unused")
    private RushlandAPI api;

    public Inventories(RushlandAPI api) {
        this.api = api;
    }

    public ItemStack createItem(Material material, String displayname, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public Inventory createInv(Player owner, int size, String name) {
        return Bukkit.createInventory(owner, size, name);
    }

    public Inventory createInv(int size, String name) {
        return Bukkit.createInventory(null, size, name);
    }

    public void clearInv(Player player) {
        player.getInventory().clear();
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
    }
}
