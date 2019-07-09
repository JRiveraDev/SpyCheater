package org.runnerer.spycheater.checks.movement.speed;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.*;
import org.runnerer.spycheater.events.VelocityEvent;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SpeedA
        extends Check
{

    public int groundThreshold = 12;
    public int bunnyThreshold = 12;
    private Map<UUID, Vector> playerVelocity = new ConcurrentHashMap<UUID, Vector>();
    private Map<UUID, Long> velocityUpdate = new ConcurrentHashMap<UUID, Long>();
    private long velocityDeacyTime = 2500L;
    private double maxVelocityBeforeDeacy = 0.03;

    public SpeedA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Speed", "Speed", 0, 50, 12, 0);
        new BukkitRunnable()
        {

            public void run()
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    long l;
                    Vector vector = player.getVelocity();
                    UUID uUID = player.getUniqueId();
                    if (!SpeedA.this.velocityUpdate.containsKey(player.getUniqueId()) || (l = System.currentTimeMillis() - (Long) SpeedA.this.velocityUpdate.get(player.getUniqueId())) <= SpeedA.this.velocityDeacyTime || !(vector.getX() < SpeedA.this.maxVelocityBeforeDeacy && vector.getY() < SpeedA.this.maxVelocityBeforeDeacy && vector.getZ() < SpeedA.this.maxVelocityBeforeDeacy) && (!(vector.getX() > -SpeedA.this.maxVelocityBeforeDeacy) || !(vector.getY() > -SpeedA.this.maxVelocityBeforeDeacy) || !(vector.getZ() > -SpeedA.this.maxVelocityBeforeDeacy)))
                        continue;
                    SpeedA.this.playerVelocity.remove(player.getUniqueId());
                }
            }
        }.runTaskTimer((Plugin) antiCheat, 0L, 1L);
    }

    @EventHandler
    public void Move(PlayerMoveEvent playerMoveEvent)
    {
        double d;
        PlayerStats playerStats;
        Player player;
        int n;
        double d2;
        int n2;
        block29:
        {
            block28:
            {
                block25:
                {
                    double d3;
                    int n3;
                    block27:
                    {
                        block26:
                        {
                            if (!this.isEnabled())
                            {
                                return;
                            }
                            player = playerMoveEvent.getPlayer();
                            playerStats = this.getCore().getPlayerStats(player);
                            double d4 = UtilServer.getPing(player);
                            if (player.isFlying())
                            {
                                return;
                            }
                            if (playerStats.getVelocityXZ() > 0.0)
                            {
                                return;
                            }
                            if (player.isInsideVehicle())
                            {
                                return;
                            }
                            if (playerStats.getLastMountDiff() < 500L)
                            {
                                return;
                            }
                            if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK})) return;
                            if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS}))
                            {
                                return;
                            }
                            if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.SPONGE})) return;
                            if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS}))
                            {
                                return;
                            }
                            double d5 = UtilMath.offset(UtilMath.getHorizontalVector(playerMoveEvent.getFrom().toVector()), UtilMath.getHorizontalVector(playerMoveEvent.getTo().toVector()));
                            d2 = UtilMath.offset(UtilMath.getHorizontalVector(playerMoveEvent.getFrom().toVector()), UtilMath.getHorizontalVector(playerMoveEvent.getTo().toVector()));
                            n2 = 0;
                            for (PotionEffect potionEffect : player.getActivePotionEffects())
                            {
                                if (!potionEffect.getType().equals((Object) PotionEffectType.SPEED)) continue;
                                n2 = potionEffect.getAmplifier() + 1;
                                break;
                            }
                            if (!playerStats.isOnGround()) break block25;
                            d3 = d5 == 0.5 ? 0.37 : 0.34;
                            n3 = playerStats.getCheck(this, 0);
                            if (UtilPlayer.isOnStairs(player, 0) || UtilPlayer.isOnStairs(player, 1))
                            {
                                d3 = 0.45;
                            }
                            if (playerStats.getLastGroundTimeDiff() > 150L || playerStats.getLastBunnyTimeDiff() < 150L)
                            {
                                d3 = 0.65;
                            }
                            if (UtilPlayer.isOnGround(player, -2))
                            {
                                d3 = 0.75;
                            }
                            if (d2 == 0.45)
                            {
                                return;
                            }
                            if (d4 > 600.0)
                            {
                                return;
                            }
                            if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.SKULL})) return;
                            if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.ICE, Material.PACKED_ICE}))
                                return;
                            if (UtilPlayer.isOnBlock(player, 2, new Material[]{Material.ICE, Material.PACKED_ICE}))
                                return;
                            if (UtilPlayer.isOnBlock(player, 3, new Material[]{Material.SKULL}))
                            {
                                return;
                            }
                            if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.ICE, Material.PACKED_ICE}))
                                break block26;
                            if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.ICE, Material.PACKED_ICE}))
                                break block26;
                            if (UtilPlayer.isOnBlock(player, 2, new Material[]{Material.ICE, Material.PACKED_ICE}))
                                break block26;
                            if (!UtilPlayer.isOnBlock(player, 3, new Material[]{Material.ICE, Material.PACKED_ICE}))
                                break block27;
                        }
                        d3 = UtilPlayer.isOnGround(player, -2) ? 1.0 : 0.65;
                    }
                    d3 += (double) (player.getWalkSpeed() > 0.2f ? player.getWalkSpeed() * 10.0f * 0.33f : 0.0f);
                    d3 += 0.06 * (double) n2;
                    if (this.playerVelocity.containsKey(player.getUniqueId()))
                    {
                        d3 += VelocityUtil.getVelocityHorizontalAsDistance(this.playerVelocity.get(player.getUniqueId()));
                        d3 += 0.9;
                    }
                    if (d2 == 0.34)
                    {
                        return;
                    }
                    n3 = d2 > d3 ? (n3 += 2) : --n3;
                    if (n3 > this.groundThreshold)
                    {
                        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Ground " + Common.FORMAT_0x00.format(d2) + " > " + Common.FORMAT_0x00.format(d3)));
                        n3 = 0;
                    }
                    playerStats.setCheck(this, 0, n3);
                    return;
                }
                d = 0.4;
                n = playerStats.getCheck(this, 0);
                if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.ICE, Material.PACKED_ICE})) break block28;
                if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.ICE, Material.PACKED_ICE})) break block28;
                if (UtilPlayer.isOnBlock(player, 2, new Material[]{Material.ICE, Material.PACKED_ICE})) break block28;
                if (!UtilPlayer.isOnBlock(player, 3, new Material[]{Material.ICE, Material.PACKED_ICE})) break block29;
            }
            d = UtilPlayer.isOnGround(player, -2) ? 1.0 : 0.65;
        }
        d += (double) (player.getWalkSpeed() > 0.2f ? player.getWalkSpeed() * 10.0f * 0.33f : 0.0f);
        d += 0.02 * (double) n2;
        if (this.playerVelocity.containsKey(player.getUniqueId()))
        {
            d += VelocityUtil.getVelocityHorizontalAsDistance(this.playerVelocity.get(player.getUniqueId()));
            d += 0.9;
        }
        n = d2 > d ? (n += 2) : --n;
        if (n > this.bunnyThreshold)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "Bunny " + Common.FORMAT_0x00.format(d2) + " > " + Common.FORMAT_0x00.format(d)));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onVelocity(VelocityEvent velocityEvent)
    {
        this.playerVelocity.put(velocityEvent.getPlayer().getUniqueId(), velocityEvent.getVec());
        if (!this.velocityUpdate.containsKey(velocityEvent.getPlayer().getUniqueId()))
        {
            this.velocityUpdate.put(velocityEvent.getPlayer().getUniqueId(), System.currentTimeMillis());
            return;
        }
        this.velocityUpdate.put(velocityEvent.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.playerVelocity.containsKey(player.getUniqueId()))
        {
            this.playerVelocity.remove(player.getUniqueId());
        }
        if (!this.velocityUpdate.containsKey(player.getUniqueId())) return;
        this.velocityUpdate.remove(player.getUniqueId());
    }

}

