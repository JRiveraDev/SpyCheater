package org.runnerer.spycheater.checks.killaura.pattern;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.events.PacketUseEntityEvent;

import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PacketPattern
        extends Check
{

    private Map<UUID, Long> lastPacketSend = new HashMap<UUID, Long>();
    private double maxPacketRange = 3.4;

    public PacketPattern(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "PacketPattern", "KillAura", 110, 50, 16, 0);
    }

    public void onPacketSend(PacketUseEntityEvent packetUseEntityEvent)
    {
        if (packetUseEntityEvent.getAction() != EnumWrappers.EntityUseAction.ATTACK)
        {
            return;
        }
        if (!(packetUseEntityEvent.getAttacker() instanceof Player))
        {
            return;
        }
        if (!(packetUseEntityEvent.getAttacked() instanceof Player))
        {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        Player player2 = (Player) packetUseEntityEvent.getAttacked();
        double d = player.getLocation().distance(player.getLocation());
        long l = 701L;
        if (this.lastPacketSend.containsKey(player.getUniqueId()))
        {
            l = System.currentTimeMillis() - this.lastPacketSend.get(player.getUniqueId());
        }
        if (l <= 700L) return;
        int n = UtilServer.getPing(player);
        int n2 = 0;
        if (n > 400)
        {
            return;
        }
        if (n > 300)
        {
            this.maxPacketRange *= 1.6;
        } else if (n > 250)
        {
            this.maxPacketRange *= 2.0;
        } else if (n > 200)
        {
            this.maxPacketRange *= 1.4;
        }
        if (d >= this.maxPacketRange)
        {
            ++n2;
        } else if (d >= this.maxPacketRange * 1.2)
        {
            n2 += 4;
        } else if (d >= this.maxPacketRange * 1.4)
        {
            n2 += 5;
        }
        if (n2 == 3)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Invalid Hit: " + Common.FORMAT_0x00.format(d)));
            return;
        }
        if (n2 >= 4)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Invalid Hit: " + Common.FORMAT_0x00.format(d)));
            return;
        }
        if (n2 < 5) return;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGH, "Invalid Hit: " + Common.FORMAT_0x00.format(d)));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.lastPacketSend.containsKey(player.getUniqueId())) return;
        this.lastPacketSend.remove(player.getUniqueId());
    }
}

