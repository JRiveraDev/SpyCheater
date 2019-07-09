package org.runnerer.spycheater.checks.world.scaffold;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;

public class ScaffoldB extends Check
{

    private HashMap<Player, Integer> scaffoldBlocksPlaced = new HashMap<>();

    public ScaffoldB(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.OTHER, "Scaffold", "Scaffold", 3, 50, 19, 0);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(antiCheat, () -> {
            scaffoldBlocksPlaced.clear();

            for (Player p : Bukkit.getOnlinePlayers())
            {
                scaffoldBlocksPlaced.put(p, 0);
            }
        }, 0, 100);

    }

    @EventHandler
    public void addPlayer(PlayerJoinEvent event)
    {
        scaffoldBlocksPlaced.put(event.getPlayer(), 0);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();

        if (player.isFlying())
            return;

        Block placed = event.getBlockPlaced();

        if (!player.getLocation().subtract(0, 1, 0).getBlock().equals(placed))
            return;

        int blocksPlaced = scaffoldBlocksPlaced.get(player);
        scaffoldBlocksPlaced.replace(player, blocksPlaced, blocksPlaced + 1);

        if (blocksPlaced <= 5)
            return;

        getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, "Scaffold"));
    }
}
