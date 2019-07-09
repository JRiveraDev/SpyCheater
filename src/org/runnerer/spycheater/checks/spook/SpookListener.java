package org.runnerer.spycheater.checks.spook;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.AngleY;

public class SpookListener
        extends Check
        implements Listener
{

    public SpookListener(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.OTHER, "Spook", "Spook", 2, 2, 2, 0);
    }

    @EventHandler
    public void playerSpookCheck(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player) || !(entityDamageByEntityEvent.getEntity() instanceof Player))
        {
            return;
        }

        Player player = (Player) entityDamageByEntityEvent.getDamager();
        Player player2 = (Player) entityDamageByEntityEvent.getEntity();
        float f = AngleY.getOffset(player, player2);
        SpookA.SpookAInstance().onAim(player, f);
    }
}

