package org.runnerer.spycheater.checks.killaura.heuristic;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.database.PlayerData;
import org.runnerer.spycheater.events.PlayerAttackEvent;
import org.runnerer.spycheater.events.PlayerDisconnectEvent;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.*;

public class AimAssist
        extends Check
{

    private final Map<UUID, Set<PlayerData.Hit>> hits = new WeakHashMap<UUID, Set<PlayerData.Hit>>();

    public AimAssist(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "AimAssister", "AimAssist", 15, 4, 18, 0);
    }

    @EventHandler
    public void onAttack(PlayerAttackEvent playerAttackEvent)
    {
        Player player = playerAttackEvent.getPlayer();
        Location location = player.getLocation();
        Location location2 = playerAttackEvent.getEntity().getLocation();
        float f = (float) location2.toVector().subtract(location.toVector()).normalize().dot(location.getDirection());
        Set set = this.hits.getOrDefault(player.getUniqueId(), new HashSet());
        set.add(new PlayerData.Hit(f, location));
        this.hits.put(player.getUniqueId(), set);
        if (set.size() <= 19) return;
        this.analyze(player);
    }

    private void analyze(Player player)
    {
        UUID uUID = player.getUniqueId();
        Set<PlayerData.Hit> set = this.hits.get(uUID);
        float f = 0.0f;
        float f2 = 0.0f;
        long l = 0L;
        Location location = null;
        int n = 0;
        int n2 = 0;
        double d = 0.0;
        Location location2 = null;
        for (PlayerData.Hit hit : set)
        {
            f = hit.getAccuracy();
            l = hit.getTime();
            location = hit.getLocation();
            if (location2 != null)
            {
                d += UtilMath.getXZDistance(location, location2);
            }
            if (f > 0.95f)
            {
                ++n;
                if ((double) (f2 - f) <= 0.005)
                {
                    ++n2;
                }
            }
            location2 = location;
            f2 = f;
        }
        if (n > 18 && d > 7.5)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "ahits: " + n + " distanceT: " + d));
        }
        this.hits.remove(uUID);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent playerDeathEvent)
    {
        this.hits.remove(playerDeathEvent.getEntity().getUniqueId());
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent playerDisconnectEvent)
    {
        this.hits.remove(playerDisconnectEvent.getPlayer().getUniqueId());
    }
}

