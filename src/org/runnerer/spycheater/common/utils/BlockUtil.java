package org.runnerer.spycheater.common.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BlockUtil
{

    public static HashSet<Byte> blockPassSet = new HashSet();
    public static HashSet<Byte> blockAirFoliageSet = new HashSet();
    public static HashSet<Byte> fullSolid = new HashSet();
    public static HashSet<Byte> blockUseSet = new HashSet();

    public static Block getLowestBlockAt(Location location)
    {
        Block block = location.getWorld().getBlockAt((int) location.getX(), 0, (int) location.getZ());
        if (block != null)
        {
            if (!block.getType().equals((Object) Material.AIR)) return block;
        }
        block = location.getBlock();
        for (int i = (int) location.getY(); i > 0; --i)
        {
            Block block2 = location.getWorld().getBlockAt((int) location.getX(), i, (int) location.getZ());
            Block block3 = block2.getLocation().subtract(0.0, 1.0, 0.0).getBlock();
            if (block3 != null && !block3.getType().equals((Object) Material.AIR)) continue;
            block = block2;
        }
        return block;
    }

    public static boolean containsBlock(Location location, Material material)
    {
        for (int i = 0; i < 256; ++i)
        {
            Block block = location.getWorld().getBlockAt((int) location.getX(), i, (int) location.getZ());
            if (block == null || !block.getType().equals((Object) material)) continue;
            return true;
        }
        return false;
    }

    public static boolean containsBlock(Location location)
    {
        for (int i = 0; i < 256; ++i)
        {
            Block block = location.getWorld().getBlockAt((int) location.getX(), i, (int) location.getZ());
            if (block == null || block.getType().equals((Object) Material.AIR)) continue;
            return true;
        }
        return false;
    }

    public static boolean containsBlockBelow(Location location)
    {
        for (int i = 0; i < (int) location.getY(); ++i)
        {
            Block block = location.getWorld().getBlockAt((int) location.getX(), i, (int) location.getZ());
            if (block == null || block.getType().equals((Object) Material.AIR)) continue;
            return true;
        }
        return false;
    }

    public static ArrayList<Block> getBlocksAroundCenter(Location location, int n)
    {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        for (int i = location.getBlockX() - n; i <= location.getBlockX() + n; ++i)
        {
            for (int j = location.getBlockY() - n; j <= location.getBlockY() + n; ++j)
            {
                for (int k = location.getBlockZ() - n; k <= location.getBlockZ() + n; ++k)
                {
                    Location location2 = new Location(location.getWorld(), (double) i, (double) j, (double) k);
                    if (!(location2.distance(location) <= (double) n)) continue;
                    arrayList.add(location2.getBlock());
                }
            }
        }
        return arrayList;
    }

    public static Location stringToLocation(String string)
    {
        String[] arrstring = string.split(",");
        World world = Bukkit.getWorld((String) arrstring[0]);
        double d = Double.parseDouble(arrstring[1]);
        double d2 = Double.parseDouble(arrstring[2]);
        double d3 = Double.parseDouble(arrstring[3]);
        float f = Float.parseFloat(arrstring[4]);
        float f2 = Float.parseFloat(arrstring[5]);
        return new Location(world, d, d2, d3, f, f2);
    }

    public static String LocationToString(Location location)
    {
        return String.valueOf(String.valueOf(location.getWorld().getName())) + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
    }

    public static boolean isStair(Block block)
    {
        String string = block.getType().name().toLowerCase();
        if (string.contains("stair")) return true;
        if (string.contains("_step")) return true;
        if (string.equals("step")) return true;
        return false;
    }

    public static boolean isWeb(Block block)
    {
        if (block.getType() != Material.WEB) return false;
        return true;
    }

    public static boolean containsBlockType(Material[] arrmaterial, Block block)
    {
        for (Material material : arrmaterial)
        {
            if (material != block.getType()) continue;
            return true;
        }
        return false;
    }

    public static boolean isLiquid(Block block)
    {
        if (block == null) return false;
        if (block.getType() == Material.WATER) return true;
        if (block.getType() == Material.STATIONARY_WATER) return true;
        if (block.getType() == Material.LAVA) return true;
        if (block.getType() != Material.STATIONARY_LAVA) return false;
        return true;
    }

    public static boolean isSolid(Block block)
    {
        if (block == null) return false;
        if (!BlockUtil.isSolid(block.getTypeId())) return false;
        return true;
    }

    public static boolean isIce(Block block)
    {
        if (block == null) return false;
        if (block.getType() == Material.ICE) return true;
        if (block.getType() != Material.PACKED_ICE) return false;
        return true;
    }

    public static boolean isAny(Block block, Material[] arrmaterial)
    {
        for (Material material : arrmaterial)
        {
            if (!block.getType().equals((Object) material)) continue;
            return true;
        }
        return false;
    }

    public static boolean isSolid(int n)
    {
        return BlockUtil.isSolid((byte) n);
    }

    public static boolean isSolid(byte by)
    {
        if (blockPassSet.isEmpty())
        {
            blockPassSet.add((byte) 0);
            blockPassSet.add((byte) 6);
            blockPassSet.add((byte) 8);
            blockPassSet.add((byte) 9);
            blockPassSet.add((byte) 10);
            blockPassSet.add((byte) 11);
            blockPassSet.add((byte) 27);
            blockPassSet.add((byte) 28);
            blockPassSet.add((byte) 30);
            blockPassSet.add((byte) 31);
            blockPassSet.add((byte) 32);
            blockPassSet.add((byte) 37);
            blockPassSet.add((byte) 38);
            blockPassSet.add((byte) 39);
            blockPassSet.add((byte) 40);
            blockPassSet.add((byte) 50);
            blockPassSet.add((byte) 51);
            blockPassSet.add((byte) 55);
            blockPassSet.add((byte) 59);
            blockPassSet.add((byte) 63);
            blockPassSet.add((byte) 66);
            blockPassSet.add((byte) 68);
            blockPassSet.add((byte) 69);
            blockPassSet.add((byte) 70);
            blockPassSet.add((byte) 72);
            blockPassSet.add((byte) 75);
            blockPassSet.add((byte) 76);
            blockPassSet.add((byte) 77);
            blockPassSet.add((byte) 78);
            blockPassSet.add((byte) 83);
            blockPassSet.add((byte) 90);
            blockPassSet.add((byte) 104);
            blockPassSet.add((byte) 105);
            blockPassSet.add((byte) 115);
            blockPassSet.add((byte) 119);
            blockPassSet.add((byte) -124);
            blockPassSet.add((byte) -113);
            blockPassSet.add((byte) -81);
            blockPassSet.add((byte) -85);
        }
        if (!blockPassSet.contains(by)) return true;
        return false;
    }

    public static boolean airFoliage(Block block)
    {
        if (block == null) return false;
        if (!BlockUtil.airFoliage(block.getTypeId())) return false;
        return true;
    }

    public static boolean airFoliage(int n)
    {
        return BlockUtil.airFoliage((byte) n);
    }

    public static boolean airFoliage(byte by)
    {
        if (!blockAirFoliageSet.isEmpty()) return blockAirFoliageSet.contains(by);
        blockAirFoliageSet.add((byte) 0);
        blockAirFoliageSet.add((byte) 6);
        blockAirFoliageSet.add((byte) 31);
        blockAirFoliageSet.add((byte) 32);
        blockAirFoliageSet.add((byte) 37);
        blockAirFoliageSet.add((byte) 38);
        blockAirFoliageSet.add((byte) 39);
        blockAirFoliageSet.add((byte) 40);
        blockAirFoliageSet.add((byte) 51);
        blockAirFoliageSet.add((byte) 59);
        blockAirFoliageSet.add((byte) 104);
        blockAirFoliageSet.add((byte) 105);
        blockAirFoliageSet.add((byte) 115);
        blockAirFoliageSet.add((byte) -115);
        blockAirFoliageSet.add((byte) -114);
        return blockAirFoliageSet.contains(by);
    }

    public static boolean fullSolid(Block block)
    {
        if (block == null) return false;
        if (!BlockUtil.fullSolid(block.getTypeId())) return false;
        return true;
    }

    public static boolean fullSolid(int n)
    {
        return BlockUtil.fullSolid((byte) n);
    }

    public static boolean fullSolid(byte by)
    {
        if (!fullSolid.isEmpty()) return fullSolid.contains(by);
        fullSolid.add((byte) 1);
        fullSolid.add((byte) 2);
        fullSolid.add((byte) 3);
        fullSolid.add((byte) 4);
        fullSolid.add((byte) 5);
        fullSolid.add((byte) 7);
        fullSolid.add((byte) 12);
        fullSolid.add((byte) 13);
        fullSolid.add((byte) 14);
        fullSolid.add((byte) 15);
        fullSolid.add((byte) 16);
        fullSolid.add((byte) 17);
        fullSolid.add((byte) 19);
        fullSolid.add((byte) 20);
        fullSolid.add((byte) 21);
        fullSolid.add((byte) 22);
        fullSolid.add((byte) 23);
        fullSolid.add((byte) 24);
        fullSolid.add((byte) 25);
        fullSolid.add((byte) 29);
        fullSolid.add((byte) 33);
        fullSolid.add((byte) 35);
        fullSolid.add((byte) 41);
        fullSolid.add((byte) 42);
        fullSolid.add((byte) 43);
        fullSolid.add((byte) 44);
        fullSolid.add((byte) 45);
        fullSolid.add((byte) 46);
        fullSolid.add((byte) 47);
        fullSolid.add((byte) 48);
        fullSolid.add((byte) 49);
        fullSolid.add((byte) 56);
        fullSolid.add((byte) 57);
        fullSolid.add((byte) 58);
        fullSolid.add((byte) 60);
        fullSolid.add((byte) 61);
        fullSolid.add((byte) 62);
        fullSolid.add((byte) 73);
        fullSolid.add((byte) 74);
        fullSolid.add((byte) 79);
        fullSolid.add((byte) 80);
        fullSolid.add((byte) 82);
        fullSolid.add((byte) 84);
        fullSolid.add((byte) 86);
        fullSolid.add((byte) 87);
        fullSolid.add((byte) 88);
        fullSolid.add((byte) 89);
        fullSolid.add((byte) 91);
        fullSolid.add((byte) 95);
        fullSolid.add((byte) 97);
        fullSolid.add((byte) 98);
        fullSolid.add((byte) 99);
        fullSolid.add((byte) 100);
        fullSolid.add((byte) 103);
        fullSolid.add((byte) 110);
        fullSolid.add((byte) 112);
        fullSolid.add((byte) 121);
        fullSolid.add((byte) 123);
        fullSolid.add((byte) 124);
        fullSolid.add((byte) 125);
        fullSolid.add((byte) 126);
        fullSolid.add((byte) -127);
        fullSolid.add((byte) -123);
        fullSolid.add((byte) -119);
        fullSolid.add((byte) -118);
        fullSolid.add((byte) -104);
        fullSolid.add((byte) -103);
        fullSolid.add((byte) -101);
        fullSolid.add((byte) -98);
        return fullSolid.contains(by);
    }

    public static boolean usable(Block block)
    {
        if (block == null) return false;
        if (!BlockUtil.usable(block.getTypeId())) return false;
        return true;
    }

    public static boolean usable(int n)
    {
        return BlockUtil.usable((byte) n);
    }

    public static boolean usable(byte by)
    {
        if (!blockUseSet.isEmpty()) return blockUseSet.contains(by);
        blockUseSet.add((byte) 23);
        blockUseSet.add((byte) 26);
        blockUseSet.add((byte) 33);
        blockUseSet.add((byte) 47);
        blockUseSet.add((byte) 54);
        blockUseSet.add((byte) 58);
        blockUseSet.add((byte) 61);
        blockUseSet.add((byte) 62);
        blockUseSet.add((byte) 64);
        blockUseSet.add((byte) 69);
        blockUseSet.add((byte) 71);
        blockUseSet.add((byte) 77);
        blockUseSet.add((byte) 93);
        blockUseSet.add((byte) 94);
        blockUseSet.add((byte) 96);
        blockUseSet.add((byte) 107);
        blockUseSet.add((byte) 116);
        blockUseSet.add((byte) 117);
        blockUseSet.add((byte) -126);
        blockUseSet.add((byte) -111);
        blockUseSet.add((byte) -110);
        blockUseSet.add((byte) -102);
        blockUseSet.add((byte) -98);
        return blockUseSet.contains(by);
    }

    public static HashMap<Block, Double> getInRadius(Location location, double d)
    {
        return BlockUtil.getInRadius(location, d, 999.0);
    }

    public static HashMap<Block, Double> getInRadius(Location location, double d, double d2)
    {
        HashMap<Block, Double> hashMap = new HashMap<Block, Double>();
        int n = (int) d + 1;
        for (int i = -n; i <= n; ++i)
        {
            for (int j = -n; j <= n; ++j)
            {
                for (int k = -n; k <= n; ++k)
                {
                    double d3;
                    Block block;
                    if (!((double) Math.abs(k) <= d2) || !((d3 = UtilMath.offset(location, (block = location.getWorld().getBlockAt((int) (location.getX() + (double) i), (int) (location.getY() + (double) k), (int) (location.getZ() + (double) j))).getLocation().add(0.5, 0.5, 0.5))) <= d))
                        continue;
                    hashMap.put(block, 1.0 - d3 / d);
                }
            }
        }
        return hashMap;
    }

    public static HashMap<Block, Double> getInRadius(Block block, double d)
    {
        HashMap<Block, Double> hashMap = new HashMap<Block, Double>();
        int n = (int) d + 1;
        for (int i = -n; i <= n; ++i)
        {
            for (int j = -n; j <= n; ++j)
            {
                for (int k = -n; k <= n; ++k)
                {
                    Block block2 = block.getRelative(i, k, j);
                    double d2 = UtilMath.offset(block.getLocation(), block2.getLocation());
                    if (!(d2 <= d)) continue;
                    hashMap.put(block2, 1.0 - d2 / d);
                }
            }
        }
        return hashMap;
    }

    public static boolean isBlock(ItemStack itemStack)
    {
        if (itemStack == null) return false;
        if (itemStack.getTypeId() <= 0) return false;
        if (itemStack.getTypeId() >= 256) return false;
        return true;
    }

    public static Block getHighest(Location location)
    {
        return BlockUtil.getHighest(location, null);
    }

    public static Block getHighest(Location location, HashSet<Material> hashSet)
    {
        location.setY(0.0);
        for (int i = 0; i < 256; ++i)
        {
            location.setY((double) (256 - i));
            if (!BlockUtil.isSolid(location.getBlock())) continue;
            return location.getBlock().getRelative(BlockFace.UP);
        }
        return location.getBlock().getRelative(BlockFace.UP);
    }

    public static boolean isInAir(Player player)
    {
        boolean bl = false;
        for (Block block : BlockUtil.getSurrounding(player.getLocation().getBlock(), true))
        {
            if (block.getType() == Material.AIR) continue;
            return true;
        }
        return bl;
    }

    public static ArrayList<Block> getSurrounding(Block block, boolean bl)
    {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        if (!bl)
        {
            arrayList.add(block.getRelative(BlockFace.UP));
            arrayList.add(block.getRelative(BlockFace.DOWN));
            arrayList.add(block.getRelative(BlockFace.NORTH));
            arrayList.add(block.getRelative(BlockFace.SOUTH));
            arrayList.add(block.getRelative(BlockFace.EAST));
            arrayList.add(block.getRelative(BlockFace.WEST));
            return arrayList;
        }
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                for (int k = -1; k <= 1; ++k)
                {
                    if (i == 0 && j == 0 && k == 0) continue;
                    arrayList.add(block.getRelative(i, j, k));
                }
            }
        }
        return arrayList;
    }

    public static ArrayList<Block> getSurroundingXZ(Block block)
    {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        arrayList.add(block.getRelative(BlockFace.NORTH));
        arrayList.add(block.getRelative(BlockFace.NORTH_EAST));
        arrayList.add(block.getRelative(BlockFace.NORTH_WEST));
        arrayList.add(block.getRelative(BlockFace.SOUTH));
        arrayList.add(block.getRelative(BlockFace.SOUTH_EAST));
        arrayList.add(block.getRelative(BlockFace.SOUTH_WEST));
        arrayList.add(block.getRelative(BlockFace.EAST));
        arrayList.add(block.getRelative(BlockFace.WEST));
        return arrayList;
    }

    public static String serializeLocation(Location location)
    {
        int n = (int) location.getX();
        int n2 = (int) location.getY();
        int n3 = (int) location.getZ();
        int n4 = (int) location.getPitch();
        int n5 = (int) location.getYaw();
        return new String(String.valueOf(String.valueOf(location.getWorld().getName())) + "," + n + "," + n2 + "," + n3 + "," + n4 + "," + n5);
    }

    public static Location deserializeLocation(String string)
    {
        if (string == null)
        {
            return null;
        }
        String[] arrstring = string.split(",");
        World world = Bukkit.getServer().getWorld(arrstring[0]);
        Double d = Double.parseDouble(arrstring[1]);
        Double d2 = Double.parseDouble(arrstring[2]);
        Double d3 = Double.parseDouble(arrstring[3]);
        Float f = Float.valueOf(Float.parseFloat(arrstring[4]));
        Float f2 = Float.valueOf(Float.parseFloat(arrstring[5]));
        Location location = new Location(world, d.doubleValue(), d2.doubleValue(), d3.doubleValue());
        location.setPitch(f.floatValue());
        location.setYaw(f2.floatValue());
        return location;
    }

    public static boolean isVisible(Block block)
    {
        for (Block block2 : BlockUtil.getSurrounding(block, false))
        {
            if (block2.getType().isOccluding()) continue;
            return true;
        }
        return false;
    }
}

