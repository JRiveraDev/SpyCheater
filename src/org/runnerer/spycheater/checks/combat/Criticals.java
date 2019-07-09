package org.runnerer.spycheater.checks.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class Criticals extends Check
{

    public Criticals(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.COMBAT, "Criticals", "Criticals", 3, 50, 26, 0);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() == null || !(event.getDamager() instanceof Player))
            return;
        Player damager = (Player) event.getDamager();
        PlayerStats playerStats = this.getCore().getPlayerStats(damager);

        if (damager.isOnGround())
            return;

        if (!playerStats.isOnGround())
            return;

        this.getCore().addViolation(damager, this, new Violation(this, ViolationPriority.LOW, "Criticals"));
    }

}

