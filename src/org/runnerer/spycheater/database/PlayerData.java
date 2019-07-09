package org.runnerer.spycheater.database;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData
{

    private static Map<UUID, PlayerData> playerData = new WeakHashMap<UUID, PlayerData>();
    private final Player player;
    public long useTime = 1L;
    public long armTime = 1L;

    private PlayerData(Player player)
    {
        this.player = player;
    }

    public static Map<UUID, PlayerData> getAllPlayers()
    {
        return playerData;
    }

    public static PlayerData getPlayerData(Player player)
    {
        UUID uUID = player.getUniqueId();
        if (playerData.containsKey(uUID))
        {
            return playerData.get(uUID);
        }
        PlayerData playerData = new PlayerData(player);
        PlayerData.playerData.put(uUID, playerData);
        return playerData;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null) return false;
        if (this.getClass() != object.getClass())
        {
            return false;
        }
        if (!(object instanceof PlayerData))
        {
            return false;
        }
        if (this.player != ((PlayerData) object).player) return false;
        return true;
    }

    public int hashCode()
    {
        return Objects.hash(new Object[]{this.player});
    }

    public String toString()
    {
        return "PlayerData{" + this.player.getName() + '}';
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public static class Hit
    {

        private final float accuracy;
        private final long time;
        private final Location location;

        public Hit(float f, Location location)
        {
            this.accuracy = f;
            this.time = System.currentTimeMillis();
            this.location = location;
        }

        public float getAccuracy()
        {
            return this.accuracy;
        }

        public long getTime()
        {
            return this.time;
        }

        public Location getLocation()
        {
            return this.location;
        }
    }

    public static class PlayerActionData
    {

        private static final Map<UUID, Long> combats = new HashMap<UUID, Long>();
        private static final Map<UUID, Long> join = new HashMap<UUID, Long>();
        private static final Map<UUID, Long> respawn = new HashMap<UUID, Long>();
        private static final Map<UUID, Long> flight = new HashMap<UUID, Long>();
        private static final Map<UUID, Long> teleports = new HashMap<UUID, Long>();
        private static final Map<UUID, Long> inventories = new HashMap<UUID, Long>();
        private static final Set<UUID> open = new HashSet<UUID>();

        public static void setLastCombat(Player player, long l)
        {
            combats.put(player.getUniqueId(), l);
        }

        public static void setLastJoin(Player player, long l)
        {
            join.put(player.getUniqueId(), l);
        }

        public static void setLastTeleport(Player player, long l)
        {
            teleports.put(player.getUniqueId(), l);
        }

        public static void setlastFlight(Player player, long l)
        {
            flight.put(player.getUniqueId(), l);
        }

        public static void setLastRespawn(Player player, long l)
        {
            respawn.put(player.getUniqueId(), l);
        }

        public static void setInventoryOpen(Player player)
        {
            open.add(player.getUniqueId());
        }

        public static void setInventoryClosed(Player player)
        {
            open.remove(player.getUniqueId());
        }

        public static void setLastInventoryOpened(Player player, long l)
        {
            inventories.put(player.getUniqueId(), l);
        }

        public static long getLastCombat(Player player)
        {
            return combats.getOrDefault(player.getUniqueId(), -1L);
        }

        public static long getLastTeleport(Player player)
        {
            return teleports.getOrDefault(player.getUniqueId(), -1L);
        }

        public static long getLastJoin(Player player)
        {
            return join.getOrDefault(player.getUniqueId(), -1L);
        }

        public static long getLastFlight(Player player)
        {
            return flight.getOrDefault(player.getUniqueId(), -1L);
        }

        public static long getLastRespawn(Player player)
        {
            return respawn.getOrDefault(player.getUniqueId(), -1L);
        }

        public static long getLastInventoryOpened(Player player)
        {
            return inventories.getOrDefault(player.getUniqueId(), -1L);
        }

        public static boolean hasInventoryOpen(Player player)
        {
            return open.contains(player.getUniqueId());
        }
    }

    public static class PlayerLog
    {

        private final long time;

        public PlayerLog(long l)
        {
            this.time = l;
            this.load();
        }

        public void load()
        {
        }

        public long getTime()
        {
            return this.time;
        }

        public void save()
        {
        }
    }

}

