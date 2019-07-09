package org.runnerer.spycheater.checks.combat;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Regen
        extends Check
{

    private long speedSatiated = 2000L;
    private long speedPeaceful = 400L;
    private Map<UUID, Long> lastRegen = new HashMap<UUID, Long>();
    private Map<UUID, Long> lastPeacefulRegen = new HashMap<UUID, Long>();
    private Map<UUID, Long> lastPotionRegen = new HashMap<UUID, Long>();

    public Regen(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.COMBAT, "Regen", "Regen", 3, 50, 2, 0);
    }

    @EventHandler
    public void onEntityRegainHealth(final EntityRegainHealthEvent entityRegainHealthEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        if (((EntityEvent) entityRegainHealthEvent).getEntityType() != EntityType.PLAYER)
        {
            return;
        }
        final Player player = (Player) ((EntityEvent) entityRegainHealthEvent).getEntity();
        final PlayerStats playerStats = this.getCore().getPlayerStats(player);
        long n = -1000L;
        int check = playerStats.getCheck(this, 0);
        final int threshold = this.getThreshold();
        long n2 = 0L;
        switch (entityRegainHealthEvent.getRegainReason().ordinal())
        {
            case 2:
            {
                n2 = this.speedSatiated;
                if (this.lastRegen.containsKey(player.getUniqueId()))
                {
                    n = System.currentTimeMillis() - this.lastRegen.get(player.getUniqueId());
                }
                this.lastRegen.put(player.getUniqueId(), System.currentTimeMillis());
                break;
            }
            case 1:
            {
                n2 = this.speedPeaceful;
                if (this.lastPeacefulRegen.containsKey(player.getUniqueId()))
                {
                    n = System.currentTimeMillis() - this.lastPeacefulRegen.get(player.getUniqueId());
                }
                this.lastPeacefulRegen.put(player.getUniqueId(), System.currentTimeMillis());
                break;
            }
            case 6:
            {
                for (final PotionEffect potionEffect : player.getActivePotionEffects())
                {
                    if (potionEffect.getType().equals((Object) PotionEffectType.REGENERATION))
                    {
                        switch (potionEffect.getAmplifier())
                        {
                            case 0:
                            {
                                n2 = 1500L;
                                continue;
                            }
                            case 1:
                            {
                                n2 = 750L;
                                continue;
                            }
                            case 2:
                            {
                                n2 = 250L;
                                continue;
                            }
                            default:
                            {
                                return;
                            }
                        }
                    }
                }
                if (this.lastPotionRegen.containsKey(player.getUniqueId()))
                {
                    n = System.currentTimeMillis() - this.lastPotionRegen.get(player.getUniqueId());
                }
                this.lastPotionRegen.put(player.getUniqueId(), System.currentTimeMillis());
                break;
            }
            default:
            {
                return;
            }
        }
        if (n == -1000L)
        {
            return;
        }
        if (n2 > n)
        {
            ++check;
            if (n2 / 2L > n)
            {
                ++check;
            }
            if (n2 / 3L > n)
            {
                ++check;
            }
        } else
        {
            --check;
        }
        if (check > threshold * 1.5)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.valueOf(n) + "ms"));
            check = 0;
        } else if (check > threshold)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(n) + "ms"));
            check = 0;
        }
        playerStats.setCheck(this, 0, check);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastPeacefulRegen.containsKey(player.getUniqueId()))
        {
            this.lastPeacefulRegen.remove(player.getUniqueId());
        }
        if (this.lastPotionRegen.containsKey(player.getUniqueId()))
        {
            this.lastPotionRegen.remove(player.getUniqueId());
        }
        if (!this.lastRegen.containsKey(player.getUniqueId())) return;
        this.lastRegen.remove(player.getUniqueId());
    }
}

