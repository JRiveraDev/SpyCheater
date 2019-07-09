package org.runnerer.spycheater.checks.movement.phase;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.plugin.Plugin;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;

import java.util.HashMap;

public class PhaseA
        extends Check
{

    public HashMap<String, Long> horse = new HashMap();
    public YamlConfiguration config;
    private PhaseTask task;

    public PhaseA(SpyCheater antiCheat, YamlConfiguration yamlConfiguration)
    {
        super(antiCheat, CheckType.MOVEMENT, "Phase", "Phase", 110, 50, -1, 0);
        this.config = yamlConfiguration;
        this.task = new PhaseTask(this);
        this.task.runTaskTimer((Plugin) antiCheat, 0L, 1L);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void setTeleported(PlayerTeleportEvent playerTeleportEvent)
    {
        if (playerTeleportEvent.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) return;
        this.task.addTeleportedPlayer(playerTeleportEvent.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void setTeleported(PlayerRespawnEvent playerRespawnEvent)
    {
        this.task.addTeleportedPlayer(playerRespawnEvent.getPlayer().getUniqueId());
    }

    @EventHandler
    public void vehExit(VehicleExitEvent vehicleExitEvent)
    {
        if (!(vehicleExitEvent.getExited() instanceof Player)) return;
        if (!vehicleExitEvent.getVehicle().getType().equals((Object) EntityType.HORSE)) return;
        Player player = (Player) vehicleExitEvent.getExited();
        this.horse.put(player.getUniqueId().toString(), System.currentTimeMillis() + 600L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (!this.horse.containsKey(player.getUniqueId().toString())) return;
        this.horse.remove(player.getUniqueId().toString());
    }
}

