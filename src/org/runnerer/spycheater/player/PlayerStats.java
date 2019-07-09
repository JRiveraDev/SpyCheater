package org.runnerer.spycheater.player;

import org.bukkit.Location;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.violation.Violation;

import java.util.*;

public class PlayerStats
{

    private UUID UUID;
    private Map<Check, Map<Integer, Integer>> threshold = new HashMap<Check, Map<Integer, Integer>>();
    private Map<Check, List<Violation>> violations = new HashMap<Check, List<Violation>>();
    private Map<Check, Integer> bans = new HashMap<Check, Integer>();
    private long lastReceivedKeepAlive;
    private int lastReceivedKeepAliveID;
    private long lastSentKeepAlive;
    private int lastSentKeepAliveID;
    private long lastAnimation;
    private int animationSpam;
    private long lastBlockPlacementPacket;
    private int blockPlacementSpam;
    private double velocityY;
    private double velocityXZ;
    private long velocityTime;
    private boolean onGround;
    private Location lastGround;
    private long lastGroundTime = System.currentTimeMillis();
    private long lastBunnyTime = System.currentTimeMillis();
    private int highestCPS;
    private int latestCPS;
    private long lastMount;
    private long lastWorldChange;
    private long lastPlayerPacket;
    private long lastDelayedPacket;
    private double cps = 0.0;
    private int clicks;
    private int hits;

    public PlayerStats(UUID uUID)
    {
        this.UUID = uUID;
    }

    public UUID getUUID()
    {
        return this.UUID;
    }

    public double getCPS()
    {
        return this.cps;
    }

    public void setCPS(double d)
    {
        this.cps = d;
    }

    public long getLastDelayedPacket()
    {
        return this.lastDelayedPacket;
    }

    public void setLastDelayedPacket(long l)
    {
        this.lastDelayedPacket = l;
    }

    public long getLastDelayedPacketDiff()
    {
        return System.currentTimeMillis() - this.getLastDelayedPacket();
    }

    public long getLastPlayerPacket()
    {
        return this.lastPlayerPacket;
    }

    public void setLastPlayerPacket(long l)
    {
        this.lastPlayerPacket = l;
    }

    public long getLastPlayerPacketDiff()
    {
        return System.currentTimeMillis() - this.getLastPlayerPacket();
    }

    public long getLastMountDiff()
    {
        return System.currentTimeMillis() - this.getLastMount();
    }

    public long getLastMount()
    {
        return this.lastMount;
    }

    public void setLastMount(long l)
    {
        this.lastMount = l;
    }

    public long getLastWorldChangeDiff()
    {
        return System.currentTimeMillis() - this.getLastWorldChange();
    }

    public long getLastWorldChange()
    {
        return this.lastWorldChange;
    }

    public void setLastWorldChange(long l)
    {
        this.lastWorldChange = l;
    }

    public long getLastReceivedKeepAlive()
    {
        return this.lastReceivedKeepAlive;
    }

    public void setLastReceivedKeepAlive(long l)
    {
        this.lastReceivedKeepAlive = l;
    }

    public long getLastSentKeepAlive()
    {
        return this.lastSentKeepAlive;
    }

    public void setLastSentKeepAlive(long l)
    {
        this.lastSentKeepAlive = l;
    }

    public int getLastReceivedKeepAliveID()
    {
        return this.lastReceivedKeepAliveID;
    }

    public void setLastReceivedKeepAliveID(int n)
    {
        this.lastReceivedKeepAliveID = n;
    }

    public int getLastSentKeepAliveID()
    {
        return this.lastSentKeepAliveID;
    }

    public void setLastSentKeepAliveID(int n)
    {
        this.lastSentKeepAliveID = n;
    }

    public long getLastAnimation()
    {
        return this.lastAnimation;
    }

    public void setLastAnimation(long l)
    {
        this.lastAnimation = l;
    }

    public int getAnimationSpam()
    {
        return this.animationSpam;
    }

    public void setAnimationSpam(int n)
    {
        this.animationSpam = n;
    }

    public long getLastBlockPlacementPacket()
    {
        return this.lastBlockPlacementPacket;
    }

