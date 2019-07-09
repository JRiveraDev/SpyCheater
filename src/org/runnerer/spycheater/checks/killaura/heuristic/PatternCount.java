package org.runnerer.spycheater.checks.killaura.heuristic;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.common.utils.Common;
import org.runnerer.spycheater.common.utils.UtilTime;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.*;

public class PatternCount
        extends Check
{

    private Map<UUID, Long> LastMS = new HashMap<UUID, Long>();
    private Map<UUID, List<Long>> Clicks = new HashMap<UUID, List<Long>>();
    private Map<UUID, Map.Entry<Integer, Long>> ClickTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    public PatternCount(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.KILLAURA, "KillAura", "KillAura", 110, 50, 13, 0);
        this.onRegister();
    }

    public void onRegister()
    {
        this.registerPacketReceiving(PacketType.Play.Client.USE_ENTITY, packetEvent -> {
            if (packetEvent.getPacket().getEntityUseActions().read(0) != EnumWrappers.EntityUseAction.ATTACK)
            {
                return;
            }
            Player player = packetEvent.getPlayer();
            if (!(packetEvent.getPacket().getEntityModifier(player.getWorld()).read(0) instanceof LivingEntity))
            {
                return;
            }
            Player player2 = packetEvent.getPlayer();
            int n = 0;
            long l = System.currentTimeMillis();
            if (this.ClickTicks.containsKey(player2.getUniqueId()))
            {
                n = this.ClickTicks.get(player2.getUniqueId()).getKey();
                l = this.ClickTicks.get(player2.getUniqueId()).getValue();
            }
            if (this.LastMS.containsKey(player2.getUniqueId()))
            {
                long l2 = UtilTime.nowlong() - this.LastMS.get(player2.getUniqueId());
                if (l2 > 500L || l2 < 5L)
                {
                    this.LastMS.put(player2.getUniqueId(), UtilTime.nowlong());
                    return;
                }
                if (this.Clicks.containsKey(player2.getUniqueId()))
                {
                    List<Long> list = this.Clicks.get(player2.getUniqueId());
                    if (list.size() == 10)
                    {
                        this.Clicks.remove(player2.getUniqueId());
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
                        this.Clicks.put(player2.getUniqueId(), list);
                    }
                } else
                {
                    ArrayList<Long> arrayList = new ArrayList<Long>();
                    arrayList.add(l2);
                    this.Clicks.put(player2.getUniqueId(), arrayList);
                }
            }
            if (this.ClickTicks.containsKey(player2.getUniqueId()) && UtilTime.elapsed(l, 5000L))
            {
                n = 0;
                l = UtilTime.nowlong();
            }
            if (n > 0)
            {
                this.getCore().addViolation(player2, this, new Violation(this, ViolationPriority.MEDIUM, Common.FORMAT_0x00.format(n)));
                n = 0;
            }
            this.LastMS.put(player2.getUniqueId(), UtilTime.nowlong());
            this.ClickTicks.put(player2.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(n, l));
        });
    }
}

