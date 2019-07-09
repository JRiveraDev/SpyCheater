package org.runnerer.spycheater.checks.movement.fly;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.CheatUtil;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Fly
        extends Check
{

    ArrayList<Player> attacked = new ArrayList();
    private int verticalDownThreshold = 6;
    private int verticalUpThreshold = 3;
    private Map<UUID, Double> lastOffsetYUp = new HashMap<UUID, Double>();
    private Map<UUID, Double> lastOffsetYDown = new HashMap<UUID, Double>();
    private Map<UUID, Long> flyTicks = new HashMap<UUID, Long>();

    public Fly(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Fly", "Fly", 3, 50, 9, 0);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new NetworkFlightPacketAdapter());
    }

    @EventHandler
    public void CheckFly(PlayerMoveEvent playerMoveEvent)
    {
        long l;
        Player player = playerMoveEvent.getPlayer();
        if (player.getAllowFlight())
        {
            return;
        }
        if (player.getVehicle() != null)
        {
            return;
        }
        if (CheatUtil.isInWater(player))
        {
            return;
        }
        if (CheatUtil.isInWeb(player))
        {
            return;
        }
        if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK})) return;
        if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS}))
        {
            return;
        }
        if (CheatUtil.blocksNear(player))
        {
            if (!this.flyTicks.containsKey(player.getUniqueId())) return;
            this.flyTicks.remove(player.getUniqueId());
            return;
        }
        if (playerMoveEvent.getTo().getX() == playerMoveEvent.getFrom().getX() && playerMoveEvent.getTo().getZ() == playerMoveEvent.getFrom().getZ())
        {
            return;
        }
        if (playerMoveEvent.getTo().getY() != playerMoveEvent.getFrom().getY())
        {
            if (!this.flyTicks.containsKey(player.getUniqueId())) return;
            this.flyTicks.remove(player.getUniqueId());
            return;
        }
        long l2 = System.currentTimeMillis();
        if (this.flyTicks.containsKey(player.getUniqueId()))
        {
            l2 = this.flyTicks.get(player.getUniqueId());
        }
        if ((l = System.currentTimeMillis() - l2) > 500L)
        {
            this.flyTicks.remove(player.getUniqueId());
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, Common.FORMAT_0x00.format(l)));
            playerMoveEvent.setCancelled(true);
            player.teleport(playerMoveEvent.getFrom());
            return;
        }
        this.flyTicks.put(player.getUniqueId(), l2);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.lastOffsetYDown.containsKey(player.getUniqueId()))
        {
            this.lastOffsetYDown.remove(player.getUniqueId());
        }
        if (!this.lastOffsetYUp.containsKey(player.getUniqueId())) return;
        this.lastOffsetYUp.remove(player.getUniqueId());
    }

    @EventHandler
    public void onDmg(EntityDamageEvent entityDamageEvent)
    {
        if (!(entityDamageEvent.getEntity() instanceof Player)) return;
        final Player player = (Player) entityDamageEvent.getEntity();
        this.attacked.add(player);
        Bukkit.getScheduler().runTaskLater((Plugin) SpyCheater.Instance, new Runnable()
        {

            @Override
            public void run()
            {
                Fly.this.attacked.remove((Object) player);
            }
        }, 20L);
    }

    private class NetworkFlightPacketAdapter
            extends PacketAdapter
    {

        NetworkFlightPacketAdapter()
        {
            super((Plugin) Fly.this.getCore(), new PacketType[]{PacketType.Play.Client.ABILITIES});
        }

        public void onPacketReceiving(PacketEvent packetEvent)
        {
            Player player = packetEvent.getPlayer();
            if (player.getAllowFlight()) return;
            Fly.this.getCore().addViolation(player, Fly.this, new Violation(Fly.this, ViolationPriority.HIGHEST, "Illegal Fly Packets"));
        }
    }

}

