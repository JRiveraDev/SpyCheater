package org.runnerer.spycheater.checks.world.scaffold;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.List;
import java.util.Set;

public class ScaffoldA extends Check
{

    private int _scaffoldWarnCount = 0;

    public ScaffoldA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.OTHER, "Scaffold", "Scaffold", 27, 50, 976, 0);
    }

    @EventHandler
    public void onBlockPlace(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        Block target = player.getTargetBlock((Set<Material>) null, 10);
        PlayerStats playerStats = this.getCore().getPlayerStats(player);

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        if (!event.isBlockInHand())
            return;

        if (player.isFlying())
            return;

        if (player.getFallDistance() > 0) return;
        if (!playerStats.isOnGround()) return;
        if (playerStats.getVelocityXZ() > 0) return;
        if ((double) player.getWalkSpeed() > 0.21) return;
        if (playerStats.getVelocityY() > 2) return;

        if (player.getLocation().getY() <= target.getLocation().getY())
            return;

        if (target.isLiquid())
            return;

        List<Block> blocks = player.getLastTwoTargetBlocks((Set<Material>) null, 10);
        BlockFace face = null;
        if (blocks.size() > 1)
        {
            face = blocks.get(1).getFace(blocks.get(0));
        } else
        {
            return;
        }

        if (event.getBlockFace() == face)
            return;

        getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Scaffold"));
    }
}
