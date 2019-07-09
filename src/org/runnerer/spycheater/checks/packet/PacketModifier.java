package org.runnerer.spycheater.checks.packet;

import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketModifier
        extends Event
{

    private final PacketEvent a;
    private final Player player;

    public PacketModifier(PacketEvent packetEvent)
    {
        this.a = packetEvent;
        this.player = packetEvent.getPlayer();
    }

    public Entity a()
    {
        return (Entity) this.a.getPacket().getEntityModifier(this.player.getWorld()).read(0);
    }

    public PacketEvent a1()
    {
        return this.a;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public HandlerList getHandlers()
    {
        return new HandlerList();
    }
}

