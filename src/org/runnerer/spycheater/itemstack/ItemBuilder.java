package org.runnerer.spycheater.itemstack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder
{

    private Material type;
    private String name;
    private String[] lore;
    private int amount;
    private short data;
    private boolean unbreakable;

    public ItemBuilder(Material material, String string, String... arrstring)
    {
        this.type = material;
        this.name = string;
        this.lore = arrstring;
        this.amount = 1;
        this.data = 0;
    }

    public ItemBuilder(Material material, String string, boolean bl, String... arrstring)
    {
        this.type = material;
        this.name = string;
        this.lore = arrstring;
        this.amount = 1;
        this.unbreakable = bl;
        this.data = 0;
    }

    public ItemBuilder(Material material, String string, int n, String... arrstring)
    {
        this.type = material;
        this.name = string;
        this.lore = arrstring;
        this.amount = n;
        this.data = 0;
    }

    public ItemBuilder(Material material, String string, int n, short s, String... arrstring)
    {
        this.type = material;
        this.name = string;
        this.lore = arrstring;
        this.amount = n;
        this.data = s;
    }

    public ItemBuilder(Material material, String string, int n, short s, List<String> list)
    {
        this.type = material;
        this.name = string;
        this.lore = list.toArray(new String[list.size()]);
        this.amount = n;
        this.data = s;
    }

    public ItemBuilder(ItemStack itemStack, String... arrstring)
    {
        this.type = itemStack.getType();
        this.name = itemStack.getItemMeta().getDisplayName();
        this.lore = arrstring;
        this.amount = itemStack.getAmount();
        this.data = itemStack.getDurability();
    }

    public ItemStack getItem()
    {

        ArrayList<String> arrayList = new ArrayList<String>();
        for (String string23 : this.lore)
        {
            arrayList.add(ChatColor.translateAlternateColorCodes((char) '&', (String) string23));
        }
        ItemStack string2 = new ItemStack(this.type, this.amount, this.data);
        ItemMeta itemMeta = string2.getItemMeta();
        itemMeta.setDisplayName(this.name);
        itemMeta.setLore(arrayList);
        itemMeta.spigot().setUnbreakable(this.unbreakable);
        string2.setItemMeta(itemMeta);
        return string2;
    }
}

