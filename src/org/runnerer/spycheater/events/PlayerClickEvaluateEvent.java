package org.runnerer.spycheater.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.runnerer.spycheater.player.PlayerStats;

public class PlayerClickEvaluateEvent
        extends Event
{

    private static HandlerList handlerList = new HandlerList();
    private Player player;
    private PlayerStats profile;
    private int clicks;
    private int hits;

    public PlayerClickEvaluateEvent(Player player, PlayerStats playerStats, int n, int n2)
    {
        this.player = player;
        this.profile = playerStats;
        this.clicks = n;
        this.hits = n2;
    }

    public static HandlerList getHandlerList()
    {
        return handlerList;
    }

    public PlayerStats getProfile()
    {
        return this.profile;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public int getClicks()
    {
        return this.clicks;
    }

    public int getHits()
    {
        return this.hits;
    }

    public HandlerList getHandlers()
    {
        return handlerList;
    }
}

