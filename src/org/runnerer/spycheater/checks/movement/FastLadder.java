package org.runnerer.spycheater.checks.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;

public class FastLadder extends Check
{

    public static HashMap<Player, Integer> count = new HashMap();

    public FastLadder(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "FastLadder", "Fast Ladder", 3, 50, 16, 0);

    }

    @EventHandler
    public void onLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (count.containsKey(p)) {
            count.remove(p);
        }
    }

    @EventHandler
    public void checkFastLadder(PlayerMoveEvent e) {
        double OffsetY = UtilMath.offset(UtilMath.getVerticalVector(e.getFrom().toVector()), UtilMath.getVerticalVector(e.getTo().toVector()));
        double Limit = 0.13D;
        Player player = e.getPlayer();

        if (!count.containsKey(player))
        {
            count.put(player, Integer.valueOf(0));

            return;
        }

        if (player.getAllowFlight()) {
            return;
        }

        int sCount = ((Integer)count.get(player)).intValue();

        if (!UtilPlayer.isOnClimbable(player)) return;


        if (e.getFrom().getY() == e.getTo().getY()) {
            return;
        }

        double updown = e.getTo().getY() - e.getFrom().getY();
        if (updown <= 0.0D) {
            return;
        }

        if (OffsetY > Limit) {
            count.put(player, Integer.valueOf(sCount + 1));
            } else {
            count.put(player, Integer.valueOf(0));
        }

        long percent = Math.round((OffsetY - Limit) * 120.0D);

        if (sCount >= 13) {
            count.remove(player);
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Speed"));
            return;
        }
    }
}


