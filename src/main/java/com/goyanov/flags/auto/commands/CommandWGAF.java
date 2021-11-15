package com.goyanov.flags.auto.commands;

import com.goyanov.flags.auto.main.WorldGuardAutoFlags;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandWGAF implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload"))
        {
            WorldGuardAutoFlags.inst().loadPlugin();
            sender.sendMessage("§8§l| §aPlugin reloaded :)");
        }
        else
        {
            sender.sendMessage("§c/wgaf reload - reload the config");
        }
        return true;
    }
}
