package com.goyanov.flags.auto.events.custom;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RegionClaimEvent extends Event
{
    ///====================================================================

    private Player player;
    private ProtectedRegion region;

    ///====================================================================

    public RegionClaimEvent(Player player, ProtectedRegion region)
    {
        this.player = player;
        this.region = region;
    }

    ///====================================================================

    public Player getPlayer()
    {
        return player;
    }
    public ProtectedRegion getRegion()
    {
        return region;
    }

    ///====================================================================

    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    public HandlerList getHandlers()
    {
        return handlers;
    }

    ///====================================================================
}
