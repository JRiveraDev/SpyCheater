package org.runnerer.spycheater.checks.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuraA
        extends Check
{

    Map<UUID, Integer> hits = new HashMap<UUID, Integer>();

    public AuraA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "PAura", "KillAura", 110, 50, 3, 0);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player))
        {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player))
        {
            return;
        }
        if (((Player) entityDamageByEntityEvent.getDamager()).hasLineOfSight(entityDamageByEntityEvent.getEntity()))
            return;
        if (this.isPlayerInCorner((Player) entityDamageByEntityEvent.getDamager())) return;
        int n = 0;
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        this.hits.putIfAbsent(entityDamageByEntityEvent.getDamager().getUniqueId(), 1);
        if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) >= 5)
        {
            n = 1;
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(n)));
        }
        if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) >= 10)
        {
            n = 2;
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(n)));
        }
        if (this.hits.get(entityDamageByEntityEvent.getDamager().getUniqueId()) <= 19) return;
        n = 3;
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGH, Common.FORMAT_0x00.format(n)));
        this.hits.remove(entityDamageByEntityEvent.getDamager().getUniqueId());
    }

    public boolean isPlayerInCorner(Player player)
    {
        int n;
        float f = player.getLocation().getYaw();
        if (f < 0.0f)
        {
            f += 360.0f;
        }
        if ((n = (int) ((double) ((f %= 360.0f) + 8.0f) / 22.5)) == 0) return false;
        if (n == 4) return false;
        if (n == 8) return false;
        if (n == 12) return false;
        return true;
    }
}

