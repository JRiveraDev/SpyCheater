package org.runnerer.spycheater.checks.pme;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.json.simple.parser.JSONParser;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.CheckType;
import org.runnerer.spycheater.violation.Violation;
import org.runnerer.spycheater.violation.ViolationPriority;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientPME
        extends Check
        implements PluginMessageListener,
        Listener
{

    public static final Map<UUID, Map<String, String>> forgeMods;
    public static String type;

    static
    {
        forgeMods = new HashMap<>();
    }

    private final JSONParser parser = new JSONParser();

    public ClientPME(SpyCheater antiCheat)
    {
        super(antiCheat, CheckType.OTHER, "PME", "PME: Type: " + type, 2, 2, 2, 0);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent)
    {
        this.getClientType(playerJoinEvent.getPlayer());
    }

    public void addVio(Player player)
    {
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "PME: " + (Object) player + "logged a client type of the PME list."));
    }

    public void onPluginMessageReceived(String string, Player player, byte[] arrby)
    {
        ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput((byte[]) arrby);
        if (!"ForgeMods".equals(byteArrayDataInput.readUTF())) return;
        String string2 = byteArrayDataInput.readUTF();
        try
        {
            Map map = (Map) this.parser.parse(string2);
            forgeMods.put(player.getUniqueId(), map);
            String string3 = this.getClientType(player);
            if (string3 == null) return;
            type = string3;
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "PME: " + (Object) player + "logged a client type of the PME list."));
            return;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent)
    {
        forgeMods.remove(playerQuitEvent.getPlayer().getUniqueId());
    }

    public String getClientType(Player player)
    {
        Map<String, String> map = forgeMods.get(player.getUniqueId());
        if (map == null) return null;
        if (map.containsKey("gc"))
        {
            type = "A";
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "PME: " + (Object) player + "logged a client type of the PME list."));
            return "A";
        }
        if (map.containsKey("ethylene"))
        {
            type = "B";
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "PME: " + (Object) player + "logged a client type of the PME list."));
            return "B";
        }
        if ("1.0".equals(map.get("OpenComputers")))
        {
            type = "C";
            this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "PME: " + (Object) player + "logged a client type of the PME list."));
            return "C";
        }
        if (!"1.7.6.git".equals(map.get("Schematica"))) return null;
        type = "D";
        this.getCore().addViolation(player, this, new Violation(this, ViolationPriority.HIGHEST, "PME: " + (Object) player + "logged a client type of the PME list."));
        return "D";
    }
}

