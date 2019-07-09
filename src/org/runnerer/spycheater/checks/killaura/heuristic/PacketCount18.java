package org.runnerer.spycheater.checks.killaura.heuristic;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilTime;
import org.runnerer.spycheater.events.PacketUseEntityEvent;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.*;

public class PacketCount18
        extends Check
{

    private Map<UUID, Long> LastMS = new HashMap<UUID, Long>();
    private Map<UUID, List<Long>> Clicks = new HashMap<UUID, List<Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public PacketCount18(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "KillAura", "KillAura", 110, 50, 6, 0);
    }

    @EventHandler
    public void onUseEntity(PacketUseEntityEvent packetUseEntityEvent)
    {
        Player player = packetUseEntityEvent.getAttacker();
        int n = 0;
        long l = System.currentTimeMillis();
        if (this.ClickTicks.containsKey(player.getUniqueId()))
        {
            n = this.ClickTicks.get(player.getUniqueId()).getKey();
            l = this.ClickTicks.get(player.getUniqueId()).getValue();
        }
        if (this.LastMS.containsKey(player.getUniqueId()))
        {
            long l2 = UtilTime.nowlong() - this.LastMS.get(player.getUniqueId());
            if (l2 > 500L || l2 < 5L)
            {
                this.LastMS.put(player.getUniqueId(), UtilTime.nowlong());
                return;
            }
            if (this.Clicks.containsKey(player.getUniqueId()))
            {
                List<Long> list = this.Clicks.get(player.getUniqueId());
                if (list.size() == 10)
                {
                    this.Clicks.remove(player.getUniqueId());
                    Collections.sort(list);
                    long l3 = list.get(list.size() - 1) - list.get(0);
                    if (l3 < 30L)
                    {
                        ++n;
                        l = System.currentTimeMillis();
                    }
                } else
                {
                    list.add(l2);
                    this.Clicks.put(player.getUniqueId(), list);
                }
            } else
            {
                ArrayList<Long> arrayList = new ArrayList<Long>();
                arrayList.add(l2);
                this.Clicks.put(player.getUniqueId(), arrayList);
            }
        }
        if (this.ClickTicks.containsKey(player.getUniqueId()) && UtilTime.elapsed(l, 5000L))
        {
            n = 0;
            l = UtilTime.nowlong();
        }
        if (n > 0)
        {
            n = 0;
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(n)));
        }
        this.LastMS.put(player.getUniqueId(), UtilTime.nowlong());
        this.ClickTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(n, l));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        if (this.LastMS.containsKey(player.getUniqueId()))
        {
            this.LastMS.remove(player.getUniqueId());
        }
        if (this.ClickTicks.containsKey(player.getUniqueId()))
        {
            this.ClickTicks.remove(player.getUniqueId());
        }
        if (!this.Clicks.containsKey(player.getUniqueId())) return;
        this.Clicks.remove(player.getUniqueId());
    }
}

