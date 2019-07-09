package org.runnerer.spycheater.checks.killaura.heuristic;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.AngleUtil;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class AngleB
        extends Check
{

    public AngleB(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "AngleB", "KillAura", 19, 2, 13, 0);
    }

    @EventHandler
    public void onAngleHit(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player))
        {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player))
        {
            return;
        }
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        Player player2 = (Player) entityDamageByEntityEvent.getEntity();
        double d = UtilServer.getPing(player);
        double d2 = UtilServer.getPing(player2);
        double d3 = AngleUtil.getOffsets(player, (LivingEntity) player2)[0];
        if (d2 > 450.0)
        {
            return;
        }
        if (d >= 100.0 && d < 200.0)
        {
            d3 -= 50.0;
        } else if (d >= 200.0 && d < 250.0)
        {
            d3 -= 75.0;
        } else if (d >= 250.0 && d < 300.0)
        {
            d3 -= 150.0;
        } else if (d >= 300.0 && d < 350.0)
        {
            d3 -= 300.0;
        } else if (d >= 350.0 && d < 400.0)
        {
            d3 -= 350.0;
        } else if (d > 400.0)
        {
            return;
        }
        if (!(d3 >= 300.0)) return;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "AngleHit B " + Common.FORMAT_0x00.format(String.valueOf(d3) + " > " + "400")));
    }
}

