package org.runnerer.spycheater.checks.movement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilMath;
import org.runnerer.spycheater.events.PacketPlayerEvent;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Timer
        extends Check
{

    private Map<UUID, Long> lastTimer = new HashMap<UUID, Long>();
    private Map<UUID, ArrayList<Long>> MS = new HashMap<UUID, ArrayList<Long>>();
    private Map<UUID, Integer> timerTicks = new HashMap<UUID, Integer>();

    public Timer(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.MOVEMENT, "Timer", "Timer", 6, 50, 7, 0);
    }

    @EventHandler
    public void PacketPlayer(PacketPlayerEvent paramPacketPlayerEvent)
    {
        Player player = paramPacketPlayerEvent.getPlayer();
        int i = 0;
        if (this.timerTicks.containsKey(player.getUniqueId()))
        {
            i = ((Integer) this.timerTicks.get(player.getUniqueId())).intValue();
        }
        if (this.lastTimer.containsKey(player.getUniqueId()))
        {
            long l = System.currentTimeMillis() - ((Long) this.lastTimer.get(player.getUniqueId())).longValue();
            ArrayList<Long> list = new ArrayList<Long>();
            if (this.MS.containsKey(player.getUniqueId()))
            {
                list = this.MS.get(player.getUniqueId());
            }
            list.add(Long.valueOf(l));
            if (list.size() == 20)
            {
                boolean bool = true;
                for (Long long1 : list)
                {
                    if (long1 < 1L)
                    {
                        bool = false;
                    }
                }
                Long _long = Long.valueOf(UtilMath.averageLong(list));
                if (_long.longValue() < 48L && bool)
                {
                    i++;
                } else
                {

                    i = 0;
                }
                this.MS.remove(player.getUniqueId());
            } else
            {

                this.MS.put(player.getUniqueId(), list);
            }
        }
        if (i > 4)
        {
            i = 0;
            getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGH, Common.FORMAT_0x00.format(i)));
        }
        this.lastTimer.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
        this.timerTicks.put(player.getUniqueId(), Integer.valueOf(i));
    }
}

