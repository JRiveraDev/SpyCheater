package org.runnerer.spycheater.events.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public class WrapperPlayServerEntityVelocity
        extends AbstractPacket
{

    public static final PacketType TYPE = PacketType.Play.Server.ENTITY_VELOCITY;

    public WrapperPlayServerEntityVelocity()
    {
        super(new PacketContainer(TYPE), TYPE);
        this.handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerEntityVelocity(PacketContainer packetContainer)
    {
        super(packetContainer, TYPE);
    }

    public int getEntityID()
    {
        return (Integer) this.handle.getIntegers().read(0);
    }

    public void setEntityID(int n)
    {
        this.handle.getIntegers().write(0, n);
    }

    public Entity getEntity(World world)
    {
        return (Entity) this.handle.getEntityModifier(world).read(0);
    }

    public Entity getEntity(PacketEvent packetEvent)
    {
        return this.getEntity(packetEvent.getPlayer().getWorld());
    }

    public double getVelocityX()
    {
        return (double) ((Integer) this.handle.getIntegers().read(1)).intValue() / 8000.0;
    }

    public void setVelocityX(double d)
    {
        this.handle.getIntegers().write(1, ((int) (d * 8000.0)));
    }

    public double getVelocityY()
    {
        return (double) ((Integer) this.handle.getIntegers().read(2)).intValue() / 8000.0;
    }

    public void setVelocityY(double d)
    {
        this.handle.getIntegers().write(2, ((int) (d * 8000.0)));
    }

    public double getVelocityZ()
    {
        return (double) ((Integer) this.handle.getIntegers().read(3)).intValue() / 8000.0;
    }

    public void setVelocityZ(double d)
    {
        this.handle.getIntegers().write(3, ((int) (d * 8000.0)));
    }
}

