package org.runnerer.spycheater.common.utils;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.runnerer.spycheater.SpyCheater;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class UtilServer
{

    private static Object MC_SERVER_OBJ = null;
    private static Field MC_SERVER_TPS_FIELD = null;

    public static List<Entity> getEntities(World world)
    {
        return new ArrayList<Entity>(world.getEntities());
    }

    public static void asyncKick(final Player player, final String string)
    {
        SpyCheater.Instance.getServer().getScheduler().runTask((Plugin) SpyCheater.Instance, new Runnable()
        {

            @Override
            public void run()
            {
                player.kickPlayer(string);
            }
        });
    }

    public static int getPing(Player player)
    {
        Object object = ReflectionUtil.getEntityHandle((Entity) player);
        if (object == null) return 150;
        Field field = ReflectionUtil.getField("ping", object.getClass());
        try
        {
            return field.getInt(object);
        }
        catch (Exception exception)
        {
            // empty catch block
        }
        return 150;
    }

    public static double[] getTps()
    {
        if (MC_SERVER_OBJ == null)
        {
            try
            {
                MC_SERVER_OBJ = ReflectionUtil.getMethod("getServer", ReflectionUtil.getNMSClass("MinecraftServer")).invoke(null, new Object[0]);
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException)
            {
                reflectiveOperationException.printStackTrace();
            }
            MC_SERVER_TPS_FIELD = ReflectionUtil.getField("recentTps", ReflectionUtil.getNMSClass("MinecraftServer"));
        }
        try
        {
            return (double[]) MC_SERVER_TPS_FIELD.get(MC_SERVER_OBJ);
        }
        catch (IllegalAccessException illegalAccessException)
        {
            illegalAccessException.printStackTrace();
            return null;
        }
    }
}

