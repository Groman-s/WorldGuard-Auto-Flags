package com.goyanov.flags.auto.events;

import com.goyanov.flags.auto.events.custom.RegionClaimEvent;
import com.goyanov.flags.auto.main.WorldGuardAutoFlags;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Map;

public class CallingRegionClaimEvent implements Listener
{
    @EventHandler
    public void onClaim(final PlayerCommandPreprocessEvent e)
    {
        String cmd = e.getMessage(); // ~/rg claim test - Example
        if (!cmd.startsWith("/rg claim ") && !cmd.startsWith("/region claim ")) return; // accept only ~/region claim, ~/rg claim
        String[] cmdParts = cmd.split(" "); // [/rg, claim, test]
        final Player p = e.getPlayer(); // who sent the command
        final String regionName = cmdParts[2]; // ~/rg claim ->[test]<-
        World playerWorld = BukkitAdapter.adapt(p.getWorld()); // where is a player selection TODO заменить на выделенный игроком регион
        final RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(playerWorld); // region manager of the world
        if (regionManager != null && regionManager.getRegion(regionName) != null) return; // if the region exists - return
        Bukkit.getScheduler().scheduleSyncDelayedTask(WorldGuardAutoFlags.inst(), () ->
        {
            if (regionManager == null) return; // if null return
            ProtectedRegion region = regionManager.getRegion(regionName); // region by name
            Bukkit.getPluginManager().callEvent(new RegionClaimEvent(p, region)); // call event
            p.sendMessage("Регион захвачен. Его имя " + region.getId()); // TODO убрать
            for (Map.Entry<Flag, Object> entry : WorldGuardAutoFlags.AUTO_FLAGS.entrySet())
            {
                region.setFlag(entry.getKey(), entry.getValue()); // set the flag
            }
        }, 20);
    }
}
