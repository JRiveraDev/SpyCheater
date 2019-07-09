package org.runnerer.spycheater.checks.movement.jesus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JesusA
        extends Check
{

    private Map<UUID, Double> lastOffsetY = new HashMap<UUID, Double>();

    public JesusA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Jesus", "Jesus", 5, 50, 7, 0);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (player.getAllowFlight())
        {
            return;
        }
        if (player.isInsideVehicle())
        {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        double d = Math.abs(playerMoveEvent.getTo().getX() - playerMoveEvent.getFrom().getX()) + Math.abs(playerMoveEvent.getTo().getZ() - playerMoveEvent.getFrom().getZ());
        double d2 = Math.abs(playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY());
        double d3 = 0.0;
        if (d == 0.12) return;
        if (d == 0.9)
        {
            return;
        }
        if (this.lastOffsetY.containsKey(player.getUniqueId()))
        {
            d3 = this.lastOffsetY.get(player.getUniqueId());
        }
        this.lastOffsetY.put(player.getUniqueId(), d2);
        if (!UtilPlayer.isHoveringOverWater(player, 0))
        {
            if (!UtilPlayer.isHoveringOverWater(player, 1)) return;
            if (UtilPlayer.isOnGround(player, -2)) return;
        }
        if (!UtilPlayer.isOnGround(player, 0) && !UtilPlayer.isHoveringOverWater(player, -1) && d2 != 0.0 && d2 <= 0.15 && d3 <= 0.15 && d >= 0.01 && (UtilMath.getFraction(playerMoveEvent.getTo().getY()) > 0.75 || UtilMath.getFraction(playerMoveEvent.getTo().getY()) == 0.0))
        {
            ++n;
            if (d > 1.0)
            {
                n += 2;
                if (d > 2.0)
                {
                    n += 2;
                }
            }
        } else
        {
            n -= 2;
        }
        if (n > n2)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "+" + Common.FORMAT_0x00.format(d)));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.lastOffsetY.containsKey(player.getUniqueId())) return;
        this.lastOffsetY.remove(player.getUniqueId());
    }
}

