package org.runnerer.spycheater.events.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import org.runnerer.spycheater.events.VelocityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VelocityPacketHandler
        extends PacketAdapter
{

    private Map<UUID, Long> lastVecUpdate = new HashMap();


    public VelocityPacketHandler(Plugin paramPlugin)
    {
        super(paramPlugin, new PacketType[]{PacketType.Play.Server.ENTITY_VELOCITY});
    }


    public void onPacketSending(PacketEvent paramPacketEvent)
    {
        WrapperPlayServerEntityVelocity wrapperPlayServerEntityVelocity = new WrapperPlayServerEntityVelocity(paramPacketEvent.getPacket());

        Player player = null;

        for (World world : Bukkit.getWorlds())
        {
            for (Entity entity : world.getEntities())
            {
                if (entity instanceof Player &&
                        entity.getEntityId() == wrapperPlayServerEntityVelocity.getEntityID())
                {
                    player = (Player) entity;
                }
            }
        }


        if (player != null)
        {
            if (!this.lastVecUpdate.containsKey(player.getUniqueId()))
            {
                this.lastVecUpdate.put(player.getUniqueId(), Long.valueOf(0L));
            }

            long l = System.currentTimeMillis() - (
                    (Long) this.lastVecUpdate.get(player.getUniqueId())).longValue();

            this.lastVecUpdate.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));

            if (l > 5L)
            {
                Vector vector = new Vector(wrapperPlayServerEntityVelocity.getVelocityX(), wrapperPlayServerEntityVelocity.getVelocityY(), wrapperPlayServerEntityVelocity.getVelocityZ());
                VelocityEvent velocityEvent = new VelocityEvent(player, vector);

                Bukkit.getPluginManager().callEvent(velocityEvent);
            }
        }
    }
}
