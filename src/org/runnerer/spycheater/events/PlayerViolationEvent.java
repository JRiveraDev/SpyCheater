package org.runnerer.spycheater.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.runnerer.spycheater.checks.CheckType;

public class PlayerViolationEvent
        extends Event
{

    private static final HandlerList handlers = new HandlerList();
    private Player violatingPlayer;
    private CheckType violatingCheckType;
    private String message;

    public PlayerViolationEvent(Player player, CheckType checkType, String string)
    {
        this.violatingPlayer = player;
        this.violatingCheckType = checkType;
        this.message = string;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public String getMessage()
    {
        return this.message;
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

