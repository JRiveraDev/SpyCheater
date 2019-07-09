package org.runnerer.spycheater.events.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.google.common.base.Objects;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPacket
{

    protected PacketContainer handle;

    protected AbstractPacket(PacketContainer packetContainer, PacketType packetType)
    {
        if (packetContainer == null)
        {
            throw new IllegalArgumentException("Packet handle cannot be NULL.");
        }
        if (!Objects.equal((Object) packetContainer.getType(), (Object) packetType))
        {
            throw new IllegalArgumentException(packetContainer.getHandle() + " is not a packet of type " + (Object) packetType);
        }
        this.handle = packetContainer;
    }

    public PacketContainer getHandle()
    {
        return this.handle;
    }

    public void sendPacket(Player player)
    {
        try
        {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, this.getHandle());
            return;
        }
        catch (InvocationTargetException invocationTargetException)
        {
            throw new RuntimeException("Cannot send packet.", invocationTargetException);
        }
    }

    @Deprecated
    public void recievePacket(Player player)
    {
        try
        {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(player, this.getHandle());
            return;
        }
        catch (Exception exception)
        {
            throw new RuntimeException("Cannot recieve packet.", exception);
        }
    }

    public void receivePacket(Player player)
    {
        try
        {
            ProtocolLibrary.getProtocolManager().recieveClientPacket(player, this.getHandle());
            return;
        }
        catch (Exception exception)
        {
            throw new RuntimeException("Cannot recieve packet.", exception);
        }
    }
}

