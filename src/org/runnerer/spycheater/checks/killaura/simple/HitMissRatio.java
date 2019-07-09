package org.runnerer.spycheater.checks.killaura.simple;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.Map;

public class HitMissRatio
        extends Check
{

    private Map<Player, Integer> swings;
    private Map<Player, Integer> hits;

    public HitMissRatio(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "HitMissRatios", "KillAura", 15, 4, 11, 0);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onUseEntity(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        double d;
        if (!entityDamageByEntityEvent.getCause().equals((Object) EntityDamageEvent.DamageCause.ENTITY_ATTACK))
        {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player))
        {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player))
        {
            return;
        }
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        this.hits.put(player, this.hits.get((Object) player) + 1);
        if (!this.hits.containsKey((Object) player)) return;
        if (!this.swings.containsKey((Object) player)) return;
        if (this.hits.get((Object) player) <= 20) return;
        if (this.swings.get((Object) player) <= 20) return;
        try
        {
            d = Math.min(this.hits.get((Object) player), this.swings.get((Object) player)) / Math.max(this.hits.get((Object) player), this.swings.get((Object) player));
        }
        catch (Exception exception)
        {
            return;
        }
        if (!(d > 80.0)) return;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(d *= 100.0)));
    }

    public Map<Player, Integer> getSwings()
    {
        return this.swings;
    }

    public Map<Player, Integer> getHits()
    {
        return this.hits;
    }
}

