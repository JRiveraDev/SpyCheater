package org.runnerer.spycheater.ban;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.common.utils.C;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.stats.BanStats;
import org.runnerer.spycheater.update.UpdateType;
import org.runnerer.spycheater.update.events.UpdateEvent;

import java.util.HashMap;
import java.util.Map;


public class BanManagement
        implements Listener
{

    public SpyCheater Core;
    public Map<Player, Long> AutoBanQueue;
    public Map<Player, Check> AutoBanChecks;

    public BanManagement(SpyCheater paramAntiCheat)
    {
        this.AutoBanQueue = new HashMap();
        this.AutoBanChecks = new HashMap();
        this.Core = paramAntiCheat;

        this.Core.RegisterListener(this);
    }

    @EventHandler
    public void Update(UpdateEvent paramUpdateEvent)
    {
        if (!(paramUpdateEvent.getType() == UpdateType.SEC_02)) return;

        for (Player player : AutoBanQueue.keySet())
        {
            if (player == null)
            {
                this.AutoBanQueue.remove(player);
                continue;
            }

            forceban(player);
        }
    }


    public Map<Player, Long> getAutobanQueue()
    {
        return new HashMap(this.AutoBanQueue);
    }


    public void setBanCheck(Player paramPlayer, Check paramCheck)
    {
        if (this.AutoBanChecks.containsKey(paramPlayer)) this.AutoBanChecks.remove(paramPlayer);
        if (paramCheck != null) this.AutoBanChecks.put(paramPlayer, paramCheck);
    }

    public void cancelban(Player paramPlayer)
    {
        this.AutoBanQueue.remove(paramPlayer);
        this.AutoBanChecks.remove(paramPlayer);
        PlayerStats playerStats = this.Core.getPlayerStats(paramPlayer);
        playerStats.removeViolations();
        playerStats.removeBans();
    }

    public void forceban(Player paramPlayer)
    {

        Check check = this.AutoBanChecks.get(paramPlayer);

        if (check == null)
            return;

        if (this.AutoBanQueue.containsKey(paramPlayer))
        {
            this.AutoBanQueue.remove(paramPlayer);
        }

        PlayerStats playerStats = this.Core.getPlayerStats(paramPlayer);

        final String command = this.Core.banCommand.replaceAll("%player%", paramPlayer.getName()).trim();

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        BanStats.update();

        Bukkit.getServer().broadcastMessage("");
        Bukkit.getServer().broadcastMessage(C.Red + "SpyCheater " + C.Gray + "has banned " + C.Yellow + paramPlayer.getName() + C.Gray + " for Unfair Advantage.");
        Bukkit.getServer().broadcastMessage(C.Gray + "This user has cheated therefore this player gets punished.");
        Bukkit.getServer().broadcastMessage("");

        playerStats.removeBans();

        if (this.AutoBanChecks.containsKey(paramPlayer)) this.AutoBanChecks.remove(paramPlayer);
    }

    public void ban(Check paramCheck, Player paramPlayer)
    {
        if (paramCheck.isTempDisabled())
            return;

        setBanCheck(paramPlayer, paramCheck);
        forceban(paramPlayer);

    }
}
