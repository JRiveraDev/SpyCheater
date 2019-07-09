
package org.runnerer.spycheater.checks.killaura.huzuni;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class HuzuniAura
        extends Check
{

    private float lastYaw;
    private float lastBad;

    public HuzuniAura(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.AUTOCLICKER, "HuzuniAura", "KillAura", 110, 7, 6, 0);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        float f;
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player))
        {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player))
        {
            return;
        }
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        this.lastYaw = f = player.getLocation().getYaw();
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
    }

    public boolean onAim(Player player, float f)
    {
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        this.lastBad = (float) Math.round(f2 * 10.0f) * 0.1f;
        if ((double) f < 0.1)
        {
            return true;
        }
        if (!(f2 > 1.0f)) return true;
        if ((float) Math.round(f2 * 10.0f) * 0.1f != f2) return true;
        if ((float) Math.round(f2) == f2) return true;
        if (f2 != this.lastBad) return false;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Aim: " + Common.FORMAT_0x00.format(f)));
        return true;
    }
}

