package org.runnerer.spycheater.events;

import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketEntityUse
        extends Event
{

    private static final HandlerList handlers = new HandlerList();
    public EnumWrappers.EntityUseAction Action;
    public Player Attacker;
    public Entity Attacked;
    public long LastAttack;

    public PacketEntityUse(EnumWrappers.EntityUseAction entityUseAction, Player player, Entity entity)
    {
        this.Action = entityUseAction;
        this.Attacker = player;
        this.Attacked = entity;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }

    public EnumWrappers.EntityUseAction getAction()
    {
        return this.Action;
    }

    public Player getAttacker()
    {
        return this.Attacker;
    }

    public Entity getAttacked()
    {
        return this.Attacked;
    }

    public long getLastAttack()
    {
        return this.LastAttack;
    }

    public long getDiffAttack()
    {
        return System.currentTimeMillis() - this.LastAttack;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }
}

