package org.runnerer.spycheater.common.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class UtilMath
{

    public static Random random = new Random();

    public static double getFraction(double d)
    {
        return d % 1.0;
    }

    public static double round(double d, int n)
    {
        if (n < 0)
        {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public static double trim(int n, double d)
    {
        String string = "#.#";
        for (int i = 1; i < n; ++i)
        {
            string = String.valueOf(String.valueOf(string)) + "#";
        }
        DecimalFormat decimalFormat = new DecimalFormat(string);
        return Double.valueOf(decimalFormat.format(d));
    }

    public static boolean close(Double[] arrdouble, int n)
    {
        double d = arrdouble[4];
        double d2 = arrdouble[3];
        double d3 = arrdouble[2];
        double d4 = arrdouble[1];
        double d5 = arrdouble[0];
        boolean bl = (d >= d2 ? d - d2 : d2 - d) <= (double) n;
        boolean bl2 = (d >= d3 ? d - d3 : d3 - d) <= (double) n;
        boolean bl3 = (d >= d4 ? d - d4 : d4 - d) <= (double) n;
        boolean bl4 = (d >= d5 ? d - d5 : d5 - d) <= (double) n;
        if (!bl) return false;
        if (!bl2) return false;
        if (!bl3) return false;
        if (!bl4) return false;
        return true;
    }

    public static double getXZDistance(Location location, Location location2)
    {
        double d = Math.abs(Math.abs(location.getX()) - Math.abs(location2.getX()));
        double d2 = Math.abs(Math.abs(location.getZ()) - Math.abs(location2.getZ()));
        return Math.sqrt(d * d + d2 * d2);
    }

    public static int r(int n)
    {
        return random.nextInt(n);
    }

    public static double abs(double d)
    {
        double d2;
        if (d <= 0.0)
        {
            d2 = 0.0 - d;
            return d2;
        }
        d2 = d;
        return d2;
    }

    public static float getYawDifference(Location location, Location location2)
    {
        float f = UtilMath.getYaw(location);
        float f2 = UtilMath.getYaw(location2);
        float f3 = Math.abs(f - f2);
        if (!(f < 90.0f) || !(f2 > 270.0f))
        {
            if (!(f2 < 90.0f)) return Math.abs(f3);
            if (!(f > 270.0f)) return Math.abs(f3);
        }
        f3 -= 360.0f;
        return Math.abs(f3);
    }

    public static double getMagnitude(double x, double y)
    {
        return Math.sqrt(x * x + y * y);
    }

    public static float getYaw(Location location)
    {
        float f = (location.getYaw() - 90.0f) % 360.0f;
        if (!(f < 0.0f)) return f;
        f += 360.0f;
        return f;
    }

    public static String arrayToString(String[] arrstring)
    {
        String string = "";
        for (String string2 : arrstring)
        {
            string = String.valueOf(String.valueOf(string)) + string2 + ",";
        }
        if (string.length() == 0) return null;
        return string.substring(0, string.length() - 1);
    }

    public static String arrayToString(List<String> list)
    {
        String string = "";
        for (String string2 : list)
        {
            string = String.valueOf(String.valueOf(string)) + string2 + ",";
        }
        if (string.length() == 0) return null;
        return string.substring(0, string.length() - 1);
    }

    public static String[] stringtoArray(String string, String string2)
    {
        return string.split(string2);
    }

    public static double offset2d(Entity entity, Entity entity2)
    {
        return UtilMath.offset2d(entity.getLocation().toVector(), entity2.getLocation().toVector());
    }

    public static double offset2d(Location location, Location location2)
    {
        return UtilMath.offset2d(location.toVector(), location2.toVector());
    }

    public static double offset2d(Vector vector, Vector vector2)
    {
        vector.setY(0);
        vector2.setY(0);
        return vector.subtract(vector2).length();
    }

    public static double distanceXZSquared(Location location, Location location2)
    {
        double d = location.getX() - location2.getX();
        double d2 = location.getZ() - location2.getZ();
        return d * d + d2 * d2;
    }

    public static double offset(Entity entity, Entity entity2)
    {
        return UtilMath.offset(entity.getLocation().toVector(), entity2.getLocation().toVector());
    }

    public static double offset(Location location, Location location2)
    {
        return UtilMath.offset(location.toVector(), location2.toVector());
    }

    public static double offset(Vector vector, Vector vector2)
    {
        return vector.subtract(vector2).length();
    }

    public static Vector getHorizontalVector(Vector vector)
    {
        vector.setY(0);
        return vector;
    }

    public static Vector getVerticalVector(Vector vector)
    {
        vector.setX(0);
        vector.setZ(0);
        return vector;
    }

    public static String serializeLocation(Location location)
    {
        int n = (int) location.getX();
        int n2 = (int) location.getY();
        int n3 = (int) location.getZ();
        int n4 = (int) location.getPitch();
        int n5 = (int) location.getYaw();
        return String.valueOf(String.valueOf(location.getWorld().getName())) + "," + n + "," + n2 + "," + n3 + "," + n4 + "," + n5;
    }

    public static Location deserializeLocation(String string)
    {
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

    public static long averageLong(List<Long> list)
    {
        long l = 0L;
        for (Long l2 : list)
        {
            l += l2.longValue();
        }
        return l / (long) list.size();
    }

    public static double averageDouble(List<Double> list)
    {
        Double d = 0.0;
        for (Double d2 : list)
        {
            d = d + d2;
        }
        return d / (double) list.size();
    }

    public static float averageFloat(List<Float> list)
    {
        Float f = Float.valueOf(0.0f);
        for (Float f2 : list)
        {
            f = Float.valueOf(f.floatValue() + f2.floatValue());
        }
        return f.floatValue() / (float) list.size();
    }
}

