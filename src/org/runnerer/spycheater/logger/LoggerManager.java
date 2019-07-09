package org.runnerer.spycheater.logger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.events.PacketPlayerEvent;
import org.runnerer.spycheater.player.PlayerStats;
import org.spigotmc.event.entity.EntityMountEvent;

import java.io.File;
import java.io.IOException;

public class LoggerManager
        implements Listener
{

    private SpyCheater Core;

    public LoggerManager(SpyCheater antiCheat)
    {
        this.Core = antiCheat;
        this.Core.RegisterListener(this);
    }

    @EventHandler
    public void PacketPlayer(PacketPlayerEvent packetPlayerEvent)
    {
        Player player = packetPlayerEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        if (playerStats.getLastPlayerPacketDiff() > 200L)
        {
            playerStats.setLastDelayedPacket(System.currentTimeMillis());
        }
        playerStats.setLastPlayerPacket(System.currentTimeMillis());
    }

    @EventHandler
    public void Mount(EntityMountEvent entityMountEvent)
    {
        if (!(entityMountEvent.getEntity() instanceof Player))
        {
            return;
        }
        Player player = (Player) entityMountEvent.getEntity();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        playerStats.setLastMount(System.currentTimeMillis());
    }

    @EventHandler
    public void WorldChange(PlayerChangedWorldEvent playerChangedWorldEvent)
    {
        Player player = playerChangedWorldEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        playerStats.setLastWorldChange(System.currentTimeMillis());
    }

    @EventHandler
    public void Join(PlayerJoinEvent playerJoinEvent)
    {
        Player player = playerJoinEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        File file = new File(SpyCheater.Instance.getDataFolder() + "/logs/" + player.getUniqueId() + ".txt");
        try
        {
            file.createNewFile();
        }
        catch (IOException iOException)
        {

        }
        playerStats.setLastWorldChange(System.currentTimeMillis());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent playerMoveEvent)
    {
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        if (UtilPlayer.isOnGround(playerMoveEvent.getTo(), 0) || UtilPlayer.isOnGround(playerMoveEvent.getTo(), 1))
        {
            if (!playerStats.isOnGround())
            {
                playerStats.setLastGround(null);
            }
            playerStats.setOnGround(true);
            playerStats.setLastGroundTime(System.currentTimeMillis());
        } else
        {
            if (playerStats.isOnGround())
            {
                playerStats.setLastGround(player.getLocation());
            }
            playerStats.setOnGround(false);
            playerStats.setLastBunnyTime(System.currentTimeMillis());
        }
        if (playerMoveEvent.getFrom().getY() != playerMoveEvent.getTo().getY() && playerStats.getVelocityY() > 0.0)
        {
            playerStats.setVelocityY(playerStats.getVelocityY() - 1.0);
        }
        if (playerMoveEvent.getFrom().getX() == playerMoveEvent.getTo().getX())
        {
            if (playerMoveEvent.getFrom().getZ() == playerMoveEvent.getTo().getZ()) return;
        }
        if (!(playerStats.getVelocityXZ() > 0.0)) return;
        playerStats.setVelocityXZ(playerStats.getVelocityXZ() - 1.0);
    }

    @EventHandler
    public void Velocity(PlayerVelocityEvent playerVelocityEvent)
    {
        Player player = playerVelocityEvent.getPlayer();
        PlayerStats playerStats = this.Core.getPlayerStats(player);
        Vector vector = playerVelocityEvent.getVelocity();
        double d = Math.abs(vector.getX());
        double d2 = Math.abs(vector.getY());
        double d3 = Math.abs(vector.getZ());
        if (player.getLastDamageCause() != null)
        {
            if (player.getLastDamageCause().getCause().ordinal() != 16) return;
        }
        if (d2 > 0.0)
        {
            double d4 = (int) (Math.pow(d2 + 2.0, 2.0) * 5.0);
            playerStats.setVelocityY(d4);
        }
        if (d > 0.0 || d3 > 0.0)
        {
            int n = (int) (((d + d3) / 2.0 + 2.0) * 10.0);
            playerStats.setVelocityXZ(n);
        }
        playerStats.setVelocityTime(System.currentTimeMillis());
    }
}

