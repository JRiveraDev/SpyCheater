package org.runnerer.spycheater.checks.movement;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

// UNDER CONSTRUCTION.
public class Sprint extends Check
{

    public Sprint(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.OTHER, "Sprint", "Sprint", 3, 50, 9, 0);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Location to = event.getTo();
        Location from = event.getFrom();

        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        float yaw = Math.abs(from.getYaw()) % 360;

        if (!event.getPlayer().isSprinting())
        {
            return;
        }

        if (deltaX < 0.0 && deltaZ > 0.0 && yaw > 180.0f && yaw < 270.0f || deltaX < 0.0 && deltaZ < 0.0 && yaw > 270.0f && yaw < 360.0f || deltaX > 0.0 && deltaZ < 0.0 && yaw > 0.0f && yaw < 90.0f || deltaX > 0.0 && deltaZ > 0.0 && yaw > 90.0f && yaw < 180.0f)
        {
            this.getCore().addViolation(event.getPlayer(), this, new Violation(this, ViolationPriority.LOW, "Unexpectedly sprinted"));
        }
    }
}
