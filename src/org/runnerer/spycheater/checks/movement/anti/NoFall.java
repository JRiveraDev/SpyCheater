package org.runnerer.spycheater.checks.movement.anti;

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

public class NoFall
        extends Check
{

    public NoFall(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "NoFall", "No Fall", 3, 50, 4, 0);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent playerMoveEvent)
    {
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (!this.isEnabled())
        {
            return;
        }
        if (player.getAllowFlight())
        {
            return;
        }
        if (player.isInsideVehicle())
        {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        n = playerMoveEvent.getFrom().getY() > playerMoveEvent.getTo().getY() ? (player.isOnGround() && !playerStats.isOnGround() ? ++n : --n) : --n;
        if (n > n2)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Fake Packets"));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }
}

