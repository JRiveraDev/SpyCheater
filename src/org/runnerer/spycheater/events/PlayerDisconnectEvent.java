package org.runnerer.spycheater.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDisconnectEvent extends Event
{

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    public PlayerDisconnectEvent(Player player)
    {
        this.player = player;
    }

    public static HandlerList getHandlerList()
    {
        return HANDLERS;
    }

    public HandlerList getHandlers()
    {
        return HANDLERS;
    }

    public Player getPlayer()
    {
        return this.player;
    }
}
