package org.runnerer.spycheater.checks.killaura.reach;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.CheatUtil;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class HeuristicFlows
        extends Check
{

    private double allowedDistance = 3.9;
    private int hitCount = 0;

    public HeuristicFlows(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "Heuristics", "Reach", 110, 50, 17, 0);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent paramEntityDamageByEntityEvent)
    {
        if (!(paramEntityDamageByEntityEvent.getEntity() instanceof Player))
        {
            return;
        }
        if (!(paramEntityDamageByEntityEvent.getDamager() instanceof Player))
        {
            return;
        }
        this.hitCount++;

        Bukkit.getScheduler().runTaskLater(SpyCheater.Instance, () -> {
                }
                , 300L);


        Player player1 = (Player) paramEntityDamageByEntityEvent.getDamager();
        Player player2 = (Player) paramEntityDamageByEntityEvent.getEntity();

        double d1 = CheatUtil.getHorizontalDistance(player1.getLocation(), player2.getLocation());
        double d2 = this.allowedDistance;

        int i = UtilServer.getPing(player1);
        int j = UtilServer.getPing(player2);

        int k = i + j / 2;

        int m = (int) (k * 0.0017D);

        d2 += m;

        if (!player2.isSprinting())
        {
            d2 += 0.2D;
        }

        if (!player2.isOnGround())
        {
            return;
        }


        for (PotionEffect potionEffect : player2.getActivePotionEffects())
        {
            if (potionEffect.getType().getId() == PotionEffectType.SPEED.getId())
            {

                int n = potionEffect.getAmplifier() + 1;
                d2 += 0.15D * n;

                break;
            }
        }
        for (PotionEffect potionEffect : player1.getActivePotionEffects())
        {
            if (potionEffect.getType().getId() == PotionEffectType.SPEED.getId())
            {

                int n = potionEffect.getAmplifier() + 1;
                d2 += 0.15D * n;

                break;
            }
        }
        if (d1 > d2)
        {
            getCore().addViolation(player1, this, new Violation(this, ViolationPriority.MEDIUM, String.valueOf(player1.getName()) + "'s distance (" + d1 + ") is greater than " + d2));
        }
    }
}

