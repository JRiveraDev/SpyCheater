package org.runnerer.spycheater.checks.movement;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Change
        extends Check
{

    private List<UUID> built = new ArrayList<UUID>();
    private List<UUID> falling = new ArrayList<UUID>();

    public Change(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Change", "Change", 6, 50, 5, 0);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent paramPlayerMoveEvent)
    {
        if (!isEnabled())
            return;
        Player player = paramPlayerMoveEvent.getPlayer();
        PlayerStats playerStats = getCore().getPlayerStats(player);

        if (player.getAllowFlight())
            return;
        if (player.isInsideVehicle())
            return;
        if (!player.getNearbyEntities(1.0D, 1.0D, 1.0D).isEmpty())
            return;
        if (this.built.contains(player.getUniqueId()))
            return;
        int i = playerStats.getCheck(this, 0);
        int j = getThreshold();

        if (!playerStats.isOnGround() && !UtilPlayer.isOnBlock(player, 0, new Material[]{Material.CARPET}) && !UtilPlayer.isHoveringOverWater(player, 0) && player.getLocation().getBlock().getType() == Material.AIR)
        {
            if (paramPlayerMoveEvent.getFrom().getY() > paramPlayerMoveEvent.getTo().getY())
            {
                if (!this.falling.contains(player.getUniqueId()))
                {
                    this.falling.add(player.getUniqueId());
                }
            } else if (paramPlayerMoveEvent.getTo().getY() > paramPlayerMoveEvent.getFrom().getY())
            {
                if (this.falling.contains(player.getUniqueId()))
                {
                    i++;
                } else
                {
                    i--;
                }
            } else
            {
                i--;
            }
        } else
        {
            this.falling.remove(player.getUniqueId());
        }

        if (playerStats.getVelocityY() > 0.0D)
        {
            this.falling.remove(player.getUniqueId());
            i = 0;
        }

        if (i > j)
        {
            getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Went upwards unexpectedly"));
            i = 0;
            this.falling.remove(player.getUniqueId());
        }

        playerStats.setCheck(this, 0, i);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.falling.contains(player.getUniqueId())) return;
        this.falling.remove(player.getUniqueId());
    }

    @EventHandler
    public void onAttack(BlockPlaceEvent blockPlaceEvent)
    {
        if (!(blockPlaceEvent.getPlayer() instanceof Player)) return;
        Player player = blockPlaceEvent.getPlayer();
        this.built.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater((Plugin) SpyCheater.Instance, () -> {
            boolean bl = this.built.remove(player.getUniqueId());
        }, 60L);
    }
}

