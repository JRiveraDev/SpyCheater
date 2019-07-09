package org.runnerer.spycheater.checks.autoclicker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilTime;

import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CPS
        extends Check
{

    public static CPS cps;
    private int maxCPS = 23;
    private Map<UUID, Long> clickTimes = new HashMap<UUID, Long>();

    public CPS(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.AUTOCLICKER, "HighCPS", "AutoClicker", 2, 50, 12, 0);
    }

    public static CPS cpsInstance()
    {
        return cps;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent playerInteractEvent)
    {
        long l;
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        double d = playerStats.getCPS();
        int n = playerStats.getCheck(this, 0);
        if (playerInteractEvent.getAction() == Action.LEFT_CLICK_AIR)
        {
            playerStats.setCPS(d += 1.0);
        } else if (playerInteractEvent.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            playerStats.setCPS(d += 0.5);
        }
        if (this.clickTimes.containsKey(player.getUniqueId()) && UtilTime.elapsed(l = this.clickTimes.get(player.getUniqueId()).longValue(), 1000L))
        {
            if (d >= (double) this.maxCPS)
            {
                ++n;
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.valueOf(d) + " Cps"));
            } else
            {
                --n;
            }
            playerStats.setLatestCPS((int) d);
            if (playerStats.getLatestCPS() > playerStats.getHighestCPS())
            {
                playerStats.setHighestCPS(playerStats.getLatestCPS());
            }
            playerStats.setCPS(0.0);
            this.clickTimes.remove(player.getUniqueId());
        }
        playerStats.setCheck(this, 0, n);
        if (this.clickTimes.containsKey(player.getUniqueId())) return;
        this.clickTimes.put(player.getUniqueId(), UtilTime.nowlong());
    }
}

