package org.runnerer.spycheater.checks.movement.anti;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class NoWeb
        extends Check
{

    public NoWeb(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "NoWeb", "No Web", 3, 50, 65, 0);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (!this.isEnabled())
        {
            return;
        }
        if (player.isFlying())
        {
            return;
        }
        if (player.isInsideVehicle())
        {
            return;
        }

        if (player.getLocation().getBlock().getType() != Material.WEB)
            return;

        double diff = event.getTo().distanceSquared(event.getFrom());

        if (diff < .012)
            return;

        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "NoWeb"));

    }
}

