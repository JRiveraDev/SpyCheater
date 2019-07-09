package org.runnerer.spycheater.combat;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.potion.PotionEffectType;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.events.PacketUseEntityEvent;
import org.runnerer.spycheater.events.PlayerAttackEvent;
import org.runnerer.spycheater.events.PlayerClickEvaluateEvent;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.update.UpdateType;
import org.runnerer.spycheater.update.events.UpdateEvent;

public class CombatManager
        implements Listener
{

    @EventHandler
    public void onPlayerPacket(PlayerAnimationEvent playerAnimationEvent)
    {
        if (!playerAnimationEvent.getAnimationType().equals((Object) PlayerAnimationType.ARM_SWING))
        {
            return;
        }
        Player player = playerAnimationEvent.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
        {
            return;
        }
        PlayerStats playerStats = SpyCheater.Instance.getPlayerStats(player);
        playerStats.setHits(playerStats.getClicks() + 1);
    }

    @EventHandler
    public void onPlayerPacket(PacketUseEntityEvent packetUseEntityEvent)
    {
        Player player = packetUseEntityEvent.getAttacker();
        if (!(packetUseEntityEvent.getAttacked() instanceof Player)) return;
        EnumWrappers.EntityUseAction entityUseAction = packetUseEntityEvent.getAction();
        if (entityUseAction != EnumWrappers.EntityUseAction.ATTACK) return;
        PlayerStats playerStats = SpyCheater.Instance.getPlayerStats(player);
        playerStats.setHits(playerStats.getHits() + 1);
    }

    @EventHandler
    public void onUpdate(UpdateEvent updateEvent)
    {
        if (updateEvent.getType() != UpdateType.SEC) return;
        for (Player player : Bukkit.getOnlinePlayers())
        {
            PlayerStats playerStats = SpyCheater.Instance.getPlayerStats(player);
            int n = playerStats.getClicks();
            int n2 = playerStats.getHits();
            Bukkit.getPluginManager().callEvent((Event) new PlayerClickEvaluateEvent(player, playerStats, n, n2));
            playerStats.setHits(0);
            playerStats.setClicks(0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        Entity entity = entityDamageByEntityEvent.getDamager();
        if (!(entity instanceof Player))
        {
            return;
        }
        PlayerAttackEvent playerAttackEvent = new PlayerAttackEvent((Player) entity, entityDamageByEntityEvent.getEntity());
        Bukkit.getPluginManager().callEvent(playerAttackEvent);
        if (!playerAttackEvent.isCancelled()) return;
        entityDamageByEntityEvent.setCancelled(true);
    }
}

