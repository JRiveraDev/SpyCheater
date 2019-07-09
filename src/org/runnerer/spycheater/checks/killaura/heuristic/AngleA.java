package org.runnerer.spycheater.checks.killaura.heuristic;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.AngleUtil;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.common.utils.UtilTime;
import org.runnerer.spycheater.events.PacketUseEntityEvent;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.AbstractMap;
import java.util.Map;
import java.util.UUID;

public class AngleA
        extends Check
{

    private Map<UUID, Map.Entry<Integer, Long>> AuraTicks;

    public AngleA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "AngleA", "KillAura", 15, 4, 13, 0);
    }

    @EventHandler
    public void onUseEntity(PacketUseEntityEvent packetUseEntityEvent)
    {
        int n;
        if (packetUseEntityEvent.getAction() != EnumWrappers.EntityUseAction.ATTACK)
        {
            return;
        }
        if (!(packetUseEntityEvent.getAttacked() instanceof Player))
        {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        Player player2 = (Player) packetUseEntityEvent.getAttacked();
        if (player.getAllowFlight())
        {
            return;
        }
        if (player2.getAllowFlight())
        {
            return;
        }
        int n2 = 0;
        long l = System.currentTimeMillis();
        if (this.AuraTicks.containsKey(player.getUniqueId()))
        {
            n2 = this.AuraTicks.get(player.getUniqueId()).getKey();
            l = this.AuraTicks.get(player.getUniqueId()).getValue();
        }
        double d = AngleUtil.getOffsets(player2, (LivingEntity) player)[0];
        double d2 = 300.0;
        if (player.getVelocity().length() > 0.08)
        {
            d2 += 200.0;
        }
        if ((n = UtilServer.getPing(player)) >= 100 && n < 200)
        {
            d2 += 50.0;
        } else if (n >= 200 && n < 250)
        {
            d2 += 75.0;
        } else if (n >= 250 && n < 300)
        {
            d2 += 150.0;
        } else if (n >= 300 && n < 350)
        {
            d2 += 300.0;
        } else if (n >= 350 && n < 400)
        {
            d2 += 400.0;
        } else if (n > 400)
        {
            return;
        }
        if (d > d2 * 4.0)
        {
            n2 += 12;
        } else if (d > d2 * 3.0)
        {
            n2 += 10;
        } else if (d > d2 * 2.0)
        {
            n2 += 8;
        } else if (d > d2)
        {
            n2 += 4;
        }
        if (this.AuraTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(l, 60000L))
        {
            n2 = 0;
            l = UtilTime.nowlong();
        }
        if (n2 >= 19)
        {
            n2 = 0;
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Logged Offset: " + Common.FORMAT_0x00.format(String.valueOf(d) + " > " + d2)));
        }
        this.AuraTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(n2, l));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
    }
}

