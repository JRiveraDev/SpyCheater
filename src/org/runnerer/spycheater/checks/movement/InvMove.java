package org.runnerer.spycheater.checks.movement;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.ArrayList;

public class InvMove extends Check
{

    private ArrayList<Player> _moved = new ArrayList<>();
    private SpyCheater _antiCheat;

    public InvMove(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "InvMove", "Inventory Move", 3, 50, 65, 0);

        _antiCheat = antiCheat;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(antiCheat, () -> {
            _moved.clear();
        }, 0, 50);

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        Player player = (Player) event.getWhoClicked();

        if (event.getClick() == ClickType.CREATIVE && event.getAction() == InventoryAction.PLACE_ALL)
            return;

        if (!_moved.contains(player))
            return;

        event.getWhoClicked().closeInventory();

        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.LOW, "Inventory Move"));
    }

    @EventHandler
    public void move(PlayerMoveEvent event)
    {
        _moved.add(event.getPlayer());

        Bukkit.getScheduler().scheduleSyncDelayedTask(_antiCheat, () -> {
            _moved.remove(event.getPlayer());
        }, 5);
    }
}

