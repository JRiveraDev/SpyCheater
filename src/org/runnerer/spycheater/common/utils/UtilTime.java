package org.runnerer.spycheater.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilTime
{

    public static final String DATE_FORMAT_NOW;
    public static final String DATE_FORMAT_DAY;

    static
    {
        DATE_FORMAT_DAY = "yyyy-MM-dd";
        DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    }

    public static String now()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static long nowlong()
    {
        return System.currentTimeMillis();
    }

    public static String when(long l)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        return simpleDateFormat.format(l);
    }

    public static long a(String string)
    {
        if (string.endsWith("s"))
        {
            return Long.valueOf(string.substring(0, string.length() - 1)) * 1000L;
        }
        if (string.endsWith("m"))
        {
            return Long.valueOf(string.substring(0, string.length() - 1)) * 60000L;
        }
        if (string.endsWith("h"))
        {
            return Long.valueOf(string.substring(0, string.length() - 1)) * 3600000L;
        }
        if (string.endsWith("d"))
        {
            return Long.valueOf(string.substring(0, string.length() - 1)) * 86400000L;
        }
        if (string.endsWith("m"))
        {
            return Long.valueOf(string.substring(0, string.length() - 1)) * 2592000000L;
        }
        if (!string.endsWith("y")) return -1L;
        return Long.valueOf(string.substring(0, string.length() - 1)) * 31104000000L;
    }

    public static String date()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String getTime(int n)
    {
        Date date = new Date();
        date.setTime(n * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String since(long l)
    {
        return "Took " + UtilTime.convertString(System.currentTimeMillis() - l, 1, TimeUnit.FIT) + ".";
    }

    public static double convert(long l, int n, TimeUnit timeUnit)
    {
        if (timeUnit == TimeUnit.FIT)
        {
            timeUnit = l < 60000L ? TimeUnit.SECONDS : (l < 3600000L ? TimeUnit.MINUTES : (l < 86400000L ? TimeUnit.HOURS : TimeUnit.DAYS));
        }
        if (timeUnit == TimeUnit.DAYS)
        {
            return UtilMath.trim(n, (double) l / 8.64E7);
        }
        if (timeUnit == TimeUnit.HOURS)
        {
            return UtilMath.trim(n, (double) l / 3600000.0);
        }
        if (timeUnit == TimeUnit.MINUTES)
        {
            return UtilMath.trim(n, (double) l / 60000.0);
        }
        if (timeUnit != TimeUnit.SECONDS) return UtilMath.trim(n, l);
        return UtilMath.trim(n, (double) l / 1000.0);
    }

    public static String MakeStr(long l)
    {
        return UtilTime.convertString(l, 1, TimeUnit.FIT);
    }

    public static String MakeStr(long l, int n)
    {
        return UtilTime.convertString(l, n, TimeUnit.FIT);
    }

    public static String convertString(long l, int n, TimeUnit timeUnit)
    {
        if (l == -1L)
        {
            return "Permanent";
        }
        if (timeUnit == TimeUnit.FIT)
        {
            timeUnit = l < 60000L ? TimeUnit.SECONDS : (l < 3600000L ? TimeUnit.MINUTES : (l < 86400000L ? TimeUnit.HOURS : TimeUnit.DAYS));
        }
        if (timeUnit == TimeUnit.DAYS)
        {
            return String.valueOf(String.valueOf(UtilMath.trim(n, (double) l / 8.64E7))) + " Days";
        }
        if (timeUnit == TimeUnit.HOURS)
        {
            return String.valueOf(String.valueOf(UtilMath.trim(n, (double) l / 3600000.0))) + " Hours";
        }
        if (timeUnit == TimeUnit.MINUTES)
        {
            return String.valueOf(String.valueOf(UtilMath.trim(n, (double) l / 60000.0))) + " Minutes";
        }
        if (timeUnit != TimeUnit.SECONDS) return String.valueOf(String.valueOf(UtilMath.trim(n, l))) + " Milliseconds";
        return String.valueOf(String.valueOf(UtilMath.trim(n, (double) l / 1000.0))) + " Seconds";
    }

    public static boolean elapsed(long l, long l2)
    {
        if (System.currentTimeMillis() - l <= l2) return false;
        return true;
    }

    public static long elapsed(long l)
    {
        return System.currentTimeMillis() - l;
    }

    public static long left(long l, long l2)
    {
        return l2 + l - System.currentTimeMillis();
    }

    public static enum TimeUnit
    {
        FIT("FIT", 0),
        DAYS("DAYS", 1),
        HOURS("HOURS", 2),
        MINUTES("MINUTES", 3),
        SECONDS("SECONDS", 4),
        MILLISECONDS("MILLISECONDS", 5);


        private TimeUnit(String string2, int n2)
        {
        }
    }

}

