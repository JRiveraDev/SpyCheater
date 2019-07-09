package org.runnerer.spycheater.checks.movement.fly;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.CheatUtil;
import org.runnerer.spycheater.common.utils.UtilPlayer;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;

public class FlyB
        extends Check
{

    public int verticalDownThreshold = 6;
    public int verticalUpThreshold = 3;
    private Map<Player, Double> lastOffsetYUp = new HashMap<Player, Double>();
    private Map<Player, Double> lastOffsetYDown = new HashMap<Player, Double>();

    public FlyB(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Fly", "Fly", 3, 50, 9, 0);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new NetworkFlightPacketAdapter());
    }

    @EventHandler
    public void Move(PlayerMoveEvent playerMoveEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        Player player = playerMoveEvent.getPlayer();
        PlayerStats playerStats = this.getCore().getPlayerStats(player);
        if (player.getAllowFlight())
        {
            return;
        }
        if (player.isInsideVehicle())
        {
            return;
        }
        if (UtilPlayer.isHoveringOverWater(player, 1)) return;
        if (UtilPlayer.isHoveringOverWater(player, 0))
        {
            return;
        }
        if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.WEB})) return;
        if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WEB}))
        {
            return;
        }
        if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.SOUL_SAND})) return;
        if (UtilPlayer.isOnBlock(player, 2, new Material[]{Material.SOUL_SAND}))
        {
            return;
        }
        if (playerStats.getLastGround() == null) return;
        if (playerStats.isOnGround())
        {
            return;
        }
        if (UtilPlayer.isOnBlock(player, 0, new Material[]{Material.MELON_BLOCK})) return;
        if (UtilPlayer.isOnBlock(player, 1, new Material[]{Material.WOOD_STAIRS}))
        {
            return;
        }
        if (playerStats.getVelocityY() > 0.0)
        {
            return;
        }
        if (player.getLocation().getY() < 0.0)
        {
            return;
        }
        if (playerStats.getLastMountDiff() < 500L)
        {
            return;
        }
        if (CheatUtil.isInWeb(player))
        {
            return;
        }
        double d = playerMoveEvent.getTo().getY() - playerMoveEvent.getFrom().getY();
        if (d < 0.0 && playerStats.getLastWorldChangeDiff() > 3000L)
        {
            int n = playerStats.getCheck(this, 0);
            int n2 = this.verticalDownThreshold;
            d = Math.abs(d);
            double d2 = 0.0;
            if (this.lastOffsetYDown.containsKey((Object) player))
            {
                d2 = this.lastOffsetYDown.get((Object) player);
            }
            this.lastOffsetYDown.put(player, d);
            double d3 = Math.abs(d - d2);
            n = d3 < 0.02 ? ++n : (n -= 2);
            if (UtilPlayer.isOnGround(player, -1) || UtilPlayer.isOnGround(player, -2))
            {
                --n;
            }
            if (n > n2)
            {
                this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "*"));
                n = 0;
            }
            playerStats.setCheck(this, 0, n);
            return;
        }
        if (!(d > 0.0)) return;
        int n = playerStats.getCheck(this, 1);
        int n3 = this.verticalUpThreshold;
        double d4 = 0.0;
        if (this.lastOffsetYUp.containsKey((Object) player))
        {
            d4 = this.lastOffsetYUp.get((Object) player);
        }
        this.lastOffsetYUp.put(player, d);
        double d5 = Math.abs(d - d4);
        n = d5 < 0.02 ? ++n : (n -= 2);
        if (UtilPlayer.isOnGround(player, -1) || UtilPlayer.isOnGround(player, -2) || UtilPlayer.isOnGround(player, -3))
        {
            --n;
        }
        if (n > n3)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "*"));
            n = 0;
        }
        playerStats.setCheck(this, 1, n);
    }

    private class NetworkFlightPacketAdapter
            extends PacketAdapter
    {

        public NetworkFlightPacketAdapter()
        {
            super((Plugin) FlyB.this.getCore(), new PacketType[]{PacketType.Play.Client.ABILITIES});
        }

        public void onPacketReceiving(PacketEvent packetEvent)
        {
            Player player = packetEvent.getPlayer();
            if (player.getAllowFlight()) return;
            FlyB.this.getCore().addViolation(player, FlyB.this, new Violation(FlyB.this, ViolationPriority.HIGHEST, "Illegal Fly Packets"));
        }
    }

}

