package org.runnerer.spycheater.checks.movement.phase;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.material.Directional;
import org.bukkit.material.Door;
import org.bukkit.material.Gate;
import org.bukkit.material.TrapDoor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PhaseTask
        extends BukkitRunnable
{

    private final Map<UUID, Location> validLocations = new HashMap<UUID, Location>();
    private PhaseUtil phaseUtil = new PhaseUtil();
    private PhaseA phase;
    private Set<UUID> teleported = new HashSet<UUID>();

    public PhaseTask(PhaseA phaseA)
    {
        this.phase = phaseA;
        this.phaseUtil.load(phaseA.config);
    }

    public void addTeleportedPlayer(UUID uUID)
    {
        this.teleported.add(uUID);
    }

    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            UUID uUID = player.getUniqueId();
            Location location = this.validLocations.containsKey(uUID) ? this.validLocations.get(uUID) : player.getLocation();
            Location location2 = player.getLocation();
            if (location.getWorld() == location2.getWorld() && !this.teleported.contains(uUID) && location.distance(location2) > 10.0)
            {
                if (player.getAllowFlight() || this.phase.horse != null && this.phase.horse.containsKey(player.getUniqueId().toString()) && this.phase.horse.get(player.getUniqueId().toString()) < System.currentTimeMillis())
                    continue;
                player.teleport(this.validLocations.get(uUID), PlayerTeleportEvent.TeleportCause.PLUGIN);
                continue;
            }
            if (this.isValidMove(uUID, location, location2))
            {
                this.validLocations.put(uUID, location2);
                continue;
            }
            if (!this.validLocations.containsKey(uUID) || player.getAllowFlight() || this.phase.horse != null && this.phase.horse.containsKey(player.getUniqueId().toString()) && this.phase.horse.get(player.getUniqueId().toString()) < System.currentTimeMillis())
                continue;
            player.teleport(this.validLocations.get(uUID), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        }
    }

    private boolean isValidMove(UUID uUID, Location location, Location location2)
    {
        if (location.getWorld() != location2.getWorld())
        {
            return true;
        }
        if (this.teleported.remove(uUID))
        {
            return true;
        }
        int n = Math.max(location.getBlockX(), location2.getBlockX());
        int n2 = Math.min(location.getBlockX(), location2.getBlockX());
        int n3 = Math.max(location.getBlockY(), location2.getBlockY()) + 1;
        int n4 = Math.min(location.getBlockY(), location2.getBlockY());
        int n5 = Math.max(location.getBlockZ(), location2.getBlockZ());
        int n6 = Math.min(location.getBlockZ(), location2.getBlockZ());
        if (n3 > 256)
        {
            n = 256;
        }
        if (n4 > 256)
        {
            n4 = 256;
        }
        for (int i = n2; i <= n; ++i)
        {
            for (int j = n6; j <= n5; ++j)
            {
                for (int k = n4; k <= n3; ++k)
                {
                    Block block = location.getWorld().getBlockAt(i, k, j);
                    if (k == n4 && location.getBlockY() != location2.getBlockY() || !this.hasPhased(block, location, location2))
                        continue;
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasPhased(Block paramBlock, Location paramLocation1, Location paramLocation2)
    {
        if (this.phaseUtil.getPassable().contains(paramBlock.getType()))
        {
            return false;
        }

        double d1 = Math.max(paramLocation1.getX(), paramLocation2.getX());
        double d2 = Math.min(paramLocation1.getX(), paramLocation2.getX());
        double d3 = Math.max(paramLocation1.getY(), paramLocation2.getY()) + 1.8D;
        double d4 = Math.min(paramLocation1.getY(), paramLocation2.getY());
        double d5 = Math.max(paramLocation1.getZ(), paramLocation2.getZ());
        double d6 = Math.min(paramLocation1.getZ(), paramLocation2.getZ());
        double d7 = (paramBlock.getLocation().getBlockX() + 1);
        double d8 = paramBlock.getLocation().getBlockX();
        double d9 = (paramBlock.getLocation().getBlockY() + 2);
        double d10 = paramBlock.getLocation().getBlockY();
        double d11 = (paramBlock.getLocation().getBlockZ() + 1);
        double d12 = paramBlock.getLocation().getBlockZ();

        if (d10 > d4)
        {
            d9--;
        }

        if (this.phaseUtil.getDoors().contains(paramBlock.getType()))
        {
            Door door = (Door) paramBlock.getType().getNewData(paramBlock.getData());

            if (door.isTopHalf())
            {
                return false;
            }

            BlockFace blockFace = door.getFacing();

            if (door.isOpen())
            {
                Block block = paramBlock.getRelative(BlockFace.UP);

                if (!this.phaseUtil.getDoors().contains(block.getType()))
                {
                    return false;
                }

                boolean bool = ((block.getData() * (byte) 1 == 1) ? true : false);

                if (blockFace == BlockFace.NORTH)
                {
                    blockFace = bool ? BlockFace.WEST : BlockFace.EAST;
                } else if (blockFace == BlockFace.EAST)
                {
                    blockFace = bool ? BlockFace.NORTH : BlockFace.SOUTH;
                } else if (blockFace == BlockFace.SOUTH)
                {
                    blockFace = bool ? BlockFace.EAST : BlockFace.WEST;
                } else
                {
                    blockFace = bool ? BlockFace.SOUTH : BlockFace.NORTH;
                }
            }

            if (blockFace == BlockFace.WEST)
            {
                d7 -= 0.8D;
            }

            if (blockFace == BlockFace.EAST)
            {
                d8 += 0.8D;
            }

            if (blockFace == BlockFace.NORTH)
            {
                d11 -= 0.8D;
            }

            if (blockFace == BlockFace.SOUTH)
            {
                d12 += 0.8D;
            }
        } else if (this.phaseUtil.getGates().contains(paramBlock.getType()))
        {
            if (((Gate) paramBlock.getType().getNewData(paramBlock.getData())).isOpen())
            {
                return false;
            }

            BlockFace blockFace = ((Directional) paramBlock.getType().getNewData(paramBlock.getData())).getFacing();

            if (blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH)
            {
                d7 -= 0.2D;
                d8 += 0.2D;
            } else
            {
                d11 -= 0.2D;
                d12 += 0.2D;
            }
        } else if (this.phaseUtil.getTrapdoors().contains(paramBlock.getType()))
        {
            TrapDoor trapDoor = (TrapDoor) paramBlock.getType().getNewData(paramBlock.getData());

            if (trapDoor.isOpen())
            {
                return false;
            }

            if (trapDoor.isInverted())
            {
                d10 += 0.85D;
            } else
            {
                d9 -= ((d10 > d4) ? 0.85D : 1.85D);
            }
        } else if (this.phaseUtil.getFences().contains(paramBlock.getType()))
        {
            d7 -= 0.2D;
            d8 += 0.2D;
            d11 -= 0.2D;
            d12 += 0.2D;

            if ((d1 > d7 && d2 > d7 && d5 > d11 && d6 > d11) || (
                    d1 < d8 && d2 < d8 && d5 > d11 && d6 > d11) || (
                    d1 > d7 && d2 > d7 && d5 < d12 && d6 < d12) || (
                    d1 < d8 && d2 < d8 && d5 < d12 && d6 < d12))
            {
                return false;
            }

            if (paramBlock.getRelative(BlockFace.EAST).getType() == paramBlock.getType())
            {
                d7 += 0.2D;
            }

            if (paramBlock.getRelative(BlockFace.WEST).getType() == paramBlock.getType())
            {
                d8 -= 0.2D;
            }

            if (paramBlock.getRelative(BlockFace.SOUTH).getType() == paramBlock.getType())
            {
                d11 += 0.2D;
            }

            if (paramBlock.getRelative(BlockFace.NORTH).getType() == paramBlock.getType())
            {
                d12 -= 0.2D;
            }
        }

        boolean bool1 = (paramLocation1.getX() < paramLocation2.getX()) ? true : false;
        boolean bool2 = (paramLocation1.getY() < paramLocation2.getY()) ? true : false;
        boolean bool3 = (paramLocation1.getZ() < paramLocation2.getZ()) ? true : false;

        return
                !((d2 == d1 || d4 > d9 || d3 < d10 || d6 > d11 ||
                        d5 < d12 || ((
                        !bool1 || d2 > d8 || d1 < d8) && (
                        bool1 || d2 > d7 || d1 < d7))) && (
                        d4 == d3 || d2 > d7 || d1 < d8 ||
                                d6 > d11 || d5 < d12 || ((
                                !bool2 || d4 > d10 || d3 < d10) && (
                                bool2 || d4 > d9 || d3 < d9))) && (
                        d6 == d5 || d2 > d7 || d1 < d8 ||
                                d4 > d9 || d3 < d10 || ((
                                !bool3 || d6 > d12 || d5 < d12) && (
                                bool3 || d6 > d11 || d5 < d11))));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.validLocations.containsKey(player.getUniqueId()))
        {
            this.validLocations.remove(player.getUniqueId());
        }
        if (!this.teleported.contains(player.getUniqueId())) return;
        this.teleported.remove(player.getUniqueId());
    }
}

