package org.runnerer.spycheater.checks.movement;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.CheatUtil;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.*;

public class Spider extends Check
{
    private Map<UUID, Map.Entry<Long, Double>> AscensionTicks;

    public Spider(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Spider", "Spider", 6, 50, 5, 0);
    }


    @EventHandler
    public void CheckSpider(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getY() >= event.getTo().getY()) {
            return;
        }

        if (player.getAllowFlight()) {
            return;
        }
        if (player.getVehicle() != null)
        {
            return;
        }
        long Time = System.currentTimeMillis();
        double TotalBlocks = 0.0D;
        if (this.AscensionTicks.containsKey(player.getUniqueId())) {
            Time = ((Long)((Map.Entry)this.AscensionTicks.get(player.getUniqueId())).getKey()).longValue();
            TotalBlocks = ((Double)((Map.Entry)this.AscensionTicks.get(player.getUniqueId())).getValue()).doubleValue();
        }
        long MS = System.currentTimeMillis() - Time;
        double OffsetY = UtilMath.offset(UtilMath.getVerticalVector(event.getFrom().toVector()),
                UtilMath.getVerticalVector(event.getTo().toVector()));

        boolean ya = false;
        List<Material> Types = new ArrayList<Material>();
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.SOUTH).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.NORTH).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.WEST).getType());
        Types.add(player.getLocation().getBlock().getRelative(BlockFace.EAST).getType());
        for (Material Type : Types) {
            if (Type.isSolid() && Type != Material.LADDER && Type != Material.VINE) {
                ya = true;
                break;
            }
        }
        if (OffsetY > 0.0D) {
            TotalBlocks += OffsetY;
        }
        if (!ya || !CheatUtil.blocksNear(player)) {
            TotalBlocks = 0.0D;
        }

        PlayerStats playerStats = getCore().getPlayerStats(player);

        if (ya && (event.getFrom().getY() > event.getTo().getY() || playerStats.isOnGround())) {
            TotalBlocks = 0.0D;
        }
        double Limit = 0.5D;
        if (player.hasPotionEffect(PotionEffectType.JUMP)) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.JUMP)) {
                    int level = effect.getAmplifier() + 1;
                    Limit += Math.pow(level + 4.2D, 2.0D) / 16.0D;
                    break;
                }
            }
        }
        if (ya && TotalBlocks > Limit) {
            if (MS > 500L) {
                getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Spider"));
                Time = System.currentTimeMillis();
            }
        } else {
            Time = System.currentTimeMillis();
        }
        this.AscensionTicks.put(player.getUniqueId(),
                new AbstractMap.SimpleEntry(Long.valueOf(Time), Double.valueOf(TotalBlocks))); }

}
