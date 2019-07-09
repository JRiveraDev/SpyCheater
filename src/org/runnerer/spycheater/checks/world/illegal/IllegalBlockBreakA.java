package org.runnerer.spycheater.checks.world.illegal;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class IllegalBlockBreakA extends Check
{

    public IllegalBlockBreakA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.OTHER, "IllegalBlockPlaceA", "Illegal Block Place", 3, 50, 1, 0);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();

        if (!event.getBlock().isLiquid())
            return;

        getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Illegal Block Break"));
    }
}
