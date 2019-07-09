package org.runnerer.spycheater.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAttackEvent
        extends Event
        implements Cancellable
{

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Entity entity;
    private boolean isCancelled;

    public PlayerAttackEvent(Player player, Entity entity)
    {
        this.player = player;
        this.entity = entity;
        this.isCancelled = false;
    }

    public static HandlerList getHandlerList()
    {
        return HANDLERS;
    }

    public boolean isCancelled()
    {
        return this.isCancelled;
    }

    public void setCancelled(boolean bl)
    {
        this.isCancelled = bl;
    }

    public HandlerList getHandlers()
    {
        return HANDLERS;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public Entity getEntity()
    {
        return this.entity;
    }
}
