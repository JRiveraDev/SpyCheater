package org.runnerer.spycheater.common.utils;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NMSUtil
{

    public static EntityPlayer getNMSPlayer(Player player)
    {
        return ((CraftPlayer) player).getHandle();
    }
}
