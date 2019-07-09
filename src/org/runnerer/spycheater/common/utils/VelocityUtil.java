package org.runnerer.spycheater.common.utils;

import org.bukkit.util.Vector;

public class VelocityUtil
{

    public static double getVelocityHorizontalAsDistance(Vector vector)
    {
        return Math.sqrt(vector.getX() * vector.getX() + vector.getZ() * vector.getZ());
    }

    public static double getVelocityHeightAsDistance(Vector vector)
    {
        return Math.sqrt(vector.getY() * vector.getY());
    }
}

