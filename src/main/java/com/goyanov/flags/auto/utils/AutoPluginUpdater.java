package com.goyanov.flags.auto.utils;

import com.goyanov.flags.auto.main.WorldGuardAutoFlags;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AutoPluginUpdater
{
    public static void update()
    {
        File configFile = new File(WorldGuardAutoFlags.inst().getDataFolder() + File.separator + "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        boolean configChanged = false;

        if (!config.contains("use-permissions"))
        {
            config.set("use-permissions", false);
            configChanged = true;
        }

        if (configChanged)
        {
            try {
                config.save(configFile);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }
}
