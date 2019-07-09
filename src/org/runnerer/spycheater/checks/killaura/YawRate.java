package org.runnerer.spycheater.checks.killaura;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class YawRate
        extends Check
{

    private float lastYaw;
    private float lastBad;
    private float lastYaw2;
    private float lastPitch;
    private int streak;
    private int min;

    public YawRate(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "Yawrate", "AimAssist", 110, 7, 12, 0);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent)
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
        float f = player.getLocation().getYaw();
        this.onAim(player, f);
        this.onAim3(player, f);
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

    public int onAim2(Player player, float f, float f2)
    {
        float f3 = f - this.lastYaw2;
        float f4 = f2 - this.lastPitch;
        if (!(Math.abs(f4) >= 2.0f)) return 0;
        if (f3 != 0.0f) return 0;
        ++this.streak;
        this.lastYaw2 = f;
        this.lastPitch = f2;
        if (this.streak < this.min) return 0;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Aim1: " + Common.FORMAT_0x00.format(this.streak)));
        return this.streak;
    }

    public float onAim3(Player player, float f)
    {
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        if (f2 > 0.1f && (float) Math.round(f2) == f2)
        {
            if (f2 == this.lastBad)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Aim2: " + Common.FORMAT_0x00.format(f)));
                return f2;
            }
            this.lastBad = Math.round(f2);
            return 0.0f;
        }
        this.lastBad = 0.0f;
        return 0.0f;
    }
}

