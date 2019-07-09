package org.runnerer.spycheater.checks.movement.anti;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;

public class NoVelocity
        extends Check
{

    private Map<Player, Long> lastVelocity = new HashMap<Player, Long>();
    private Map<Player, Integer> awaitingVelocity = new HashMap<Player, Integer>();
    private Map<Player, Double> totalMoved = new HashMap<Player, Double>();

    public NoVelocity(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "NoVelocity", "KnockbackMods Modification", 100, 50, 7, 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastVelocity.containsKey((Object) player))
        {
            this.lastVelocity.remove((Object) player);
        }
        if (this.awaitingVelocity.containsKey((Object) player))
        {
            this.awaitingVelocity.remove((Object) player);
        }
        if (!this.totalMoved.containsKey((Object) player)) return;
        this.totalMoved.remove((Object) player);
    }

    @EventHandler
    public void Move(PlayerMoveEvent playerMoveEvent)
    {
        double d;
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.WEB})) return;
        if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WEB}))
        {
            return;
        }
        if (UtilPlayer.isHoveringOverWater(player, 1)) return;
        if (UtilPlayer.isHoveringOverWater(player, 0))
        {
            return;
        }
        if (player.getAllowFlight())
        {
            return;
        }
        if (UtilServer.getPing(player) > 400)
        {
            return;
        }
        int n = 0;
        if (this.awaitingVelocity.containsKey((Object) player))
        {
            n = this.awaitingVelocity.get((Object) player);
        }
        long l = 0L;
        if (this.lastVelocity.containsKey((Object) player))
        {
            l = this.lastVelocity.get((Object) player);
        }
        if (player.getLastDamageCause() == null || player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && player.getLastDamageCause().getCause() != EntityDamageEvent.DamageCause.PROJECTILE)
        {
            n = 0;
        }
        if (System.currentTimeMillis() - l > 2000L && n > 0)
        {
            --n;
        }
        double d2 = 0.0;
        if (this.totalMoved.containsKey((Object) player))
        {
            d2 = this.totalMoved.get((Object) player);
        }
        if ((d = playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY()) > 0.0)
        {
            d2 += d;
        }
        int n2 = playerStats.getCheck(this, 0);
        int n3 = this.getThreshold();
        if (n > 0)
        {
            if (d2 < 0.3)
            {
                n2 += 9;
            } else
            {
                n2 = 0;
                d2 = 0.0;
                --n;
            }
            if (UtilPlayer.isOnGround(player, -1) || UtilPlayer.isOnGround(player, -2) || UtilPlayer.isOnGround(player, -3))
            {
                n2 -= 9;
            }
        }
        if (n2 > n3)
        {
            if (d2 == 0.0)
            {
                if (UtilServer.getPing(player) > 500)
                {
                    return;
                }
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Received no velocity"));
            } else
            {
                if (UtilServer.getPing(player) > 220)
                {
                    return;
                }
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Received less velocity than expected"));
            }
            n2 = 0;
            d2 = 0.0;
            --n;
        }
        playerStats.setCheck(this, 0, n2);
        this.awaitingVelocity.put(player, n);
        this.totalMoved.put(player, d2);
    }

    @EventHandler
    public void Velocity(PlayerVelocityEvent playerVelocityEvent)
    {
        long l;
        Player player = playerVelocityEvent.getPlayer();
        if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.WEB})) return;
        if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WEB}))
        {
            return;
        }
        if (UtilPlayer.isHoveringOverWater(player, 1)) return;
        if (UtilPlayer.isHoveringOverWater(player, 0))
        {
            return;
        }
        if (UtilPlayer.isOnGround(player, -1)) return;
        if (UtilPlayer.isOnGround(player, -2)) return;
        if (UtilPlayer.isOnGround(player, -3))
        {
            return;
        }
        if (player.getAllowFlight())
        {
            return;
        }
        if (this.lastVelocity.containsKey((Object) player) && (l = System.currentTimeMillis() - this.lastVelocity.get((Object) player)) < 500L)
        {
            return;
        }
        Vector vector = playerVelocityEvent.getVelocity();
        double d = Math.abs(vector.getY());
        if (!(d > 0.0)) return;
        double d2 = (int) (Math.pow(d + 2.0, 2.0) * 5.0);
        if (!(d2 > 20.0)) return;
        int n = 0;
        if (this.awaitingVelocity.containsKey((Object) player))
        {
            n = this.awaitingVelocity.get((Object) player);
        }
        this.awaitingVelocity.put(player, ++n);
        this.lastVelocity.put(player, System.currentTimeMillis());
    }
}

