package org.runnerer.spycheater.connection;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.events.PlayerDisconnectEvent;

public class ConnectionManager
        implements Listener
{

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(playerQuitEvent.getPlayer()));
    }

    @EventHandler
    public void onKick(PlayerKickEvent playerKickEvent)
    {
        Bukkit.getPluginManager().callEvent(new PlayerDisconnectEvent(playerKickEvent.getPlayer()));
    }
}

