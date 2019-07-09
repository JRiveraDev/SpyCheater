package org.runnerer.spycheater.update;

import org.runnerer.spycheater.common.utils.UtilTime;

public enum UpdateType
{
    MIN_64("MIN_64", 0, 3840000L),
    MIN_32("MIN_32", 1, 1920000L),
    MIN_16("MIN_16", 2, 960000L),
    MIN_08("MIN_08", 3, 480000L),
    MIN_05("MIN_05", 4, 300000L),
    MIN_04("MIN_04", 5, 240000L),
    MIN_02("MIN_02", 6, 120000L),
    MIN_01("MIN_01", 7, 60000L),
    SLOWEST("SLOWEST", 8, 32000L),
    SLOWER("SLOWER", 9, 16000L),
    SLOW("SLOW", 10, 4000L),
    SEC_05("SEC_05", 11, 5000L),
    SEC_04("SEC_04", 12, 4000L),
    SEC_03("SEC_03", 13, 3000L),
    SEC_02("SEC_02", 14, 2000L),
    SEC("SEC", 15, 1000L),
    FAST("FAST", 16, 500L),
    FASTER("FASTER", 17, 250L),
    FASTEST("FASTEST", 18, 125L),
    TICK("TICK", 19, 49L);

    private long _time;
    private long _last;
    private long _timeSpent;
    private long _timeCount;

    private UpdateType(String string2, int n2, long l)
    {
        this._time = l;
        this._last = System.currentTimeMillis();
    }

    public boolean Elapsed()
    {
        if (!UtilTime.elapsed(this._last, this._time)) return false;
        this._last = System.currentTimeMillis();
        return true;
    }

    public void StartTime()
    {
        this._timeCount = System.currentTimeMillis();
    }

    public void StopTime()
    {
        this._timeSpent += System.currentTimeMillis() - this._timeCount;
    }

    public void PrintAndResetTime()
    {
        System.out.println(String.valueOf(String.valueOf(this.name())) + " in a second: " + this._timeSpent);
        this._timeSpent = 0L;
    }
}

