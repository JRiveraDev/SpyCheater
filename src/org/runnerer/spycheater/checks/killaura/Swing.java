/*
 * Decompiled with CFR <Could not determine version>.
 *
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.player.PlayerAnimationEvent
 *  org.bukkit.event.player.PlayerAnimationType
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package org.runnerer.spycheater.checks.killaura;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.*;

public class Swing
        extends Check
{

    ArrayList<UUID> combat = new ArrayList();
    private Map<UUID, Long> lastSwing = new HashMap<UUID, Long>();
    private Map<UUID, Long> lastRange = new HashMap<UUID, Long>();

    public Swing(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "SwingCheat", "KillAura", 110, 50, 2, 0);
    }

    @EventHandler
    public void onSwingArm(PlayerAnimationEvent playerAnimationEvent)
    {
        Entity entity;
        if (!playerAnimationEvent.getAnimationType().equals((Object) PlayerAnimationType.ARM_SWING))
        {
            return;
        }
        Player player = playerAnimationEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (this.lastSwing.containsKey(player.getUniqueId()) && System.currentTimeMillis() - this.lastSwing.get(player.getUniqueId()) < 500L)
        {
            this.lastSwing.put(player.getUniqueId(), System.currentTimeMillis());
            return;
        }
        if (!this.combat.contains(player.getUniqueId()))
        {
            return;
        }
        Iterator iterator = player.getNearbyEntities(6.0, 6.0, 6.0).iterator();
        if (iterator.hasNext())
        {
            entity = (Entity) iterator.next();
        }
        if (iterator.hasNext())
        {
            entity = (Entity) iterator.next();
            double d = player.getLocation().distance(entity.getLocation());
            if (d > 3.1 && d < 4.14)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Swing: " + Common.FORMAT_0x00.format(d)));
            }
        }
        this.lastSwing.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) return;
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        this.combat.add(player.getUniqueId());
        Bukkit.getScheduler().runTaskLater((Plugin) SpyCheater.Instance, () -> {
            boolean bl = this.combat.remove(player.getUniqueId());
        }, 10L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.combat.contains(player.getUniqueId()))
        {
            this.combat.remove(player.getUniqueId());
        }
        if (this.lastRange.containsKey(player.getUniqueId()))
        {
            this.lastRange.remove(player.getUniqueId());
        }
        if (!this.lastSwing.containsKey(player.getUniqueId())) return;
        this.lastSwing.remove(player.getUniqueId());
    }
}

