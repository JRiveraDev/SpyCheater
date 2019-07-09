package org.runnerer.spycheater.update;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.common.utils.UtilServer;
import org.runnerer.spycheater.update.events.UpdateEvent;

import java.text.DecimalFormat;

public class Updater
        implements Runnable
{

    private SpyCheater Core;
    private int updater;
    private DecimalFormat format = new DecimalFormat("0.00");

    public Updater(SpyCheater antiCheat)
    {
        this.Core = antiCheat;
        this.updater = Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) this.Core, (Runnable) this, 0L, 1L);
    }

    public void Disable()
    {
        Bukkit.getScheduler().cancelTask(this.updater);
    }

    @Override
    public void run()
    {
        double d = UtilServer.getTps()[0];
        String string = "";
        String string2 = "";
        for (Check arrupdateType : SpyCheater.Instance.getChecks())
        {
            if (!arrupdateType.isActuallyEnabled()) continue;
            if (arrupdateType.getTpsDisable() >= d)
            {
                if (arrupdateType.isTempDisabled()) continue;
                string2 = String.valueOf(string2) + arrupdateType.getDisplayName() + ", ";
                arrupdateType.setTempDisabled(true);
                continue;
            }
            if (!arrupdateType.isTempDisabled()) continue;
            string = String.valueOf(string) + arrupdateType.getDisplayName() + ", ";
            arrupdateType.setTempDisabled(false);
        }

        for (UpdateType updateType : UpdateType.values())
        {
            if (updateType == null || !updateType.Elapsed()) continue;
            try
            {
                UpdateEvent exception = new UpdateEvent(updateType);
                Bukkit.getPluginManager().callEvent((Event) exception);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }
}

