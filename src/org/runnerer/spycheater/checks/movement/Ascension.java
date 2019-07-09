package org.runnerer.spycheater.checks.movement;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.CheatUtil;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Ascension
        extends Check
{

    private double maxHeight = 1.5;
    private Map<UUID, Long> lastDamage = new HashMap<UUID, Long>();
    private Map<UUID, Map.Entry<Long, Double>> AscensionTicks = new HashMap<UUID, Map.Entry<Long, Double>>();

    public Ascension(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Ascension", "Ascension", 3, 50, 5, 0);
    }

    @EventHandler
    public void CheckAscension(PlayerMoveEvent playerMoveEvent)
    {
        Location location;
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (playerMoveEvent.getFrom().getY() >= playerMoveEvent.getTo().getY())
        {
            return;
        }
        if (player.getAllowFlight())
        {
            return;
        }
        if (player.getVehicle() != null)
        {
            return;
        }
        if (playerStats.getVelocityY() > 0.0)
        {
            return;
        }
        if (playerStats.getVelocityXZ() > 0.0)
        {
            return;
        }
        if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK})) return;
        if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS})) return;

        long l = System.currentTimeMillis();
        double d = 0.0;
        if (this.AscensionTicks.containsKey(player.getUniqueId()))
        {
            l = this.AscensionTicks.get(player.getUniqueId()).getKey();
            d = this.AscensionTicks.get(player.getUniqueId()).getValue();
        }
        long l2 = System.currentTimeMillis() - l;
        double d2 = UtilMath.offset(UtilMath.getVerticalVector(playerMoveEvent.getFrom().toVector()), UtilMath.getVerticalVector(playerMoveEvent.getTo().toVector()));
        if (d2 > 0.0)
        {
            d += d2;
        }
        if (CheatUtil.blocksNear(player))
        {
            d = 0.0;
        }
        if (CheatUtil.blocksNear(location = player.getLocation().subtract(0.0, 1.0, 0.0)))
        {
            d = 0.0;
        }
        double d3 = 0.5;
        if (player.hasPotionEffect(PotionEffectType.JUMP))
        {
            for (PotionEffect potionEffect : player.getActivePotionEffects())
            {
                if (!potionEffect.getType().equals((Object) PotionEffectType.JUMP)) continue;
                int n = potionEffect.getAmplifier() + 1;
                d3 += Math.pow((double) n + 4.2, 2.0) / 16.0;
                break;
            }
        }
        if (d > d3)
        {
            if (l2 > 500L)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(d2)));
                playerMoveEvent.setCancelled(true);
                l = System.currentTimeMillis();
            }
        } else
        {
            l = System.currentTimeMillis();
        }
        this.AscensionTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Long, Double>(l, d));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent entityDamageEvent)
    {
        if (entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!(entityDamageEvent.getEntity() instanceof Player)) return;
        Player player = (Player) entityDamageEvent.getEntity();
        this.lastDamage.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.lastDamage.containsKey(player.getUniqueId())) return;
        this.lastDamage.remove(player.getUniqueId());
    }
}

