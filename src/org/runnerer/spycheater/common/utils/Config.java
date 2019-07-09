package org.runnerer.spycheater.common.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config
{

    private FileConfiguration Config;
    private File File;
    private String Name;

    public Config(JavaPlugin javaPlugin, String string, String string2)
    {
        this.File = new File(javaPlugin.getDataFolder() + string);
        this.File.mkdirs();
        this.File = new File(javaPlugin.getDataFolder() + string, String.valueOf(String.valueOf(string2)) + ".yml");
        try
        {
            this.File.createNewFile();
        }
        catch (IOException iOException)
        {
            // empty catch block
        }
        this.Name = string2;
        this.Config = YamlConfiguration.loadConfiguration((File) this.File);
    }

    public String getName()
    {
        return this.Name;
    }

    public FileConfiguration getConfig()
    {
        return this.Config;
    }

    public void setDefault(String string, Object object)
    {
        if (this.getConfig().contains(string)) return;
        this.Config.set(string, object);
        this.save();
    }

    public void save()
    {
        try
        {
            this.Config.save(this.File);
            return;
        }
        catch (IOException iOException)
        {
            // empty catch block
        }
    }

    public void reload()
    {
        this.Config = YamlConfiguration.loadConfiguration((File) this.File);
    }
}

