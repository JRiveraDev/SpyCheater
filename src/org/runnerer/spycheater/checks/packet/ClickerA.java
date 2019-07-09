package org.runnerer.spycheater.checks.packet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.*;

public class ClickerA
        extends Check
{

    public static int CPS_THRESHOLD = 1000000;
    public static int CPS_THRESHOLD_EXPERIMENTAL = 1000000;
    public static int CPS_EXPERIMENTAL = 25;
    public static int CPS_LOW = 35;
    public static int CPS_INVALID = 50;
    public static int CPS_VERY_INVALID = 70;
    public static int CPS_BAN_THIS_NIGGER = 100;
    public static int INSTA_BAN = 500;
    public static Map<Player, Long> lastTickWithPacketSent = new HashMap<Player, Long>();
    public static Map<Player, Boolean> gotLastTickPacket = new HashMap<Player, Boolean>();
    public static Map<Player, Long> experimentalHitsSinceLastCheck = new HashMap<Player, Long>();
    public static Map<Player, Long> lastTickCheck = new HashMap<Player, Long>();
    public static Map<Player, Long> hitsSinceLastCheck = new HashMap<Player, Long>();
    private static Map<UUID, List<Long>> highDetectionTimes = new HashMap<UUID, List<Long>>();
    private static Map<UUID, List<Long>> fastDetectionTimes = new HashMap<UUID, List<Long>>();
    private static Map<UUID, List<Long>> longDetectionTimes = new HashMap<UUID, List<Long>>();

    public ClickerA(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.AUTOCLICKER, "PClicker", "AutoClicker", 110, 7, 3, 0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent)
    {
        lastTickCheck.put(playerJoinEvent.getPlayer(), 0L);
        hitsSinceLastCheck.put(playerJoinEvent.getPlayer(), 0L);
        lastTickWithPacketSent.put(playerJoinEvent.getPlayer(), 0L);
        gotLastTickPacket.put(playerJoinEvent.getPlayer(), false);
        experimentalHitsSinceLastCheck.put(playerJoinEvent.getPlayer(), 0L);
        highDetectionTimes.put(playerJoinEvent.getPlayer().getUniqueId(), new ArrayList());
        fastDetectionTimes.put(playerJoinEvent.getPlayer().getUniqueId(), new ArrayList());
        longDetectionTimes.put(playerJoinEvent.getPlayer().getUniqueId(), new ArrayList());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent)
    {
        lastTickCheck.remove((Object) playerQuitEvent.getPlayer());
        hitsSinceLastCheck.remove((Object) playerQuitEvent.getPlayer());
        lastTickWithPacketSent.remove((Object) playerQuitEvent.getPlayer());
        gotLastTickPacket.remove((Object) playerQuitEvent.getPlayer());
        experimentalHitsSinceLastCheck.remove((Object) playerQuitEvent.getPlayer());
        highDetectionTimes.remove(playerQuitEvent.getPlayer().getUniqueId());
        fastDetectionTimes.remove(playerQuitEvent.getPlayer().getUniqueId());
        longDetectionTimes.remove(playerQuitEvent.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent playerInteractEvent)
    {
        if (playerInteractEvent.getAction() != Action.LEFT_CLICK_BLOCK)
        {
            if (playerInteractEvent.getAction() != Action.LEFT_CLICK_AIR) return;
        }
        this.handleExperimentalAutoClicking(playerInteractEvent.getPlayer(), "C");
        this.handleAutoClicking(playerInteractEvent.getPlayer(), "A");
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent entityDamageByEntityEvent)
    {
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player))
        {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player))
        {
            return;
        }
        this.handleExperimentalAutoClicking((Player) entityDamageByEntityEvent.getDamager(), "D");
        this.handleAutoClicking((Player) entityDamageByEntityEvent.getDamager(), "B");
    }

    private void handleAutoClicking(Player player, String string)
    {
        Long l = lastTickCheck.get((Object) player);
        Long l2 = hitsSinceLastCheck.get((Object) player);
        double d = UtilServer.getTps()[0];
        if (!(d > 18.0))
        {
            l2 = l2 + 1L;
            hitsSinceLastCheck.put(player, l2);
            return;
        }
        if (l2 >= (long) INSTA_BAN)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.format(String.valueOf(d) + " ", l2)));
        }
        if (l2 >= (long) CPS_BAN_THIS_NIGGER)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.format(String.valueOf(d) + " ", l2)));
        }
        if (l2 >= (long) CPS_VERY_INVALID)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.format(String.valueOf(d) + " ", l2)));
        } else if (l2 >= (long) CPS_INVALID)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.format(String.valueOf(d) + " ", l2)));
        } else if (l2 >= (long) CPS_LOW)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.format(String.valueOf(d) + " ", l2)));
        } else if (l2 >= (long) CPS_THRESHOLD)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.format(String.valueOf(d) + " ", l2)));
        }
        lastTickCheck.put(player, (long) d);
        hitsSinceLastCheck.put(player, 0L);
    }

    private void handleExperimentalAutoClicking(Player player, String string)
    {
        Long l = lastTickCheck.get((Object) player);
        Long l2 = lastTickWithPacketSent.get((Object) player);
        Boolean bl = gotLastTickPacket.get((Object) player);
        Long l3 = experimentalHitsSinceLastCheck.get((Object) player);
        double d = UtilServer.getTps()[0];
        if ((double) l2.longValue() != d)
        {
            gotLastTickPacket.put(player, false);
            lastTickWithPacketSent.put(player, (long) d);
            l3 = l3 + 1L;
            experimentalHitsSinceLastCheck.put(player, l3);
        } else if (!bl.booleanValue())
        {
            gotLastTickPacket.put(player, true);
        } else
        {
            l3 = l3 + 1L;
            experimentalHitsSinceLastCheck.put(player, l3);
        }
        if (!((double) (l + 20L) <= d)) return;
        if (l3 >= (long) CPS_EXPERIMENTAL)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.format(String.valueOf(d) + " ", l3)));
        } else if (l3 >= (long) CPS_THRESHOLD_EXPERIMENTAL)
        {
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, String.format(String.valueOf(d) + " ", l3)));
        }
        experimentalHitsSinceLastCheck.put(player, 0L);
    }
}

