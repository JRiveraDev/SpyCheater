package org.runnerer.spycheater.checks.killaura;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.events.PlayerAttackEvent;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class Turn
        extends Check
{

    private final Map<UUID, Integer> lastViolations = new WeakHashMap<UUID, Integer>();
    private final Map<UUID, Integer> lastHits = new WeakHashMap<UUID, Integer>();

    public Turn(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "TurnCheat", "Headsnap", 110, 7, 5, 0);
    }

    @EventHandler
    public void onAttack(PlayerAttackEvent playerAttackEvent)
    {
        Player player = playerAttackEvent.getPlayer();
        int n = SpyCheater.Instance.getTicksPassed();
        if (n - this.lastViolations.getOrDefault(player.getUniqueId(), 0) > 1) return;
        if (!(playerAttackEvent.getEntity().getLocation().toVector().subtract(player.getLocation().toVector()).normalize().dot(player.getLocation().getDirection()) > 0.97))
            return;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "Invalid Turn"));
    }

    @EventHandler(ignoreCancelled = true)
    public void onRotate(PlayerMoveEvent playerMoveEvent)
    {
        Location location = playerMoveEvent.getTo();
        float f = UtilMath.getYawDifference(playerMoveEvent.getFrom(), location);
        if (f < 77.5f)
        {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        int n = SpyCheater.Instance.getTicksPassed();
        this.lastViolations.put(player.getUniqueId(), n);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent playerQuitEvent)
    {
        UUID uUID = playerQuitEvent.getPlayer().getUniqueId();
        this.lastHits.remove(uUID);
        this.lastViolations.remove(uUID);
    }
}

