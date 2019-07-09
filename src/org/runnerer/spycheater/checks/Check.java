package org.runnerer.spycheater.checks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.common.utils.ID;

import java.util.function.Consumer;

public class Check
        implements Listener
{

    private CheckType checkType;
    private String displayName;
    private String Name;
    private SpyCheater Core;
    private long expiration = 300000L;
    private int threshold;
    private int level;
    private int bans;
    private int autobanTimer;
    private boolean enabled = true;
    private String id;
    private double tpsDisable;
    private boolean tempDisabled;

    public Check(SpyCheater antiCheat, CheckType checkType, String string, String string2, int n, int n2, int n3, int n4)
    {
        this.Core = antiCheat;
        this.checkType = checkType;
        this.Name = string;
        this.displayName = string2;
        this.threshold = n;
        this.level = n2;
        this.bans = n3;
        this.autobanTimer = n4;
        this.id = ID.generate();
        this.tpsDisable = 17.0;
        this.tempDisabled = false;
    }

    public CheckType getCheckType()
    {
        return this.checkType;
    }

    public SpyCheater getCore()
    {
        return this.Core;
    }

    public String getName()
    {
        return this.Name;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public int getThreshold()
    {
        return this.threshold;
    }

    public int getLevel()
    {
        return this.level;
    }

    public int getBans()
    {
        return this.bans;
    }

    public long getExpiration()
    {
        return this.expiration;
    }

    public void setExpiration(long l)
    {
        this.expiration = l;
    }

    public int getAutobanTimer()
    {
        return this.autobanTimer;
    }

    public boolean isEnabled()
    {
        if (!this.enabled) return false;
        if (this.tempDisabled) return false;
        return true;
    }

    public void setEnabled(boolean bl)
    {
        this.enabled = bl;
    }

    public String getId()
    {
        return this.id;
    }

    public boolean isTempDisabled()
    {
        return this.tempDisabled;
    }

    public void setTempDisabled(boolean bl)
    {
        this.tempDisabled = bl;
    }

    public double getTpsDisable()
    {
        return this.tpsDisable;
    }

    public boolean isActuallyEnabled()
    {
        return this.enabled;
    }

    protected BukkitTask runTask(Runnable runnable)
    {
        return Bukkit.getScheduler().runTask((Plugin) SpyCheater.Instance, runnable);
    }

    protected BukkitTask runTaskLater(Runnable runnable, long l)
    {
        return Bukkit.getScheduler().runTaskLater((Plugin) SpyCheater.Instance, runnable, l);
    }

    protected BukkitTask runTaskTimer(Runnable runnable, long l, long l2)
    {
        return Bukkit.getScheduler().runTaskTimer((Plugin) SpyCheater.Instance, runnable, l, l2);
    }

    protected void registerPacketReceiving(PacketType packetType, final Consumer<PacketEvent> consumer)
    {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) SpyCheater.Instance, new PacketType[]{packetType})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                consumer.accept(packetEvent);
            }
        });
    }

    protected void registerPacketSending(PacketType packetType, final Consumer<PacketEvent> consumer)
    {
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) SpyCheater.Instance, new PacketType[]{packetType})
        {

            public void onPacketSending(PacketEvent packetEvent)
            {
                consumer.accept(packetEvent);
            }
        });
    }
}

