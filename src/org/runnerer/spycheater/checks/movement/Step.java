package org.runnerer.spycheater.checks.movement;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Step
        extends Check
{

    private double stepHeight = 0.9;
    private Map<UUID, Double> lastOffset = new ConcurrentHashMap<UUID, Double>();
    private int bypassThreshold = 10;

    public Step(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Step", "Step", 3, 50, 7, 0);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
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
        if (playerStats.getVelocityY() > 0.0)
        {
            return;
        }
        if (player.hasPotionEffect(PotionEffectType.JUMP))
        {
            return;
        }
        if (UtilPlayer.isOnClimbable(player))
        {
            return;
        }
        if (UtilPlayer.isOnStairs(playerMoveEvent.getTo(), 1)) return;
        if (UtilPlayer.isOnStairs(playerMoveEvent.getTo(), 0))
        {
            return;
        }
        if (playerStats.getLastMountDiff() < 500L)
        {
            return;
        }
        if (!playerStats.isOnGround())
        {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        double d = playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY();
        double d2 = 0.0;
        if (this.lastOffset.containsKey(player.getUniqueId()))
        {
            d2 = this.lastOffset.get(player.getUniqueId());
        }
        this.lastOffset.put(player.getUniqueId(), d);
        if (d > 0.0)
        {
            if (d > this.stepHeight)
            {
                ++n;
                if (d > this.stepHeight * 2.0)
                {
                    ++n;
                }
                if (d > this.stepHeight * 3.0)
                {
                    ++n;
                }
                if (d > this.stepHeight * 4.0)
                {
                    ++n;
                }
                if (d > this.stepHeight * 5.0)
                {
                    ++n;
                }
                if (d > this.stepHeight * 6.0)
                {
                    ++n;
                }
                if (playerStats.isOnGround())
                {
                    n += 2;
                }
            } else
            {
                --n;
            }
            int n3 = playerStats.getCheck(this, 1);
            int n4 = this.bypassThreshold;
            if (playerStats.isOnGround() && d > 0.0 && d != 0.5 && d2 > 0.0 && d2 != 0.5 && playerMoveEvent.getTo().getY() - (double) playerMoveEvent.getFrom().getBlockY() == 1.0 && playerMoveEvent.getTo().getX() - (double) playerMoveEvent.getFrom().getBlockX() != 0.0 && playerMoveEvent.getTo().getZ() - (double) playerMoveEvent.getFrom().getBlockZ() != 0.0 && playerMoveEvent.getTo().getBlock() != playerMoveEvent.getFrom().getBlock() && playerMoveEvent.getFrom().getBlock().getType() == Material.AIR)
            {
                n3 += 5;
                if (player.isOnGround())
                {
                    n3 += 10;
                }
            } else
            {
                --n3;
            }
            if (n3 > n4)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Bypass"));
                n3 = 0;
            }
            playerStats.setCheck(this, 1, n3);
        }
        if (n > n2)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(Common.FORMAT_0x00.format(d)) + " > " + this.stepHeight));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.lastOffset.containsKey(player.getUniqueId())) return;
        this.lastOffset.remove(player.getUniqueId());
    }
}