    public void setLastBlockPlacementPacket(long l)
    {
        this.lastBlockPlacementPacket = l;
    }

    public int getBlockPlacementSpam()
    {
        return this.blockPlacementSpam;
    }

    public void setBlockPlacementSpam(int n)
    {
        this.blockPlacementSpam = n;
    }

    public double getVelocityY()
    {
        return this.velocityY;
    }

    public void setVelocityY(double d)
    {
        this.velocityY = d;
    }

    public double getVelocityXZ()
    {
        return this.velocityXZ;
    }

    public void setVelocityXZ(double d)
    {
        this.velocityXZ = d;
    }

    public long getVelocityTime()
    {
        return this.velocityTime;
    }

    public void setVelocityTime(long l)
    {
        this.velocityTime = l;
    }

    public boolean isOnGround()
    {
        return this.onGround;
    }

    public void setOnGround(boolean bl)
    {
        this.onGround = bl;
    }

    public Location getLastGround()
    {
        return this.lastGround;
    }

    public void setLastGround(Location location)
    {
        this.lastGround = location;
    }

    public long getLastGroundTime()
    {
        return this.lastGroundTime;
    }

    public void setLastGroundTime(long l)
    {
        this.lastGroundTime = l;
    }

    public long getLastGroundTimeDiff()
    {
        return System.currentTimeMillis() - this.lastGroundTime;
    }

    public long getLastBunnyTime()
    {
        return this.lastBunnyTime;
    }

    public void setLastBunnyTime(long l)
    {
        this.lastBunnyTime = l;
    }

    public long getLastBunnyTimeDiff()
    {
        return System.currentTimeMillis() - this.lastBunnyTime;
    }

    public int getHighestCPS()
    {
        return this.highestCPS;
    }

    public void setHighestCPS(int n)
    {
        this.highestCPS = n;
    }

    public int getLatestCPS()
    {
        return this.latestCPS;
    }

    public void setLatestCPS(int n)
    {
        this.latestCPS = n;
    }

    public int getCheck(Check check, int n)
    {
        if (!this.threshold.containsKey(check)) return 0;
        Map<Integer, Integer> map = this.threshold.get(check);
        if (!map.containsKey(n)) return 0;
        return map.get(n);
    }

    public int getBans(Check check)
    {
        return this.bans.getOrDefault(check, 0);
    }

    public List<Violation> getViolations(Check check)
    {
        return this.violations.get(check);
    }

    public Map<Check, List<Violation>> getViolations()
    {
        return new HashMap<Check, List<Violation>>(this.violations);
    }

    public void setCheck(Check check, int n, int n2)
    {
        if (n2 < 0)
        {
            n2 = 0;
        }
        if (this.threshold.containsKey(check))
        {
            Map<Integer, Integer> map = this.threshold.get(check);
            map.put(n, n2);
            this.threshold.put(check, map);
            return;
        }
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        hashMap.put(n, n2);
        this.threshold.put(check, hashMap);
    }

    public void addViolation(Check check, Violation violation)
    {
        if (this.violations.containsKey(check))
        {
            this.violations.get(check).add(violation);
            return;
        }
        ArrayList<Violation> arrayList = new ArrayList<Violation>();
        arrayList.add(violation);
        this.violations.put(check, arrayList);
    }

    public void removeViolations()
    {
        this.violations.clear();
    }

    public void removeBans()
    {
        this.bans.clear();
    }

    public void removeViolations(Check check)
    {
        this.violations.remove(check);
    }

    public void removeBans(Check check)
    {
        this.bans.put(check, 0);
    }

    public void addBan(Check check)
    {
        if (this.bans.containsKey(check))
        {
            this.bans.put(check, this.bans.get(check) + 1);
            return;
        }
        this.bans.put(check, 1);
    }

    public int getClicks()
    {
        return this.clicks;
    }

    public void setClicks(int n)
    {
        this.clicks = n;
    }

    public int getHits()
    {
        return this.hits;
    }

    public void setHits(int n)
    {
        this.hits = n;
    }
}

