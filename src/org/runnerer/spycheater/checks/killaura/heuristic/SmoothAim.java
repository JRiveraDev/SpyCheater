package org.runnerer.spycheater.checks.killaura.heuristic;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class SmoothAim
        extends Check
{

    private int smoothAim = 0;

    public SmoothAim(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "SmoothAim", "Smooth Aim", 110, 50, 14, 0);
    }

    public static double getFrac(double d)
    {
        return d % 1.0;
    }

    public int getSmoothAim()
    {
        return this.smoothAim;
    }

    public void setSmoothAim(int n)
    {
        this.smoothAim = n;
        if (this.smoothAim >= 0) return;
        this.smoothAim = 0;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent)
    {
        Location location = playerMoveEvent.getFrom().clone();
        Location location2 = playerMoveEvent.getTo().clone();
        Player player = playerMoveEvent.getPlayer();
        double d = Math.abs(location.getYaw() - location2.getYaw());
        if (!(d > 0.0)) return;
        if (!(d < 360.0)) return;
        if (SmoothAim.getFrac(d) == 0.0)
        {
            this.setSmoothAim(this.getSmoothAim() + 100);
            if (this.getSmoothAim() <= 2000) return;
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(d)));
            this.setSmoothAim(0);
            return;
        }
        this.setSmoothAim(this.getSmoothAim() - 21);
    }
}

