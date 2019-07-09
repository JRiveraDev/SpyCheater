package org.runnerer.spycheater.common.utils;

import org.bukkit.ChatColor;

public class ChatUtil
{

    public static String Red = ChatColor.RED.toString();

    public static String strip(String string)
    {
        return ChatColor.stripColor((String) string);
    }

    public static String c(String string)
    {
        return ChatColor.translateAlternateColorCodes((char) '&', (String) string);
    }

    public static String colorize(String string)
    {
        return ChatUtil.c(string);
    }
}

