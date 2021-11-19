package com.goyanov.flags.auto.main;

import com.goyanov.flags.auto.commands.CommandWGAF;
import com.goyanov.flags.auto.events.AutoFlagsOnClaiming;
import com.goyanov.flags.auto.events.CallingRegionClaimEvent;
import com.goyanov.flags.auto.utils.AutoPluginUpdater;
import com.goyanov.flags.auto.utils.Metrics;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.*;
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
        if (!config.exists()) saveDefaultConfig();
        else reloadConfig();

        AutoPluginUpdater.update();

        FlagRegistry flagRegistry = WorldGuard.getInstance().getFlagRegistry();

        AUTO_FLAGS.clear();
        for (String line : getConfig().getStringList("auto-flags"))
        {
            String[] args = line.split(":");
            Flag flag = flagRegistry.get(args[0]);
            Object val = null;

            if (flag instanceof StateFlag)
            {
                StateFlag stateFlag = (StateFlag) flag;
                StateFlag.State value = StateFlag.State.valueOf(args[1].toUpperCase());
                stateFlag.marshal(value);
                val = value;
            }
            else if (flag instanceof StringFlag)
            {
                StringFlag stringFlag = (StringFlag) flag;
                args[1] = args[1].replace("&", "§");
                stringFlag.marshal(args[1]);
                val = args[1];
            }
            else if (flag instanceof IntegerFlag)
            {
                IntegerFlag integerFlag = (IntegerFlag) flag;
                Integer value = Integer.parseInt(args[1]);
                integerFlag.marshal(value);
                val = value;
            }
            else if (flag instanceof DoubleFlag)
            {
                DoubleFlag doubleFlag = (DoubleFlag) flag;
                Double value = Double.parseDouble(args[1]);
                doubleFlag.marshal(value);
                val = value;
            }
            else if (flag instanceof BooleanFlag)
            {
                BooleanFlag booleanFlag = (BooleanFlag) flag;
                Boolean value = Boolean.parseBoolean(args[1]);
                booleanFlag.marshal(value);
                val = value;
            }

            AUTO_FLAGS.put(flag, val);
        }
    }

    @Override
    public void onEnable()
    {
        instance = this;

        loadPlugin();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new AutoFlagsOnClaiming(), this);
        pm.registerEvents(new CallingRegionClaimEvent(), this);

        getCommand("worldguardautoflags").setExecutor(new CommandWGAF());

        new Metrics(this, 13333);
    }
}
