package org.runnerer.spycheater.checks.killaura.reach;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
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
import org.runnerer.spycheater.events.PacketUseEntityEvent;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReachA
        extends Check
{

    private int firstHitThreshold = 50;
    private double maxRange = 5.1;
    private double velocity = 1.175;
    private Map<UUID, Long> lastAttack = new HashMap<UUID, Long>();

    public ReachA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "Reach", "Reach", 110, 50, 17, 0);
    }

    @EventHandler
    public void onDamage(PacketUseEntityEvent packetUseEntityEvent)
    {
        int n;
        if (!this.isEnabled())
        {
            return;
        }
        if (!(packetUseEntityEvent.getAttacked() instanceof Player))
        {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        Player player2 = (Player) packetUseEntityEvent.getAttacked();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        PlayerStats playerStats2 = this.getCore().getPlayerStats(player2);
        double d = player.getLocation().distance(player2.getLocation());
        long l = 701L;
        double d2 = this.maxRange;
        if (this.lastAttack.containsKey(player.getUniqueId()))
        {
            l = System.currentTimeMillis() - this.lastAttack.get(player.getUniqueId());
        }
        this.lastAttack.put(player.getUniqueId(), System.currentTimeMillis());
        if (player.getGameMode().equals((Object) GameMode.CREATIVE))
        {
            return;
        }
        if (player.isFlying()) return;
        if (player2.isFlying())
        {
            return;
        }
        if (d > 6.0)
        {
            return;
        }
        if (!player2.hasLineOfSight((Entity) player))
        {
            return;
        }
        if (player.isSprinting())
        {
            d2 += 0.2;
        }
        if (player2.isSprinting())
        {
            d2 += 0.3;
        }
        double d3 = AngleUtil.getOffsets(player2, (LivingEntity) player)[0];
        if (l > 700L)
        {
            int n2 = playerStats.getCheck(this, 0);
            int n3 = this.firstHitThreshold;
            int n4 = UtilServer.getPing(player);
            if (n4 > 400)
            {
                return;
            }
            if (n4 > 300)
            {
                d2 *= 1.6;
            } else if (n4 > 250)
            {
                d2 *= 2.0;
            } else if (n4 > 200)
            {
                d2 *= 1.4;
            }
            if (d >= d2)
            {
                if (d >= d2 * 1.2)
                {
                    n2 += 5;
                    if (d >= d2 * 1.4)
                    {
                        n2 += 10;
                    }
                }
                n2 += 15;
            } else
            {
                n2 -= 5;
            }
            if (n2 > n3)
            {
                if (d >= d2 * 1.4)
                {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "First Hit: " + Common.FORMAT_0x00.format(d)));
                } else
                {
                    this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "First Hit: " + Common.FORMAT_0x00.format(d)));
                }
                n2 = 0;
            }
            playerStats.setCheck(this, 0, n2);
            return;
        }
        double d4 = this.maxRange;
        int n5 = playerStats.getCheck(this, 1);
        int n6 = this.getThreshold();
        if (playerStats2.getVelocityXZ() > 12.0)
        {
            d4 *= this.velocity;
        }
        if (playerStats2.getVelocityY() > 12.0)
        {
            d4 *= this.velocity;
        }
        if ((n = UtilServer.getPing(player)) > 400)
        {
            d4 *= 1.3;
        } else if (n > 300)
        {
            d4 *= 1.2;
        } else if (n > 200)
        {
            d4 *= 1.05;
        }
        if (d >= d4)
        {
            if (d >= d4 * 1.5)
            {
                n5 += 35;
            } else if (d >= d4 * 1.4)
            {
                n5 += 30;
            } else if (d >= d4 * 1.3)
            {
                n5 += 25;
            } else if (d >= d4 * 1.2)
            {
                n5 += 15;
            } else if (d >= d4 * 1.1)
            {
                n5 += 10;
            }
            n5 += 15;
        } else
        {
            n5 -= 8;
        }
        if (n5 > n6)
        {
            if (d >= d4 * 1.3)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, Common.FORMAT_0x00.format(d)));
            } else
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(d)));
            }
            n5 = 0;
        }
        playerStats.setCheck(this, 1, n5);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.lastAttack.containsKey(player.getUniqueId())) return;
        this.lastAttack.remove(player.getUniqueId());
    }
}

