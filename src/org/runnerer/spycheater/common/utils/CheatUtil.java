package org.runnerer.spycheater.common.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CheatUtil
{

    public static final String SPY_METADATA;
    private static final List<Material> INSTANT_BREAK;
    private static final List<Material> FOOD;
    private static final List<Material> INTERACTABLE;
    private static final Map<Material, Material> COMBO;

    static
    {
        SPY_METADATA = "ac-spydata";
        INSTANT_BREAK = new ArrayList<Material>();
        FOOD = new ArrayList<Material>();
        INTERACTABLE = new ArrayList<Material>();
        COMBO = new HashMap<Material, Material>();
        INSTANT_BREAK.add(Material.RED_MUSHROOM);
        INSTANT_BREAK.add(Material.RED_ROSE);
        INSTANT_BREAK.add(Material.BROWN_MUSHROOM);
        INSTANT_BREAK.add(Material.YELLOW_FLOWER);
        INSTANT_BREAK.add(Material.REDSTONE);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_OFF);
        INSTANT_BREAK.add(Material.REDSTONE_TORCH_ON);
        INSTANT_BREAK.add(Material.REDSTONE_WIRE);
        INSTANT_BREAK.add(Material.LONG_GRASS);
        INSTANT_BREAK.add(Material.PAINTING);
        INSTANT_BREAK.add(Material.WHEAT);
        INSTANT_BREAK.add(Material.SUGAR_CANE);
        INSTANT_BREAK.add(Material.SUGAR_CANE_BLOCK);
        INSTANT_BREAK.add(Material.DIODE);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_OFF);
        INSTANT_BREAK.add(Material.DIODE_BLOCK_ON);
        INSTANT_BREAK.add(Material.SAPLING);
        INSTANT_BREAK.add(Material.TORCH);
        INSTANT_BREAK.add(Material.CROPS);
        INSTANT_BREAK.add(Material.SNOW);
        INSTANT_BREAK.add(Material.TNT);
        INSTANT_BREAK.add(Material.POTATO);
        INSTANT_BREAK.add(Material.CARROT);
        INTERACTABLE.add(Material.STONE_BUTTON);
        INTERACTABLE.add(Material.LEVER);
        INTERACTABLE.add(Material.CHEST);
        FOOD.add(Material.COOKED_BEEF);
        FOOD.add(Material.COOKED_CHICKEN);
        FOOD.add(Material.COOKED_FISH);
        FOOD.add(Material.GRILLED_PORK);
        FOOD.add(Material.PORK);
        FOOD.add(Material.MUSHROOM_SOUP);
        FOOD.add(Material.RAW_BEEF);
        FOOD.add(Material.RAW_CHICKEN);
        FOOD.add(Material.RAW_FISH);
        FOOD.add(Material.APPLE);
        FOOD.add(Material.GOLDEN_APPLE);
        FOOD.add(Material.MELON);
        FOOD.add(Material.COOKIE);
        FOOD.add(Material.BREAD);
        FOOD.add(Material.SPIDER_EYE);
        FOOD.add(Material.ROTTEN_FLESH);
        FOOD.add(Material.POTATO_ITEM);
        COMBO.put(Material.SHEARS, Material.WOOL);
        COMBO.put(Material.IRON_SWORD, Material.WEB);
        COMBO.put(Material.DIAMOND_SWORD, Material.WEB);
        COMBO.put(Material.STONE_SWORD, Material.WEB);
        COMBO.put(Material.WOOD_SWORD, Material.WEB);
    }

    public static boolean isSafeSetbackLocation(Player player)
    {
        if (!CheatUtil.isInWeb(player) && !CheatUtil.isInWater(player))
        {
            if (CheatUtil.cantStandAtSingle(player.getLocation().getBlock())) return false;
        }
        if (player.getEyeLocation().getBlock().getType().isSolid()) return false;
        return true;
    }

    public static double getXDelta(Location location, Location location2)
    {
        return Math.abs(location.getX() - location2.getX());
    }

    public static double getZDelta(Location location, Location location2)
    {
        return Math.abs(location.getZ() - location2.getZ());
    }

    public static double getDistance3D(Location location, Location location2)
    {
        double d = 0.0;
        double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        double d3 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        double d4 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        double d5 = Math.sqrt(d2 + d3 + d4);
        return Math.abs(d5);
    }

    public static double getVerticalDistance(Location location, Location location2)
    {
        double d = 0.0;
        double d2 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        double d3 = Math.sqrt(d2);
        return Math.abs(d3);
    }

    public static double getHorizontalDistance(Location location, Location location2)
    {
        double d = 0.0;
        double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        double d4 = Math.sqrt(d2 + d3);
        return Math.abs(d4);
    }

    public static boolean cantStandAtBetter(Block block)
    {
        Block block2 = block.getRelative(BlockFace.DOWN);
        boolean bl = block2.getType() == Material.AIR;
        boolean bl2 = block2.getRelative(BlockFace.NORTH).getType() == Material.AIR;
        boolean bl3 = block2.getRelative(BlockFace.EAST).getType() == Material.AIR;
        boolean bl4 = block2.getRelative(BlockFace.SOUTH).getType() == Material.AIR;
        boolean bl5 = block2.getRelative(BlockFace.WEST).getType() == Material.AIR;
        boolean bl6 = block2.getRelative(BlockFace.NORTH_EAST).getType() == Material.AIR;
        boolean bl7 = block2.getRelative(BlockFace.NORTH_WEST).getType() == Material.AIR;
        boolean bl8 = block2.getRelative(BlockFace.SOUTH_EAST).getType() == Material.AIR;
        boolean bl9 = block2.getRelative(BlockFace.SOUTH_WEST).getType() == Material.AIR;
        boolean bl10 = block2.getRelative(BlockFace.DOWN).getType() == Material.AIR || block2.getRelative(BlockFace.DOWN).getType() == Material.WATER || block2.getRelative(BlockFace.DOWN).getType() == Material.LAVA;
        if (!bl) return false;
        if (!bl2) return false;
        if (!bl3) return false;
        if (!bl4) return false;
        if (!bl5) return false;
        if (!bl6) return false;
        if (!bl8) return false;
        if (!bl7) return false;
        if (!bl9) return false;
        if (!bl10) return false;
        return true;
    }

    public static boolean cantStandAtSingle(Block block)
    {
        Block block2 = block.getRelative(BlockFace.DOWN);
        if (block2.getType() != Material.AIR) return false;
        return true;
    }

    public static boolean cantStandAtWater(Block block)
    {
        Block block2 = block.getRelative(BlockFace.DOWN);
        boolean bl = block.getType() == Material.AIR;
        boolean bl2 = block2.getRelative(BlockFace.NORTH).getType() == Material.WATER || block2.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean bl3 = block2.getRelative(BlockFace.SOUTH).getType() == Material.WATER || block2.getRelative(BlockFace.SOUTH).getType() == Material.STATIONARY_WATER;
        boolean bl4 = block2.getRelative(BlockFace.EAST).getType() == Material.WATER || block2.getRelative(BlockFace.EAST).getType() == Material.STATIONARY_WATER;
        boolean bl5 = block2.getRelative(BlockFace.WEST).getType() == Material.WATER || block2.getRelative(BlockFace.WEST).getType() == Material.STATIONARY_WATER;
        boolean bl6 = block2.getRelative(BlockFace.NORTH_EAST).getType() == Material.WATER || block2.getRelative(BlockFace.NORTH_EAST).getType() == Material.STATIONARY_WATER;
        boolean bl7 = block2.getRelative(BlockFace.NORTH_WEST).getType() == Material.WATER || block2.getRelative(BlockFace.NORTH_WEST).getType() == Material.STATIONARY_WATER;
        boolean bl8 = block2.getRelative(BlockFace.SOUTH_EAST).getType() == Material.WATER || block2.getRelative(BlockFace.NORTH).getType() == Material.STATIONARY_WATER;
        boolean bl9 = block2.getRelative(BlockFace.SOUTH_WEST).getType() == Material.WATER || block2.getRelative(BlockFace.SOUTH_WEST).getType() == Material.STATIONARY_WATER;
        if (!bl2) return false;
        if (!bl3) return false;
        if (!bl4) return false;
        if (!bl5) return false;
        if (!bl6) return false;
        if (!bl7) return false;
        if (!bl8) return false;
        if (!bl9) return false;
        if (!bl) return false;
        return true;
    }

    public static boolean canStandWithin(Block block)
    {
        boolean bl = block.getType() == Material.SAND;
        boolean bl2 = block.getType() == Material.GRAVEL;
        boolean bl3 = block.getType().isSolid() && !block.getType().name().toLowerCase().contains("door") && !block.getType().name().toLowerCase().contains("fence") && !block.getType().name().toLowerCase().contains("bars") && !block.getType().name().toLowerCase().contains("sign");
        if (bl) return false;
        if (bl2) return false;
        if (bl3) return false;
        return true;
    }

    public static Vector getRotation(Location location, Location location2)
    {
        double d = location2.getX() - location.getX();
        double d2 = location2.getY() - location.getY();
        double d3 = location2.getZ() - location.getZ();
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float) (Math.atan2(d3, d) * 180.0 / 3.141592653589793) - 90.0f;
        float f2 = (float) (-(Math.atan2(d2, d4) * 180.0 / 3.141592653589793));
        return new Vector(f, f2, 0.0f);
    }

    public static double clamp180(double d)
    {
        if ((d %= 360.0) >= 180.0)
        {
            d -= 360.0;
        }
        if (!(d < -180.0)) return d;
        d += 360.0;
        return d;
    }

    public static int getLevelForEnchantment(Player player, String string)
    {
        try
        {
            Enchantment enchantment = Enchantment.getByName((String) string);
            for (ItemStack itemStack : player.getInventory().getArmorContents())
            {
                if (!itemStack.containsEnchantment(enchantment)) continue;
                return itemStack.getEnchantmentLevel(enchantment);
            }
            return -1;
        }
        catch (Exception exception)
        {
            return -1;
        }
    }

    public static boolean cantStandAt(Block block)
    {
        if (CheatUtil.canStand(block)) return false;
        if (!CheatUtil.cantStandClose(block)) return false;
        if (!CheatUtil.cantStandFar(block)) return false;
        return true;
    }

    public static boolean cantStandAtExp(Location location)
    {
        return CheatUtil.cantStandAt(new Location(location.getWorld(), CheatUtil.fixXAxis(location.getX()), location.getY() - 0.01, (double) location.getBlockZ()).getBlock());
    }

    public static boolean cantStandClose(Block block)
    {
        if (CheatUtil.canStand(block.getRelative(BlockFace.NORTH))) return false;
        if (CheatUtil.canStand(block.getRelative(BlockFace.EAST))) return false;
        if (CheatUtil.canStand(block.getRelative(BlockFace.SOUTH))) return false;
        if (CheatUtil.canStand(block.getRelative(BlockFace.WEST))) return false;
        return true;
    }

    public static boolean cantStandFar(Block block)
    {
        if (CheatUtil.canStand(block.getRelative(BlockFace.NORTH_WEST))) return false;
        if (CheatUtil.canStand(block.getRelative(BlockFace.NORTH_EAST))) return false;
        if (CheatUtil.canStand(block.getRelative(BlockFace.SOUTH_WEST))) return false;
        if (CheatUtil.canStand(block.getRelative(BlockFace.SOUTH_EAST))) return false;
        return true;
    }

    public static boolean canStand(Block block)
    {
        if (block.isLiquid()) return false;
        if (block.getType() == Material.AIR) return false;
        return true;
    }

    public static boolean isFullyInWater(Location location)
    {
        double d = CheatUtil.fixXAxis(location.getX());
        if (!new Location(location.getWorld(), d, location.getY(), (double) location.getBlockZ()).getBlock().isLiquid())
            return false;
        if (!new Location(location.getWorld(), d, (double) Math.round(location.getY()), (double) location.getBlockZ()).getBlock().isLiquid())
            return false;
        return true;
    }

    public static double fixXAxis(double d)
    {
        double d2 = d;
        double d3 = d2 - (double) Math.round(d2) + 0.01;
        if (!(d3 < 0.3)) return d2;
        return NumberConversions.floor((double) d) - 1;
    }

    public static boolean isHoveringOverWater(Location location, int n)
    {
        for (int i = location.getBlockY(); i > location.getBlockY() - n; --i)
        {
            Block block = new Location(location.getWorld(), (double) location.getBlockX(), (double) i, (double) location.getBlockZ()).getBlock();
            if (block.getType() == Material.AIR) continue;
            return block.isLiquid();
        }
        return false;
    }

    public static boolean isHoveringOverWater(Location location)
    {
        return CheatUtil.isHoveringOverWater(location, 25);
    }

    public static boolean isInstantBreak(Material material)
    {
        return INSTANT_BREAK.contains((Object) material);
    }

    public static boolean isFood(Material material)
    {
        return FOOD.contains((Object) material);
    }

    public static boolean isSlab(Block block)
    {
        Material material = block.getType();

        if (material.ordinal() == 278) return true;
        return false;
    }

    public static boolean isStair(Block block)
    {
        Material material = block.getType();

        if (material.ordinal() == 334) return true;

        return false;
    }

    public static boolean isInteractable(Material material)
    {
        return INTERACTABLE.contains((Object) material);
    }

    public static boolean sprintFly(Player player)
    {
        if (player.isSprinting()) return true;
        if (player.isFlying()) return true;
        return false;
    }

    public static boolean isOnLilyPad(Player player)
    {
        Block block = player.getLocation().getBlock();
        Material material = Material.WATER_LILY;
        if (block.getType() == material) return true;
        if (block.getRelative(BlockFace.NORTH).getType() == material) return true;
        if (block.getRelative(BlockFace.SOUTH).getType() == material) return true;
        if (block.getRelative(BlockFace.EAST).getType() == material) return true;
        if (block.getRelative(BlockFace.WEST).getType() == material) return true;
        return false;
    }

    public static boolean isSubmersed(Player player)
    {
        if (!player.getLocation().getBlock().isLiquid()) return false;
        if (!player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid()) return false;
        return true;
    }

    public static boolean isInWater(Player player)
    {
        if (player.getLocation().getBlock().isLiquid()) return true;
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).isLiquid()) return true;
        if (player.getLocation().getBlock().getRelative(BlockFace.UP).isLiquid()) return true;
        return false;
    }

    public static boolean isInWeb(Player player)
    {
        if (player.getLocation().getBlock().getType() == Material.WEB) return true;
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WEB) return true;
        if (player.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.WEB) return true;
        return false;
    }

    public static boolean isClimbableBlock(Block block)
    {
        if (block.getType() == Material.VINE) return true;
        if (block.getType() == Material.LADDER) return true;
        if (block.getType() == Material.WATER) return true;
        if (block.getType() == Material.STATIONARY_WATER) return true;
        return false;
    }

    public static boolean isOnVine(Player player)
    {
        if (player.getLocation().getBlock().getType() != Material.VINE) return false;
        return true;
    }

    public static boolean blocksNear(Player player)
    {
        return CheatUtil.blocksNear(player.getLocation());
    }

    public static boolean blocksNear(Location location)
    {
        boolean bl = false;
        for (Block block : BlockUtil.getSurrounding(location.getBlock(), true))
        {
            if (block.getType() == Material.AIR) continue;
            bl = true;
            break;
        }
        for (Block block : BlockUtil.getSurrounding(location.getBlock(), false))
        {
            if (block.getType() == Material.AIR) continue;
            bl = true;
            break;
        }
        location.setY(location.getY() - 0.5);
        if (location.getBlock().getType() != Material.AIR)
        {
            bl = true;
        }
        if (!CheatUtil.isBlock(location.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.FENCE, Material.FENCE_GATE, Material.COBBLE_WALL, Material.LADDER}))
            return bl;
        return true;
    }

    public static boolean slabsNear(Location location)
    {
        boolean bl = false;
        for (Block block : BlockUtil.getSurrounding(location.getBlock(), true))
        {
            if (!block.getType().equals((Object) Material.STEP) && !block.getType().equals((Object) Material.DOUBLE_STEP) && !block.getType().equals((Object) Material.WOOD_DOUBLE_STEP) && !block.getType().equals((Object) Material.WOOD_STEP))
                continue;
            bl = true;
            break;
        }
        for (Block block : BlockUtil.getSurrounding(location.getBlock(), false))
        {
            if (!block.getType().equals((Object) Material.STEP) && !block.getType().equals((Object) Material.DOUBLE_STEP) && !block.getType().equals((Object) Material.WOOD_DOUBLE_STEP) && !block.getType().equals((Object) Material.WOOD_STEP))
                continue;
            bl = true;
            break;
        }
        if (!CheatUtil.isBlock(location.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.STEP, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP}))
            return bl;
        return true;
    }

    public static boolean isBlock(Block block, Material[] arrmaterial)
    {
        Material material = block.getType();
        for (Material material2 : arrmaterial)
        {
            if (material2 != material) continue;
            return true;
        }
        return false;
    }

    public static String[] getCommands(String string)
    {
        return string.replaceAll("COMMAND\\[", "").replaceAll("]", "").split(";");
    }

    public static String removeWhitespace(String string)
    {
        return string.replaceAll(" ", "");
    }

    public static boolean hasArmorEnchantment(Player player, Enchantment enchantment)
    {
        for (ItemStack itemStack : player.getInventory().getArmorContents())
        {
            if (itemStack == null || !itemStack.containsEnchantment(enchantment)) continue;
            return true;
        }
        return false;
    }

    public static String listToCommaString(List<String> list)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); ++i)
        {
            stringBuilder.append(list.get(i));
            if (i >= list.size() - 1) continue;
            stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    public static long lifeToSeconds(String string)
    {
        if (string.equals("0")) return 0L;
        if (string.equals(""))
        {
            return 0L;
        }
        String[] arrstring = new String[]{"d", "h", "m", "s"};
        int[] arrn = new int[]{86400, 3600, 60, 1};
        long l = 0L;
        for (int i = 0; i < arrstring.length; ++i)
        {
            Matcher matcher = Pattern.compile("([0-9]*)" + arrstring[i]).matcher(string);
            while (matcher.find())
            {
                l += (long) (Integer.parseInt(matcher.group(1)) * arrn[i]);
            }
        }
        return l;
    }

    public static double[] cursor(Player player, LivingEntity livingEntity)
    {
        Location location = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        Location location2 = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        Vector vector2 = CheatUtil.getRotation(location2, location);
        double d = CheatUtil.clamp180(vector.getX() - vector2.getX());
        double d2 = CheatUtil.clamp180(vector.getY() - vector2.getY());
        double d3 = CheatUtil.getHorizontalDistance(location2, location);
        double d4 = CheatUtil.getDistance3D(location2, location);
        double d5 = d * d3 * d4;
        double d6 = d2 * Math.abs(Math.sqrt(location.getY() - location2.getY())) * d4;
        return new double[]{Math.abs(d5), Math.abs(d6)};
    }

    public static double getAimbotoffset(Location location, double d, LivingEntity livingEntity)
    {
        Location location2 = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        Location location3 = location.add(0.0, d, 0.0);
        Vector vector = new Vector(location3.getYaw(), location3.getPitch(), 0.0f);
        Vector vector2 = CheatUtil.getRotation(location3, location2);
        double d2 = CheatUtil.clamp180(vector.getX() - vector2.getX());
        double d3 = CheatUtil.getHorizontalDistance(location3, location2);
        double d4 = CheatUtil.getDistance3D(location3, location2);
        return d2 * d3 * d4;
    }

    public static double getAimbotoffset2(Location location, double d, LivingEntity livingEntity)
    {
        Location location2 = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        Location location3 = location.add(0.0, d, 0.0);
        Vector vector = new Vector(location3.getYaw(), location3.getPitch(), 0.0f);
        Vector vector2 = CheatUtil.getRotation(location3, location2);
        double d2 = CheatUtil.clamp180(vector.getY() - vector2.getY());
        double d3 = CheatUtil.getDistance3D(location3, location2);
        return d2 * Math.abs(Math.sqrt(location2.getY() - location3.getY())) * d3;
    }

    public static double[] getOffsetsOffCursor(Player player, LivingEntity livingEntity)
    {
        Location location = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        Location location2 = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        Vector vector2 = CheatUtil.getRotation(location2, location);
        double d = CheatUtil.clamp180(vector.getX() - vector2.getX());
        double d2 = CheatUtil.clamp180(vector.getY() - vector2.getY());
        double d3 = CheatUtil.getHorizontalDistance(location2, location);
        double d4 = CheatUtil.getDistance3D(location2, location);
        double d5 = d * d3 * d4;
        double d6 = d2 * Math.abs(Math.sqrt(location.getY() - location2.getY())) * d4;
        return new double[]{Math.abs(d5), Math.abs(d6)};
    }
}

