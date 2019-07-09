package org.runnerer.spycheater.checks.movement.fly;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Glide
        extends Check
{

    private Map<UUID, Double> lastOffsetY = new HashMap<UUID, Double>();

    public Glide(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Glide", "Glide", 6, 50, 8, 0);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (player.getAllowFlight())
        {
            return;
        }
        if (player.isInsideVehicle())
        {
            return;
        }
        if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.WEB})) return;
        if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WEB}))
        {
            return;
        }
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals((Object) Material.WEB))
        {
            return;
        }
        if (UtilPlayer.isHoveringOverWater(player, 1)) return;
        if (UtilPlayer.isHoveringOverWater(player, 0))
        {
            return;
        }
        if (playerStats.getVelocityY() > 0.0)
        {
            return;
        }
        if (playerStats.getLastWorldChangeDiff() < 3000L)
        {
            return;
        }
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        double d = playerMoveEvent.getFrom().getY() - playerMoveEvent.getTo().getY();
        double d2 = 0.0;
        if (this.lastOffsetY.containsKey(player.getUniqueId()))
        {
            d2 = this.lastOffsetY.get(player.getUniqueId());
        }
        this.lastOffsetY.put(player.getUniqueId(), d);
        n = d > 0.0 && d < 0.17 && d2 > 0.0 && d2 < 0.17 && !playerStats.isOnGround() ? ++n : (n -= 2);
        if (n > n2)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(d)));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.lastOffsetY.containsKey(player.getUniqueId())) return;
        this.lastOffsetY.remove(player.getUniqueId());
    }
}

