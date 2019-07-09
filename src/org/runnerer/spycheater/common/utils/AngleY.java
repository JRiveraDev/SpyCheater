package org.runnerer.spycheater.common.utils;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class AngleY
{

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

    public static double getVerticalDistance(Location location, Location location2)
    {
        double d = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        double d2 = Math.sqrt(d);
        return Math.abs(d2);
    }

    public static double getHorizontalDistance(Location location, Location location2)
    {
        double d = 0.0;
        double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        double d4 = Math.sqrt(d2 + d3);
        return Math.abs(d4);
    }

    public static double fix180(double d)
    {
        if ((d %= 360.0) >= 180.0)
        {
            d -= 360.0;
        }
        if (!(d < -180.0)) return d;
        d += 360.0;
        return d;
    }

    public static double getDistance3D(Location location, Location location2)
    {
        double d = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        double d2 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        double d4 = Math.sqrt(d + d2 + d3);
        return Math.abs(d4);
    }

    public static float getOffset(Player player, LivingEntity livingEntity)
    {
        double d = 0.0;
        Location location = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Location location2 = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        Vector vector2 = AngleY.getRotation(location2, location);
        double d2 = AngleY.clamp180(vector.getX() - vector2.getX());
        double d3 = AngleY.clamp180(vector.getY() - vector2.getY());
        double d4 = AngleY.getHorizontalDistance(location2, location);
        double d5 = AngleY.getDistance3D(location2, location);
        double d6 = d2 * d4 * d5;
        double d7 = d3 * Math.abs(location.getY() - location2.getY()) * d5;
        d += Math.abs(d6);
        d += Math.abs(d7);
        return 0.0f;
    }
}

