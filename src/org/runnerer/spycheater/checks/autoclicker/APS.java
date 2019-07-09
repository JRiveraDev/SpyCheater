package org.runnerer.spycheater.checks.autoclicker;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilTime;
import org.runnerer.spycheater.events.PacketUseEntityEvent;

import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class APS
        extends Check
{

    private int maxCPS = 23;
    private Map<UUID, Map.Entry<Integer, Long>> clickTimes = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public APS(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.AUTOCLICKER, "APSClicker", "AutoClicker", 1, 50, 6, 0);
    }

    @EventHandler
    public void useEntity(PacketUseEntityEvent packetUseEntityEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        if (packetUseEntityEvent.getAction() != EnumWrappers.EntityUseAction.ATTACK)
        {
            return;
        }
        if (!(packetUseEntityEvent.getAttacked() instanceof Player))
        {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        int n3 = 0;
        long l = System.currentTimeMillis();
        if (this.clickTimes.containsKey(player.getUniqueId()))
        {
            n3 = this.clickTimes.get(player.getUniqueId()).getKey();
            l = this.clickTimes.get(player.getUniqueId()).getValue();
        }
        ++n3;
        if (this.clickTimes.containsKey(player.getUniqueId()) && UtilTime.elapsed(l, 1000L))
        {
            if (n3 > this.maxCPS)
            {
                ++n;
                if (n3 > this.maxCPS * 5)
                {
                    ++n;
                }
            } else
            {
                --n;
            }
            if (n > n2)
            {
                if (n3 > this.maxCPS * 6)
                {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGH, String.valueOf(n3) + " APS"));
                } else if (n3 > this.maxCPS * 3)
                {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.valueOf(n3) + " APS"));
                } else
                {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(n3) + " APS"));
                }
                n = 0;
            }
            n3 = 0;
            l = UtilTime.nowlong();
        }
        playerStats.setCheck(this, 0, n);
        this.clickTimes.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(n3, l));
    }
}

