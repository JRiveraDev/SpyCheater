package org.runnerer.spycheater.common.utils;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

public class ActiveDistance
{

    private Location from;
    private Location to;
    private Double xDiff;
    private Double yDiff;
    private Double zDiff;
    private Double xzDiff;

    public ActiveDistance(PlayerMoveEvent playerMoveEvent)
    {
        this.from = playerMoveEvent.getFrom();
        this.to = playerMoveEvent.getTo();
        this.xDiff = (this.from.getX() > this.to.getX() ? this.from.getX() : this.to.getX()) - (this.from.getX() < this.to.getX() ? this.from.getX() : this.to.getX());
        this.yDiff = (this.from.getY() > this.to.getY() ? this.from.getY() : this.to.getY()) - (this.from.getY() < this.to.getY() ? this.from.getY() : this.to.getY());
        this.zDiff = (this.from.getZ() > this.to.getZ() ? this.from.getZ() : this.to.getZ()) - (this.from.getZ() < this.to.getZ() ? this.from.getZ() : this.to.getZ());
        this.xzDiff = this.getxDiff() > this.getzDiff() ? this.getxDiff() : this.getzDiff();
    }

    public ActiveDistance(Location location, Location location2)
    {
        this.to = location;
        this.from = location2;
        this.xDiff = Math.abs(location.getX() - location2.getX());
        this.yDiff = Math.abs(location.getY() - location2.getY());
        this.zDiff = Math.abs(location.getZ() - location2.getZ());
    }

    public Location getFrom()
    {
        return this.from;
    }

    public Location getTo()
    {
        return this.to;
    }

    public Double getxDiff()
    {
        return this.xDiff;
    }

    public Double getyDiff()
    {
        return this.yDiff;
    }

    public Double getzDiff()
    {
        return this.zDiff;
    }

    public Double getXzDiff()
    {
        return this.xzDiff;
    }
}

