package org.runnerer.spycheater.checks.spook;

import org.bukkit.entity.Player;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class SpookA
        extends Check
{

    private static SpookA spooka;
    private float lastYaw;
    private int lastBad;

    public SpookA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "SpookA", "Spook Client", 19, 2, 1, 0);
    }

    static SpookA SpookAInstance()
    {
        return spooka;
    }

    float onAim(Player player, float f)
    {
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        if (f2 > 1.0f && (float) Math.round(f2) == f2)
        {
            if (f2 == (float) this.lastBad)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(String.valueOf(this.lastYaw) + " - " + this.lastBad)));
                return f2;
            }
            this.lastBad = Math.round(f2);
            return 0.0f;
        }
        this.lastBad = 0;
        return 0.0f;
    }
}

