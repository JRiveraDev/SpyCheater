package org.runnerer.spycheater.checks.movement.jesus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JesusB
        extends Check
{

    private Map<UUID, Double> lastOffsetY = new HashMap<UUID, Double>();
    private Map<UUID, Double> lastFraction = new HashMap<UUID, Double>();
    private Map<UUID, Double> lastFractionOffset = new HashMap<UUID, Double>();
    private int patternThreshold = 25;
    private int upThreshold = 30;
    private int topThreshold = 30;

    public JesusB(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Jesus", "Jesus", 0, 50, 7, 0);
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
        double d = Math.abs(playerMoveEvent.getTo().getX() - playerMoveEvent.getFrom().getX()) + Math.abs(playerMoveEvent.getTo().getZ() - playerMoveEvent.getFrom().getZ());
        double d2 = Math.abs(playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY());
        double d3 = 0.0;
        if (this.lastOffsetY.containsKey(player.getUniqueId()))
        {
            d3 = this.lastOffsetY.get(player.getUniqueId());
        }
        this.lastOffsetY.put(player.getUniqueId(), d2);
        double d4 = UtilMath.getFraction(player.getLocation().getY());
        if (!UtilPlayer.isHoveringOverWater(player, 1))
        {
            if (!UtilPlayer.isHoveringOverWater(player, 0)) return;
        }
        if (UtilPlayer.isOnGround(player, 0)) return;
        if (UtilPlayer.isOnGround(player, -2)) return;
        if (UtilPlayer.isOnGround(player, -1)) return;
        if (UtilPlayer.isHoveringOverWater(player, -1)) return;
        if (d2 == 0.0) return;
        if (!(d2 <= 0.15)) return;
        if (!(d3 <= 0.15)) return;
        if (!(d > 0.0)) return;
        double d5 = this.lastFraction.getOrDefault(player.getUniqueId(), 0.0);
        this.lastFraction.put(player.getUniqueId(), d4);
        if (d5 != 0.0)
        {
            double d6 = Math.abs(d4 - d5);
            double d7 = this.lastFractionOffset.getOrDefault(player.getUniqueId(), 0.0);
            this.lastFractionOffset.put(player.getUniqueId(), d6);
            int n = playerStats.getCheck(this, 0);
            int n2 = this.patternThreshold;
            n = d4 > 0.8 && d5 < 0.3 ? (n += 6) : (d4 < 0.3 && d5 > 0.8 ? (n += 6) : (n -= 12));
            if (n > n2)
            {
                if (d7 != 0.0 && d7 == d6)
                {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "(experimental) (2)"));
                } else
                {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "(experimental) (1)"));
                }
                n = 0;
            }
            playerStats.setCheck(this, 0, n);
        }
        int n = playerStats.getCheck(this, 1);
        int n3 = this.upThreshold;
        if (d4 > 0.8)
        {
            n += 3;
            if (d4 > 0.9)
            {
                n += 6;
                if (d5 > 0.9)
                {
                    n += 3;
                }
            }
        } else
        {
            n -= 3;
        }
        if (playerStats.getVelocityY() > 0.0)
        {
            n -= 2;
        }
        if (n > n3)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "(3) " + d2 + " " + d4));
            n = 0;
        }
        playerStats.setCheck(this, 1, n);
        int n4 = playerStats.getCheck(this, 2);
        int n5 = this.topThreshold;
        n4 = !UtilPlayer.isHoveringOverWater(player, 0) && d4 < 0.1 ? (n4 += 6) : (n4 -= 8);
        if (playerStats.getVelocityY() > 0.0)
        {
            n4 -= 5;
        }
        if (n4 > n5)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.TEST, "(experimental) (4) " + d2 + " " + d4));
            n4 = 0;
        }
        playerStats.setCheck(this, 2, n4);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastOffsetY.containsKey(player.getUniqueId()))
        {
            this.lastOffsetY.remove(player.getUniqueId());
        }
        if (this.lastFraction.containsKey(player.getUniqueId()))
        {
            this.lastFraction.remove(player.getUniqueId());
        }
        if (!this.lastFractionOffset.containsKey(player.getUniqueId())) return;
        this.lastFractionOffset.remove(player.getUniqueId());
    }
}

