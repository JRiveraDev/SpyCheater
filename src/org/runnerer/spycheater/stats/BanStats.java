package org.runnerer.spycheater.stats;

import org.runnerer.spycheater.SpyCheater;

public class BanStats
{

    public static int bansToday;
    public static int bansOverall;

    public static void load()
    {
        bansToday = 0;
        bansOverall = SpyCheater.Instance.Config.getConfig().getInt("bans-overall", 0);
    }

    public static void save()
    {
        SpyCheater.Instance.Config.getConfig().set("bans-overall", bansOverall);
    }

    public static void update()
    {
        bansOverall =+ 1;
        bansToday =+ 1;
    }
}

