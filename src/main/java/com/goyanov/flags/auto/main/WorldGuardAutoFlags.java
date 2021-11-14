package com.goyanov.flags.auto.main;

import com.goyanov.flags.auto.events.CallingRegionClaimEvent;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class WorldGuardAutoFlags extends JavaPlugin
{
    private static WorldGuardAutoFlags instance;
    public static WorldGuardAutoFlags inst()
    {
        return instance;
    }

    public static final HashMap<Flag, Object> AUTO_FLAGS = new HashMap<>();

    public void loadPlugin()
    {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (config.exists()) saveDefaultConfig(); // TODO добавить отрицание
        else reloadConfig();

        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();

        // StringFlag, SetFlag, DoubleFlag, UnknownFlag, IntegerFlag, BooleanFlag, RegistryFlag, LocationFlag, BuildFlag

        AUTO_FLAGS.clear();
        for (String line : getConfig().getStringList("auto-flags"))
        {
            String[] args = line.split(":");
            Flag flag = flagRegistry.get(args[0]);
            if (flag instanceof StateFlag)
            {
                StateFlag stateFlag = (StateFlag) flag;
                StateFlag.State value = StateFlag.State.valueOf(args[1].toUpperCase());
                stateFlag.marshal(value);
                AUTO_FLAGS.put(stateFlag, value);
            }
        }
    }

    @Override
    public void onEnable()
    {
        instance = this;

        loadPlugin();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new CallingRegionClaimEvent(), this);
    }
}
