package org.runnerer.spycheater.checks.autoclicker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilTime;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;

public class ClickPattern
        extends Check
{

    private int maxCPS = 16;
    private Map<Player, Long> clickTimes = new HashMap<Player, Long>();

    public ClickPattern(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.AUTOCLICKER, "ClickP", "AutoClicker", 2, 50, 6, 0);
    }

    @EventHandler
    public void useEntity(PlayerInteractEvent playerInteractEvent)
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
        double d2 = 0.0;
        if (playerInteractEvent.getAction() == Action.LEFT_CLICK_AIR)
        {
            playerStats.setCPS(d += 1.0);
        } else if (playerInteractEvent.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            playerStats.setCPS(d += 0.5);
        }
        if (d == 12.0)
        {
            d2 += 1.0;
        } else if (d == 13.0)
        {
            d2 -= 1.0;
        }
        if (d == 14.0)
        {
            d2 += 1.0;
        } else if (d == 15.0)
        {
            d2 -= 2.0;
        } else if (d == 16.0)
        {
            d2 += 2.0;
        }
        if (d2 >= 5.0)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(Common.FORMAT_0x00.format(d)) + " Click-Pattern"));
        }
        if (this.clickTimes.containsKey((Object) player) && UtilTime.elapsed(l = this.clickTimes.get((Object) player).longValue(), 1000L))
        {
            if (d >= (double) this.maxCPS)
            {
                ++n;
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, String.valueOf(Common.FORMAT_0x00.format(d)) + " CPS"));
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
            this.clickTimes.remove((Object) player);
        }
        playerStats.setCheck(this, 0, n);
        if (this.clickTimes.containsKey((Object) player)) return;
        this.clickTimes.put(player, UtilTime.nowlong());
    }
}

