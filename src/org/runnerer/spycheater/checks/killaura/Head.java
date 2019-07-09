package org.runnerer.spycheater.checks.killaura;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.ActiveDistance;

import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class Head
        extends Check
{

    public Head(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "Heuristics", "Heuristics Data", 110, 7, 16, 0);
    }

    @EventHandler
    public void onHeadHit(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) return;
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        if (!player.hasLineOfSight(entityDamageByEntityEvent.getEntity()))
        {
            for (Player player2 : Bukkit.getServer().getOnlinePlayers())
            {
            }

            entityDamageByEntityEvent.setCancelled(true);
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Invalid HeadSpin " + player.getEyeHeight()));
        }
        if (player.isDead()) return;
        this.runCheck(player, entityDamageByEntityEvent.getEntity());
    }

    public void runCheck(Player player, Entity entity)
    {
        ActiveDistance activeDistance = new ActiveDistance(player.getLocation(), entity.getLocation());
        double d = activeDistance.getxDiff();
        double d2 = activeDistance.getzDiff();
        Player player2 = player;
        if (d == 0.0) return;
        if (d2 == 0.0)
        {
            return;
        }
        if (activeDistance.getyDiff() >= 0.6)
        {
            return;
        }
        Location location = null;
        if (d <= 0.5 && d2 >= 1.0)
        {
            location = player2.getLocation().getZ() > entity.getLocation().getZ() ? player2.getLocation().add(0.0, 0.0, -1.0) : player2.getLocation().add(0.0, 0.0, 1.0);
        } else if (d2 <= 0.5 && d >= 1.0)
        {
            location = player2.getLocation().getX() > entity.getLocation().getX() ? player2.getLocation().add(-1.0, 0.0, 0.0) : player2.getLocation().add(1.0, 0.0, 0.0);
        }
        if (location == null) return;
        if (!location.getBlock().getType().isSolid()) return;
        location.add(0.0, 1.0, 0.0).getBlock().getType().isSolid();
    }
}

