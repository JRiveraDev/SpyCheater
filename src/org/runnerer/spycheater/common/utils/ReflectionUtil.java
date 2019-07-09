package org.runnerer.spycheater.common.utils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReflectionUtil
{

    private static Logger log = Logger.getLogger("BoxUtils");

    private static HashMap<String, Class<?>> classCache = new HashMap('?');
    private static HashMap<String, Field> fieldCache = new HashMap('?');
    private static HashMap<String, Method> methodCache = new HashMap('?');
    private static HashMap<String, Constructor> constructorCache = new HashMap('?');

    private static String obcPrefix = null;
    private static String nmsPrefix = null;

    static
    {
        try
        {
            nmsPrefix = "net.minecraft.server." +
                    Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

            obcPrefix = "org.bukkit.craftbukkit." +
                    Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        }
        catch (Exception exception)
        {
            nmsPrefix = "net.minecraft.server.";

            obcPrefix = "org.bukkit.craftbukkit.";
        }
    }

    public static Class<?> getCraftBukkitClass(String paramString)
    {
        return getClass(String.valueOf(obcPrefix) + paramString);
    }


    public static Class<?> getNMSClass(String paramString)
    {
        return getClass(String.valueOf(nmsPrefix) + paramString);
    }


    public static Class<?> getClass(String paramString)
    {
        Validate.notNull(paramString);

        if (classCache.containsKey(paramString))
        {
            return (Class) classCache.get(paramString);
        }
        Class clazz = null;

        try
        {
            clazz = Class.forName(paramString);
        }
        catch (ClassNotFoundException classNotFoundException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the class " + paramString);
        }

        if (clazz != null)
        {
            classCache.put(paramString, clazz);
        }

        return clazz;
    }


    public static Field getField(String paramString, Class<?> paramClass)
    {
        Validate.notNull(paramString);
        Validate.notNull(paramClass);

        String str = String.valueOf(paramClass.getCanonicalName()) + "@" + paramString;

        if (fieldCache.containsKey(str))
        {
            return (Field) fieldCache.get(str);
        }
        Field field = null;

        try
        {
            field = paramClass.getField(paramString);
        }
        catch (NoSuchFieldException noSuchFieldException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the field " +
                    paramString + " in class " + paramClass.getSimpleName());
        }

        if (field != null)
        {
            fieldCache.put(str, field);
        }

        return field;
    }


    public static Method getMethod(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass)
    {
        Validate.notNull(paramString);
        Validate.notNull(paramClass);

        String str = String.valueOf(paramClass.getCanonicalName()) + "@" + paramString;

        if (methodCache.containsKey(str))
        {
            return (Method) methodCache.get(str);
        }
        Method method = null;

        try
        {
            method = paramClass.getMethod(paramString, paramArrayOfClass);
        }
        catch (NoSuchMethodException noSuchMethodException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " +
                    paramString + " in class " + paramClass.getSimpleName());
        }

        if (method != null)
        {
            methodCache.put(str, method);
        }

        return method;
    }


    public static Method getMethod(String paramString, Class<?> paramClass, Class<?>[] paramArrayOfClass)
    {
        Validate.notNull(paramString);
        Validate.notNull(paramClass);

        String str = String.valueOf(paramClass.getCanonicalName()) + "@" + paramString;

        if (methodCache.containsKey(str))
        {
            return (Method) methodCache.get(str);
        }
        Method method = null;

        try
        {
            method = paramClass.getMethod(paramString, paramArrayOfClass);
        }
        catch (NoSuchMethodException noSuchMethodException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " +
                    paramString + " in class " + paramClass.getSimpleName());
        }

        if (method != null)
        {
            methodCache.put(str, method);
        }

        return method;
    }


    public static Method getMethod(String paramString, Class<?> paramClass)
    {
        Validate.notNull(paramString);
        Validate.notNull(paramClass);

        String str = String.valueOf(paramClass.getCanonicalName()) + "@" + paramString;

        if (methodCache.containsKey(str))
        {
            return (Method) methodCache.get(str);
        }
        Method method = null;

        try
        {
            method = paramClass.getMethod(paramString, new Class[0]);
        }
        catch (NoSuchMethodException noSuchMethodException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " +
                    paramString + " in class " + paramClass.getSimpleName());
        }

        if (method != null)
        {
            methodCache.put(str, method);
        }

        return method;
    }


    public static Constructor<?> getConstructor(Class<?> paramClass, Class<?>[] paramArrayOfClass)
    {
        Validate.notNull(paramClass);
        Validate.notNull(paramArrayOfClass);

        String str = String.valueOf(paramClass.getSimpleName()) + "#";
        int i;
        Class<?>[] arrayOfClass;
        /*for (int b; i = paramArrayOfClass.length, b = 0; b < i; ) { Class<?> clazz = arrayOfClass[b];
            str = String.valueOf(str) + clazz.getSimpleName() + "_";
            b++; }*/

        str = str.substring(0, str.length() - 1);

        if (constructorCache.containsKey(str))
        {
            return (Constructor) constructorCache.get(str);
        }
        Constructor constructor = null;

        try
        {
            constructor = paramClass.getConstructor(paramArrayOfClass);
        }
        catch (NoSuchMethodException b)
        {
            NoSuchMethodException noSuchMethodException;
            StringBuilder stringBuilder = new StringBuilder();
            byte b1;
            int j;
            Class<?>[] arrayOfClass1 = null;
            for (j = paramArrayOfClass.length, b1 = 0; b1 < j; )
            {
                Class<?> clazz = arrayOfClass1[b1];
                stringBuilder.append(clazz.getCanonicalName()).append(", ");
                b1++;
            }

            stringBuilder.setLength(stringBuilder.length() - 2);

            log.log(Level.SEVERE, "[Reflection] Unable to find the the constructor  with the params [" +

                    stringBuilder.toString() + "] in class " + paramClass.getSimpleName());
        }

        if (constructor != null)
        {
            constructorCache.put(str, constructor);
        }

        return constructor;
    }


    public static Object getEntityHandle(Entity paramEntity)
    {
        try
        {
            Method method = getMethod("getHandle", paramEntity.getClass());
            return method.invoke(paramEntity, new Object[0]);
        }
        catch (Exception exception)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to getHandle of " +
                    paramEntity.getType());
            return null;
        }
    }

    public static void sendPacket(Player paramPlayer, Object paramObject)
    {
        Object object = null;
        try
        {
            object = getEntityHandle(paramPlayer);
            Object object1 = object.getClass().getField("playerConnection").get(object);
            getMethod("sendPacket", object1.getClass()).invoke(object1, new Object[]{paramObject});
        }
        catch (IllegalAccessException illegalAccessException)
        {
            illegalAccessException.printStackTrace();
        }
        catch (InvocationTargetException invocationTargetException)
        {
            invocationTargetException.printStackTrace();
        }
        catch (NoSuchFieldException noSuchFieldException)
        {
            noSuchFieldException.printStackTrace();
        }
    }
}
