package org.runnerer.spycheater.checks.combat;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FastBow
        extends Check
{

    private Map<UUID, Long> bowPull = new HashMap<UUID, Long>();
    private Long bowSpeed = 300L;

    public FastBow(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.COMBAT, "Fastbow", "Fast Bow", 2, 50, 1, 0);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent playerInteractEvent)
    {
        Player player = playerInteractEvent.getPlayer();
        if (player.getItemInHand() == null) return;
        if (!player.getItemInHand().getType().equals((Object) Material.BOW)) return;
        this.bowPull.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent projectileLaunchEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        if (!(projectileLaunchEvent.getEntity() instanceof Arrow))
        {
            return;
        }
        Arrow arrow = (Arrow) projectileLaunchEvent.getEntity();
        if (arrow.getShooter() == null)
        {
            return;
        }
        if (!(arrow.getShooter() instanceof Player))
        {
            return;
        }
        Player player = (Player) arrow.getShooter();
        if (!this.bowPull.containsKey(player.getUniqueId())) return;
        Long l = System.currentTimeMillis() - this.bowPull.get(player.getUniqueId());
        double d = arrow.getVelocity().length();
        Long l2 = this.bowSpeed;
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        int n = playerStats.getCheck(this, 0);
        int n2 = this.getThreshold();
        n = d > 2.5 && l < l2 ? ++n : --n;
        if (n > n2)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, l + "ms"));
            n = 0;
        }
        playerStats.setCheck(this, 0, n);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.bowPull.containsKey(player.getUniqueId())) return;
        this.bowPull.remove(player.getUniqueId());
    }
}

