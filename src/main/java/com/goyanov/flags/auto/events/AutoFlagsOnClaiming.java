package com.goyanov.flags.auto.events;

import com.goyanov.flags.auto.events.custom.RegionClaimEvent;
import com.goyanov.flags.auto.main.WorldGuardAutoFlags;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StringFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class AutoFlagsOnClaiming implements Listener
{
    @EventHandler
    public void setAutoFlags(RegionClaimEvent e)
    {
        Player p = e.getPlayer();
        ProtectedRegion region = e.getRegion();
        if (WorldGuardAutoFlags.inst().getConfig().getBoolean("notify.enabled"))
        {
            p.sendMessage(WorldGuardAutoFlags.inst().getConfig().getString("notify.message").replace("&", "§").replace("%p", p.getName()).replace("%n", region.getId()));
        }
        for (Map.Entry<Flag, Object> entry : WorldGuardAutoFlags.AUTO_FLAGS.entrySet())
        {
            Flag flag = entry.getKey();
            Object value = entry.getValue();
            if (flag instanceof StringFlag)
            {
                StringFlag stringFlag = (StringFlag) flag;
                String val = value.toString().replace("%p", p.getName()).replace("%n", region.getId());
                stringFlag.marshal(val);
                value = val;
            }
            region.setFlag(flag, value); // set the flag
        }
    }
}
