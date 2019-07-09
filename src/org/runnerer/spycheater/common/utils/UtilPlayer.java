package org.runnerer.spycheater.common.utils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class UtilPlayer
{

    public static void clear(Player player)
    {
        player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().clear();
        player.setFoodLevel(20);
        player.setSaturation(3.0f);
        player.setExhaustion(0.0f);
        player.setHealth(20);
        player.setFireTicks(0);
        player.setFallDistance(0.0f);
        player.setLevel(0);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.updateInventory();
        for (PotionEffect potionEffect : player.getActivePotionEffects())
        {
            player.removePotionEffect(potionEffect.getType());
        }
    }

    public static Location getEyeLocation(Player player)
    {
        Location location = player.getLocation();
        location.setY(location.getY() + player.getEyeHeight());
        return location;
    }

    public static boolean isOnClimbable(Player player)
    {
        for (Block block : BlockUtil.getSurrounding(player.getLocation().getBlock(), false))
        {
            if (block.getType() == Material.LADDER) return true;
            if (block.getType() != Material.VINE) continue;
            return true;
        }
        if (player.getLocation().getBlock().getType() == Material.LADDER) return true;
        if (player.getLocation().getBlock().getType() == Material.VINE) return true;
        return false;
    }

    public static boolean isOnGround(Location location, int n)
    {
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = UtilMath.getFraction(d) > 0.0 ? Math.abs(UtilMath.getFraction(d)) : 1.0 - Math.abs(UtilMath.getFraction(d));
        double d4 = UtilMath.getFraction(d2) > 0.0 ? Math.abs(UtilMath.getFraction(d2)) : 1.0 - Math.abs(UtilMath.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        World world = location.getWorld();
        if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4)))
        {
            return true;
        }
        if (d3 < 0.3)
        {
            if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4)))
            {
                return true;
            }
            if (d4 < 0.3)
            {
                if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4 - 1)))
                {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 - 1)))
                {
                    return true;
                }
                if (!BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4 - 1))) return false;
                return true;
            }
            if (!(d4 > 0.7)) return false;
            if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4 + 1)))
            {
                return true;
            }
            if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 + 1)))
            {
                return true;
            }
            if (!BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4 + 1))) return false;
            return true;
        }
        if (d3 > 0.7)
        {
            if (BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4)))
            {
                return true;
            }
            if (d4 < 0.3)
            {
                if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4 - 1)))
                {
                    return true;
                }
                if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 - 1)))
                {
                    return true;
                }
                if (!BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4 - 1))) return false;
                return true;
            }
            if (!(d4 > 0.7)) return false;
            if (BlockUtil.isSolid(world.getBlockAt(n2 - 1, n3, n4 + 1)))
            {
                return true;
            }
            if (BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 + 1)))
            {
                return true;
            }
            if (!BlockUtil.isSolid(world.getBlockAt(n2 + 1, n3, n4 + 1))) return false;
            return true;
        }
        if (d4 < 0.3)
        {
            if (!BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 - 1))) return false;
            return true;
        }
        if (!(d4 > 0.7)) return false;
        if (!BlockUtil.isSolid(world.getBlockAt(n2, n3, n4 + 1))) return false;
        return true;
    }

    public static boolean isOnGround(Player player, int n)
    {
        return UtilPlayer.isOnGround(player.getLocation(), n);
    }

    public static boolean isOnBlock(Location location, int n, Material[] arrmaterial)
    {
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = UtilMath.getFraction(d) > 0.0 ? Math.abs(UtilMath.getFraction(d)) : 1.0 - Math.abs(UtilMath.getFraction(d));
        double d4 = UtilMath.getFraction(d2) > 0.0 ? Math.abs(UtilMath.getFraction(d2)) : 1.0 - Math.abs(UtilMath.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        World world = location.getWorld();
        if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4)))
        {
            return true;
        }
        if (d3 < 0.3)
        {
            if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4)))
            {
                return true;
            }
            if (d4 < 0.3)
            {
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 - 1)))
                {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1)))
                {
                    return true;
                }
                if (!BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 - 1))) return false;
                return true;
            }
            if (!(d4 > 0.7)) return false;
            if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 + 1)))
            {
                return true;
            }
            if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1)))
            {
                return true;
            }
            if (!BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 + 1))) return false;
            return true;
        }
        if (d3 > 0.7)
        {
            if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4)))
            {
                return true;
            }
            if (d4 < 0.3)
            {
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 - 1)))
                {
                    return true;
                }
                if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1)))
                {
                    return true;
                }
                if (!BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 - 1))) return false;
                return true;
            }
            if (!(d4 > 0.7)) return false;
            if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 - 1, n3, n4 + 1)))
            {
                return true;
            }
            if (BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1)))
            {
                return true;
            }
            if (!BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2 + 1, n3, n4 + 1))) return false;
            return true;
        }
        if (d4 < 0.3)
        {
            if (!BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 - 1))) return false;
            return true;
        }
        if (!(d4 > 0.7)) return false;
        if (!BlockUtil.containsBlockType(arrmaterial, world.getBlockAt(n2, n3, n4 + 1))) return false;
        return true;
    }

    public static boolean isOnBlock(Player player, int n, Material[] arrmaterial)
    {
        return UtilPlayer.isOnBlock(player.getLocation(), n, arrmaterial);
    }

    public static boolean isHoveringOverWater(Location location, int n)
    {
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = UtilMath.getFraction(d) > 0.0 ? Math.abs(UtilMath.getFraction(d)) : 1.0 - Math.abs(UtilMath.getFraction(d));
        double d4 = UtilMath.getFraction(d2) > 0.0 ? Math.abs(UtilMath.getFraction(d2)) : 1.0 - Math.abs(UtilMath.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        World world = location.getWorld();
        if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4)))
        {
            return true;
        }
        if (d3 < 0.3)
        {
            if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4)))
            {
                return true;
            }
            if (d4 < 0.3)
            {
                if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4 - 1)))
                {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 - 1)))
                {
                    return true;
                }
                if (!BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4 - 1))) return false;
                return true;
            }
            if (!(d4 > 0.7)) return false;
            if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4 + 1)))
            {
                return true;
            }
            if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 + 1)))
            {
                return true;
            }
            if (!BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4 + 1))) return false;
            return true;
        }
        if (d3 > 0.7)
        {
            if (BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4)))
            {
                return true;
            }
            if (d4 < 0.3)
            {
                if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4 - 1)))
                {
                    return true;
                }
                if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 - 1)))
                {
                    return true;
                }
                if (!BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4 - 1))) return false;
                return true;
            }
            if (!(d4 > 0.7)) return false;
            if (BlockUtil.isLiquid(world.getBlockAt(n2 - 1, n3, n4 + 1)))
            {
                return true;
            }
            if (BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 + 1)))
            {
                return true;
            }
            if (!BlockUtil.isLiquid(world.getBlockAt(n2 + 1, n3, n4 + 1))) return false;
            return true;
        }
        if (d4 < 0.3)
        {
            if (!BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 - 1))) return false;
            return true;
        }
        if (!(d4 > 0.7)) return false;
        if (!BlockUtil.isLiquid(world.getBlockAt(n2, n3, n4 + 1))) return false;
        return true;
    }

    public static boolean isHoveringOverWater(Player player, int n)
    {
        return UtilPlayer.isHoveringOverWater(player.getLocation(), n);
    }

    public static boolean isOnStairs(Location location, int n)
    {
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = UtilMath.getFraction(d) > 0.0 ? Math.abs(UtilMath.getFraction(d)) : 1.0 - Math.abs(UtilMath.getFraction(d));
        double d4 = UtilMath.getFraction(d2) > 0.0 ? Math.abs(UtilMath.getFraction(d2)) : 1.0 - Math.abs(UtilMath.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        World world = location.getWorld();
        if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4)))
        {
            return true;
        }
        if (d3 < 0.3)
        {
            if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4)))
            {
                return true;
            }
            if (d4 < 0.3)
            {
                if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4 - 1)))
                {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4 - 1)))
                {
                    return true;
                }
                if (!BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4 - 1))) return false;
                return true;
            }
            if (!(d4 > 0.7)) return false;
            if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4 + 1)))
            {
                return true;
            }
            if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4 + 1)))
            {
                return true;
            }
            if (!BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4 + 1))) return false;
            return true;
        }
        if (d3 > 0.7)
        {
            if (BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4)))
            {
                return true;
            }
            if (d4 < 0.3)
            {
                if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4 - 1)))
                {
                    return true;
                }
                if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4 - 1)))
                {
                    return true;
                }
                if (!BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4 - 1))) return false;
                return true;
            }
            if (!(d4 > 0.7)) return false;
            if (BlockUtil.isStair(world.getBlockAt(n2 - 1, n3, n4 + 1)))
            {
                return true;
            }
            if (BlockUtil.isStair(world.getBlockAt(n2, n3, n4 + 1)))
            {
                return true;
            }
            if (!BlockUtil.isStair(world.getBlockAt(n2 + 1, n3, n4 + 1))) return false;
            return true;
        }
        if (d4 < 0.3)
        {
            if (!BlockUtil.isStair(world.getBlockAt(n2, n3, n4 - 1))) return false;
            return true;
        }
        if (!(d4 > 0.7)) return false;
        if (!BlockUtil.isStair(world.getBlockAt(n2, n3, n4 + 1))) return false;
        return true;
    }

    public static boolean isOnStairs(Player player, int n)
    {
        return UtilPlayer.isOnStairs(player.getLocation(), n);
    }

    public static List<Entity> getNearbyRidables(Location location, double d)
    {
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        for (Entity entity : location.getWorld().getEntities())
        {
            if (!entity.getType().equals((Object) EntityType.HORSE) && !entity.getType().equals((Object) EntityType.BOAT) || entity.getLocation().distance(location) > d)
                continue;
            arrayList.add(entity);
        }
        return arrayList;
    }
}

