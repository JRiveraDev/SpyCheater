package org.runnerer.spycheater.checks.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilTime;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.*;

public class TimerB
        extends Check
{

    public static Map<UUID, Map.Entry<Integer, Long>> timerTicks;

    public TimerB(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "TimerB", "Timer", 6, 50, 7, 0);

        timerTicks = new HashMap();

    }

    @EventHandler
    public void quitPlayer(PlayerQuitEvent event)
    {
        if (timerTicks.containsKey(event.getPlayer().getUniqueId()))
        {
            timerTicks.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent e)
    {
        Player player = e.getPlayer();

        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ() &&
                e.getFrom().getY() == e.getTo().getY()) {
            return;
        }

        int Count = 0;
        long Time = System.currentTimeMillis();
        if (timerTicks.containsKey(player.getUniqueId())) {
            Count = ((Integer)((Map.Entry)timerTicks.get(player.getUniqueId())).getKey()).intValue();
            Time = ((Long)((Map.Entry)timerTicks.get(player.getUniqueId())).getValue()).longValue();
        }

        Count++;

        if (timerTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(Time, 1000L)) {
            if (Count > 35) {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Timer"));
            }
            Count = 0;
            Time = UtilTime.nowlong();
        }
        timerTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry(Integer.valueOf(Count), Long.valueOf(Time)));
    }
}

