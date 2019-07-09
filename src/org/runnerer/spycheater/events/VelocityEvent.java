package org.runnerer.spycheater.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class VelocityEvent
        extends Event
{

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Vector vec;

    public VelocityEvent(Player player, Vector vector)
    {
        this.player = player;
        this.vec = vector;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public Vector getVec()
    {
        return this.vec;
    }
}

