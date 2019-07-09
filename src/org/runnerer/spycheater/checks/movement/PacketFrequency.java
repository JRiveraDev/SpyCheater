package org.runnerer.spycheater.checks.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.common.utils.UtilTime;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketFrequency
        extends Check
{

    private Map<UUID, Map.Entry<Long, Integer>> MS = new HashMap<UUID, Map.Entry<Long, Integer>>();
    private int maxPackets = 22;
    private long time = 1000L;

    public PacketFrequency(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "PacketFrequency", "Packet Frequency", 30, 50, 2, 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        UUID uUID = playerQuitEvent.getPlayer().getUniqueId();
        if (!this.MS.containsKey(uUID)) return;
        this.MS.remove(uUID);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void Move(PlayerMoveEvent playerMoveEvent)
    {
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (!this.isEnabled())
        {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        int n3 = 0;
        long l = System.currentTimeMillis();
        if (this.MS.containsKey(player.getUniqueId()))
        {
            n3 = this.MS.get(player.getUniqueId()).getValue();
            l = this.MS.get(player.getUniqueId()).getKey();
        }
        ++n3;
        if (this.MS.containsKey(player.getUniqueId()) && UtilTime.elapsed(l, this.time))
        {
            int n4 = this.maxPackets;
            if (UtilServer.getPing(player) > 400)
            {
                n4 *= 1;
            }
            if (n3 > n4)
            {
                n += 5;
                if ((double) n3 > (double) n4 * 1.3)
                {
                    n += 3;
                }
            } else
            {
                n -= 4;
            }
            if (n > n2)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "experimental"));
                n = 0;
            }
            n3 = 0;
            l = UtilTime.nowlong();
        }
        playerStats.setCheck(this, 0, n);
        this.MS.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Integer>(l, n3));
    }
}

