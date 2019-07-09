package org.runnerer.spycheater.checks.movement.anti;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffectType;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoSlowdown
        extends Check
{

    private Map<UUID, Long> lastSneak = new HashMap<UUID, Long>();
    private int sneakThreshold = 15;

    public NoSlowdown(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "NoSlowdown", "No Slowdown", 1, 50, 4, 0);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemConsume(PlayerItemConsumeEvent playerItemConsumeEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerItemConsumeEvent.getPlayer();
        if (player.isInsideVehicle())
        {
            return;
        }
        int n = UtilServer.getPing(player);
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        int n2 = playerStats.getCheck(this, 0);

        if (n > 250)
        {
            return;
        }

        if (player.isSprinting())
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Consumed items while sprinting"));
            n2 = 0;
        }
        playerStats.setCheck(this, 0, n2);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BowShoot(EntityShootBowEvent entityShootBowEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        if (!(entityShootBowEvent.getEntity() instanceof Player))
        {
            return;
        }
        Player player = (Player) entityShootBowEvent.getEntity();
        if (player.isInsideVehicle())
        {
            return;
        }
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        n = player.isSprinting() ? ++n : --n;
        if (n > n2)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Shot a bow while sprinting"));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onEntityAction(PlayerToggleSneakEvent playerToggleSneakEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerToggleSneakEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        long l = 100000L;
        if (this.lastSneak.containsKey(player.getUniqueId()))
        {
            l = System.currentTimeMillis() - this.lastSneak.get(player.getUniqueId());
        }
        int n = playerStats.getCheck(this, 1);
        int n2 = this.sneakThreshold;
        n = l < 100L ? ++n : (n -= 5);
        if (n > n2)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Sneak Bypass"));
            n = 0;
        }
        playerStats.setCheck(this, 1, n);
        this.lastSneak.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onEntityAction2(PlayerToggleSprintEvent playerToggleSprintEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerToggleSprintEvent.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.BLINDNESS))
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Sprint while blind"));
            return;
        }
        if (player.getFoodLevel() > 3) return;
        if (player.getAllowFlight()) return;
        if (player.getGameMode().equals((Object) GameMode.CREATIVE)) return;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Sprint while hungry"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.lastSneak.containsKey(player.getUniqueId())) return;
        this.lastSneak.remove(player.getUniqueId());
    }
}

