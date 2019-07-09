package org.runnerer.spycheater.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.runnerer.spycheater.checks.CheckType;

public class ViolationBroadcastEvent
        extends Event
        implements Cancellable
{

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled = false;
    private Player violatingPlayer;
    private CheckType violatingCheckType;

    public ViolationBroadcastEvent(Player player, CheckType checkType)
    {
        this.violatingPlayer = player;
        this.violatingCheckType = checkType;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
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
        return handlers;
    }

    public Player getViolatingPlayer()
    {
        return this.violatingPlayer;
    }

    public CheckType getViolatingCheckType()
    {
        return this.violatingCheckType;
    }
}

