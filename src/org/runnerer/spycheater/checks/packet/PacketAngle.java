package org.runnerer.spycheater.checks.packet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;

public class PacketAngle
        extends Check
{

    public PacketAngle(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "AngleP", "KillAura", 110, 50, 3, 0);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onHit(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player))
        {
            return;
        }
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        LivingEntity livingEntity = (LivingEntity) entityDamageByEntityEvent.getEntity();
        if (player.getLocation().distanceSquared(livingEntity.getLocation()) < 3.0)
        {
            return;
        }
        float f = livingEntity.getLocation().toVector().subtract(player.getLocation().toVector()).angle(livingEntity.getLocation().getDirection());
    }
}

