package org.runnerer.spycheater.checks.killaura.heuristic;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

public class HitBox
        extends Check
{

    private double HITBOX_LENGTH = 1.50;

    public HitBox(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "HitBox", "HitBox", 110, 50, 7, 0);
    }

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player))
        {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player))
        {
            return;
        }
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        Player player2 = (Player) entityDamageByEntityEvent.getDamager();
        if (this.hasInHitBox((LivingEntity) player2)) return;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Hit > " + Common.FORMAT_0x00.format(this.HITBOX_LENGTH)));
    }

    public boolean hasInHitBox(LivingEntity livingEntity)
    {
        boolean bl = false;
        Vector vector = livingEntity.getLocation().toVector().subtract(livingEntity.getLocation().toVector());
        Vector vector2 = livingEntity.getLocation().toVector().subtract(livingEntity.getLocation().toVector());
        if (!(livingEntity.getLocation().getDirection().normalize().crossProduct(vector).lengthSquared() < this.HITBOX_LENGTH))
        {
            if (!(livingEntity.getLocation().getDirection().normalize().crossProduct(vector2).lengthSquared() < this.HITBOX_LENGTH))
                return bl;
        }
        if (vector.normalize().dot(livingEntity.getLocation().getDirection().normalize()) >= 0.0) return true;
        if (!(vector2.normalize().dot(livingEntity.getLocation().getDirection().normalize()) >= 0.0)) return bl;
        return true;
    }
}

