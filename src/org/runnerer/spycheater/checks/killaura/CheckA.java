/*
 * Decompiled with CFR <Could not determine version>.
 *
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 */
package org.runnerer.spycheater.checks.killaura;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.events.PacketUseEntityEvent;

public class CheckA
        extends org.runnerer.spycheater.checks.Check
{

    private Float yaw;
    private Float lastYaw;
    private Float Diff;
    private int level;

    public CheckA(SpyCheater antiCheat, CheckType checkType, String string, String string2, int n, int n2, int n3, int n4)
    {
        super(antiCheat, checkType, string, string2, n, n2, n3, n4);
    }

    @EventHandler
    public void onPacket(PacketUseEntityEvent packetUseEntityEvent)
    {
        if (!this.isEnabled())
        {
            return;
        }
        if (!(packetUseEntityEvent.getAttacked() instanceof Player))
        {
            return;
        }
        if (!(packetUseEntityEvent.getAttacker() instanceof Player))
        {
            return;
        }
        Player player = packetUseEntityEvent.getAttacker();
        Player player2 = (Player) packetUseEntityEvent.getAttacked();
        this.lastYaw = this.yaw = Float.valueOf(player.getLocation().getPitch());
        this.Diff = Float.valueOf(Math.abs(this.yaw.floatValue() - this.lastYaw.floatValue()) % 180.0f);
        if (!(this.Diff.floatValue() > 0.1f)) return;
        if ((float) Math.round(this.Diff.floatValue() * 10.0f) * 0.1f != this.Diff.floatValue()) return;
        System.out.println("flag");
    }
}

