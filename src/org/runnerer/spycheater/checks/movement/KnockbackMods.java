package org.runnerer.spycheater.checks.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;

public class KnockbackMods
        extends Check
{

    private Map<Player, Long> lastSprintStart = new HashMap<Player, Long>();
    private Map<Player, Long> lastSprintStop = new HashMap<Player, Long>();

    public KnockbackMods(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.COMBAT, "KnockbackMods", "KnockbackMods", 3, 50, 12, 0);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastSprintStart.containsKey((Object) player))
        {
            this.lastSprintStart.remove((Object) player);
        }
        if (!this.lastSprintStop.containsKey((Object) player)) return;
        this.lastSprintStop.remove((Object) player);
    }

    @EventHandler
    public void Sprint(PlayerToggleSprintEvent playerToggleSprintEvent)
    {
        Player player = playerToggleSprintEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (playerStats.getLastDelayedPacketDiff() < 500L) return;
        if (playerStats.getLastPlayerPacketDiff() > 200L)
        {
            return;
        }
        if (playerToggleSprintEvent.isSprinting() && this.lastSprintStop.containsKey((Object) player))
        {
            int n = playerStats.getCheck(this, 0);
            int n2 = this.getThreshold();
            long l = System.currentTimeMillis() - this.lastSprintStop.get((Object) player);
            n = l < 5L ? ++n : (l > 1000L ? --n : (n -= 2));
            if (n > n2)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Modified KnockbackMods"));
                n = 0;
            }
            playerStats.setCheck(this, 0, n);
        }
        if (!playerToggleSprintEvent.isSprinting())
        {
            this.lastSprintStop.put(player, System.currentTimeMillis());
            return;
        }
        if (!playerToggleSprintEvent.isSprinting()) return;
        this.lastSprintStart.put(player, System.currentTimeMillis());
    }
}

