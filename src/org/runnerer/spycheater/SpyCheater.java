package org.runnerer.spycheater;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.runnerer.spycheater.ban.BanManagement;
import org.runnerer.spycheater.checks.Check;
import org.runnerer.spycheater.checks.autoclicker.*;
import org.runnerer.spycheater.checks.combat.Criticals;
import org.runnerer.spycheater.checks.combat.FastBow;
import org.runnerer.spycheater.checks.combat.Regen;

import org.runnerer.spycheater.checks.killaura.Turn;
import org.runnerer.spycheater.checks.killaura.YawRate;
import org.runnerer.spycheater.checks.killaura.heuristic.AimAssist;
import org.runnerer.spycheater.checks.killaura.heuristic.PacketCount18;
import org.runnerer.spycheater.checks.killaura.pattern.PatternA;
import org.runnerer.spycheater.checks.killaura.reach.*;
import org.runnerer.spycheater.checks.movement.*;
import org.runnerer.spycheater.checks.movement.anti.NoFall;
import org.runnerer.spycheater.checks.movement.anti.NoSlowdown;
import org.runnerer.spycheater.checks.movement.anti.NoVelocity;
import org.runnerer.spycheater.checks.movement.anti.NoWeb;
import org.runnerer.spycheater.checks.movement.fly.Fly;
import org.runnerer.spycheater.checks.movement.fly.FlyB;
import org.runnerer.spycheater.checks.movement.fly.Glide;
import org.runnerer.spycheater.checks.movement.jesus.JesusA;
import org.runnerer.spycheater.checks.movement.jesus.JesusB;
import org.runnerer.spycheater.checks.movement.speed.SpeedA;
import org.runnerer.spycheater.checks.movement.speed.SpeedB;
import org.runnerer.spycheater.checks.movement.Timer;
import org.runnerer.spycheater.checks.packet.AuraA;
import org.runnerer.spycheater.checks.pme.ClientPME;
import org.runnerer.spycheater.checks.spook.SpookListener;
import org.runnerer.spycheater.checks.vape.AutoClickerV5x01;
import org.runnerer.spycheater.checks.vape.AutoClickerV5x0118;
import org.runnerer.spycheater.checks.vape.VapeListener;
import org.runnerer.spycheater.checks.world.scaffold.ScaffoldB;
import org.runnerer.spycheater.commands.commands.LogsCommand;
import org.runnerer.spycheater.common.utils.*;
import org.runnerer.spycheater.events.PacketPlayerEvent;
import org.runnerer.spycheater.events.PacketUseEntityEvent;
import org.runnerer.spycheater.events.PlayerViolationEvent;
import org.runnerer.spycheater.events.ViolationBroadcastEvent;
import org.runnerer.spycheater.events.handler.VelocityPacketHandler;
import org.runnerer.spycheater.logger.LoggerManager;
import org.runnerer.spycheater.player.PlayerStats;
import org.runnerer.spycheater.stats.BanStats;
import org.runnerer.spycheater.update.Updater;
import org.runnerer.spycheater.violation.Violation;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SpyCheater
        extends JavaPlugin
        implements Listener
{

    public static SpyCheater Instance;

    public org.runnerer.spycheater.common.utils.Config Config;
    public String banCommand = "ban %player% Cheating";
    public BanManagement Autoban;
    public List<Check> Checks = new ArrayList<Check>();
    public Map<UUID, org.runnerer.spycheater.player.PlayerStats> PlayerStats = new HashMap<UUID, PlayerStats>();
    private ArrayList<Player> Alerts = new ArrayList<Player>();
    private int currentTick;
    private int ticksPassed;
    private boolean configVersion = false;

    private static void accessV1(SpyCheater antiCheat, int n)
    {
        antiCheat.currentTick = n;
    }

    private static void accessV3(SpyCheater antiCheat, int n)
    {
        antiCheat.ticksPassed = n;
    }

    public void onEnable()
    {
        File file = new File(this.getDataFolder() + "/logs");
        file.mkdirs();
        String string = this.getServer().getClass().getName().split("org.bukkit.craftbukkit.")[1];
        Instance = this;
        Instance.RegisterListener(this);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new VelocityPacketHandler((Plugin) this));
        new Updater(this);
        new LoggerManager(this);

        this.getCommand("logs").setExecutor((CommandExecutor) new LogsCommand());
        this.Config = new Config(this, "", "config");
        this.Config.setDefault("bans-overall", 0);
        this.Config.setDefault("ban.command", "ban %player% Unfair Advantage");


        this.banCommand = this.Config.getConfig().getString("ban.command");

        this.Autoban = new BanManagement(this);
        this.runCheckManagment();
        VapeListener vapeListener = new VapeListener(this);
        this.getServer().getMessenger().registerIncomingPluginChannel((Plugin) this, "Vape", (vapeListener));
        this.getServer().getPluginManager().registerEvents(vapeListener, this);
        this.getServer().getMessenger().registerIncomingPluginChannel((Plugin) this, "BungeeCord", new ClientPME(this));
        this.getServer().getPluginManager().registerEvents(new ClientPME(this), (Plugin) this);

        for (Player player : Bukkit.getServer().getOnlinePlayers())
        {
            if (!player.hasPermission("spycheater.staff")) continue;
            this.toggle(player);
        }
        new BukkitRunnable()
        {

            public void run()
            {
                SpyCheater.accessV1(SpyCheater.this, SpyCheater.this.currentTick >= 20 ? 1 : SpyCheater.this.currentTick + 1);
                SpyCheater antiCheat = SpyCheater.this;
                SpyCheater.accessV3(antiCheat, antiCheat.ticksPassed + 1);
            }
        }.runTaskTimer((Plugin) this, 1L, 1L);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Client.ARM_ANIMATION})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                PlayerStats playerStats = SpyCheater.this.getPlayerStats(player);

                if (playerStats.getAnimationSpam() == -1)
                {
                    packetEvent.setCancelled(true);
                }
                int n = 3000;

                long l = System.currentTimeMillis() - playerStats.getLastAnimation();
                if (l < 5L)
                {
                    playerStats.setAnimationSpam(playerStats.getAnimationSpam() + 1);
                    if (playerStats.getAnimationSpam() > n)
                    {
                        playerStats.setAnimationSpam(-1);
                        packetEvent.setCancelled(true);
                        SpyCheater.this.log(String.valueOf(String.valueOf(player.getName())) + " attempted Crash Exploit (Animation)");

                        UtilServer.asyncKick(player, "Timed out");
                    }
                } else
                {
                    playerStats.setAnimationSpam(0);
                }

                playerStats.setLastAnimation(System.currentTimeMillis());
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Client.BLOCK_PLACE})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                PlayerStats playerStats = SpyCheater.this.getPlayerStats(player);

                if (playerStats.getBlockPlacementSpam() == -1)
                {
                    packetEvent.setCancelled(true);
                }
                int n = 3000;

                long l = System.currentTimeMillis() - playerStats.getLastBlockPlacementPacket();
                if (l < 5L)
                {
                    playerStats.setBlockPlacementSpam(playerStats.getBlockPlacementSpam() + 1);
                    if (playerStats.getBlockPlacementSpam() > n)
                    {
                        playerStats.setBlockPlacementSpam(-1);
                        packetEvent.setCancelled(true);
                        SpyCheater.this.log(player.getName() + " attempted Crash Exploit (Block Placement)");
                        UtilServer.asyncKick(player, "Timed out");
                    }
                } else
                {
                    playerStats.setBlockPlacementSpam(0);
                }

                playerStats.setLastBlockPlacementPacket(System.currentTimeMillis());
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Client.POSITION_LOOK})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event) new PacketPlayerEvent(player, (Double) packetEvent.getPacket().getDoubles().read(0), (Double) packetEvent.getPacket().getDoubles().read(1), (Double) packetEvent.getPacket().getDoubles().read(2), ((Float) packetEvent.getPacket().getFloat().read(0)).floatValue(), ((Float) packetEvent.getPacket().getFloat().read(1)).floatValue()));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Client.LOOK})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event) new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), ((Float) packetEvent.getPacket().getFloat().read(0)).floatValue(), ((Float) packetEvent.getPacket().getFloat().read(1)).floatValue()));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Client.POSITION})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event) new PacketPlayerEvent(player, (Double) packetEvent.getPacket().getDoubles().read(0), (Double) packetEvent.getPacket().getDoubles().read(1), (Double) packetEvent.getPacket().getDoubles().read(2), player.getLocation().getYaw(), player.getLocation().getPitch()));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Client.FLYING})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event) new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Client.KEEP_ALIVE})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                PlayerStats playerStats = SpyCheater.this.getPlayerStats(player);
                playerStats.setLastReceivedKeepAlive(System.currentTimeMillis());
                playerStats.setLastReceivedKeepAliveID((Integer) packetEvent.getPacket().getIntegers().read(0));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Server.KEEP_ALIVE})
        {

            public void onPacketSending(PacketEvent packetEvent)
            {
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                PlayerStats playerStats = SpyCheater.this.getPlayerStats(player);
                playerStats.setLastSentKeepAlive(System.currentTimeMillis());
                playerStats.setLastSentKeepAliveID((Integer) packetEvent.getPacket().getIntegers().read(0));
            }
        });
        final ConcurrentHashMap<Player, Integer> concurrentHashMap = new ConcurrentHashMap();
        new BukkitRunnable()
        {

            public void run()
            {
                for (Player player : concurrentHashMap.keySet())
                {
                    if (player.isOnline()) continue;
                    concurrentHashMap.remove((Object) player);
                }
            }
        }.runTaskTimerAsynchronously((Plugin) this, 20L, 20L);
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter((Plugin) this, new PacketType[]{PacketType.Play.Client.USE_ENTITY})
        {

            public void onPacketReceiving(PacketEvent packetEvent)
            {
                PacketContainer packetContainer = packetEvent.getPacket();
                Player player = packetEvent.getPlayer();
                if (player == null)
                {
                    return;
                }
                EnumWrappers.EntityUseAction entityUseAction = null;
                try
                {
                    entityUseAction = packetContainer.getEntityUseActions().read(0);
                }
                catch (Exception exception)
                {

                }
                if (entityUseAction == null)
                {
                    return;
                }
                int n = packetContainer.getIntegers().read(0);
                Entity entity = null;
                for (Entity entity2 : UtilServer.getEntities(player.getWorld()))
                {
                    if (entity2.getEntityId() != n) continue;
                    entity = entity2;
                }
                long l = -1L;
                if (concurrentHashMap.containsKey((Object) player))
                {
                    l = concurrentHashMap.get((Object) player);
                }
                Bukkit.getServer().getPluginManager().callEvent((Event) new PacketUseEntityEvent(entityUseAction, player, entity, l));
            }
        });

        System.out.print("Registered packets!");

        // MOVEMENT.
        this.Checks.add(new NoSlowdown(this));
        this.Checks.add(new JesusA(this));
        this.Checks.add(new JesusB(this));
        this.Checks.add(new Ascension(this));
        this.Checks.add(new Fly(this));
        this.Checks.add(new Glide(this));
        this.Checks.add(new VClip(this));
        this.Checks.add(new HClip(this));
        this.Checks.add(new SpeedA(this));
        this.Checks.add(new SpeedB(this));
        this.Checks.add(new Timer(this));
        this.Checks.add(new TimerB(this));
        this.Checks.add(new Change(this));
        this.Checks.add(new Step(this));
        this.Checks.add(new NoFall(this));
        this.Checks.add(new FlyB(this));
        this.Checks.add(new NoWeb(this));

        // COMBAT.
        this.Checks.add(new NoVelocity(this));
        this.Checks.add(new ReachA(this));
        this.Checks.add(new ReachB(this));
        this.Checks.add(new FastBow(this));
        this.Checks.add(new Regen(this));
        this.Checks.add(new CPS(this));
        this.Checks.add(new APS(this));
        this.Checks.add(new PatternA(this));
        this.Checks.add(new YawRate(this));
        this.Checks.add(new AutoDetection0x01(this));
        this.Checks.add(new AutoClickerV5x01(this));
        this.Checks.add(new AutoDetection0x218(this));
        this.Checks.add(new AutoClickerV5x0118(this));
        this.Checks.add(new PacketCount18(this));
        this.Checks.add(new AuraA(this));
        this.Checks.add(new DataReach(this));
        this.Checks.add(new org.runnerer.spycheater.checks.killaura.huzuni.HuzuniAura(this));
        this.Checks.add(new ClientPME(this));
        this.Checks.add(new PatternDetection(this));
        this.Checks.add(new Turn(this));
        this.Checks.add(new AimAssist(this));
        this.Checks.add(new Criticals(this));


        // WORLD.
        this.Checks.add(new ScaffoldB(this));

        // Heuristic Based Data
        this.Checks.add(new Heuristics(this));
        this.Checks.add(new HeuristicFlows(this));

        // Clients
        this.Checks.add(new VapeListener(this));
        this.Checks.add(new SpookListener(this));

        System.out.print("Registered checks!");

        // Register all check listeners.
        for (Check check : this.Checks)
        {
            RegisterListener(check);
        }

        // Loads ban statistics
        BanStats.load();

        // Announcement
        new BukkitRunnable()
        {
            public void run()
            {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(C.Green + C.Bold + "SPYCHEATER ANNOUNCEMENT");
                Bukkit.broadcastMessage("  I have punished " + C.Red + C.Bold + BanStats.bansToday + C.White + " players in the last day.");
                Bukkit.broadcastMessage("  I have punished a total of " + C.Red + C.Bold + BanStats.bansOverall + C.White + " players.");
                Bukkit.broadcastMessage("  Blacklisted modifications are not allowed on this network and will get you punished!");
                Bukkit.broadcastMessage("");
            }
        }.runTaskTimerAsynchronously(this, 36000L, 36000L);
    }

    public void onDisable()
    {
        BanStats.save();
        this.getMainConfig().save();
    }

    public void reloadConfig()
    {
        this.getMainConfig().reload();

        this.banCommand = this.Config.getConfig().getString("ban.command");

    }

    @EventHandler
    public void Join(PlayerJoinEvent playerJoinEvent)
    {
        Player player = playerJoinEvent.getPlayer();
        PlayerStats playerStats = this.getPlayerStats(player);
        playerStats.setAnimationSpam(0);
        playerStats.setBlockPlacementSpam(0);
        playerStats.setLastReceivedKeepAlive(0L);
        playerStats.setLastReceivedKeepAliveID(0);
        playerStats.setLastSentKeepAlive(0L);
        playerStats.setLastSentKeepAliveID(0);
        if (!player.hasPermission("spycheater.staff")) return;
        this.toggle(player);
    }

    @EventHandler
    public void Quit(PlayerQuitEvent playerQuitEvent)
    {
        Player player = playerQuitEvent.getPlayer();
        PlayerStats playerStats = this.getPlayerStats(player);
        playerStats.setAnimationSpam(0);
        playerStats.setBlockPlacementSpam(0);
        playerStats.setLastReceivedKeepAlive(0L);
        playerStats.setLastReceivedKeepAliveID(0);
        playerStats.setLastSentKeepAlive(0L);
        playerStats.setLastSentKeepAliveID(0);
    }

    public void log(String string)
    {
        System.out.println(string);
    }

    public BanManagement getAutoban()
    {
        return this.Autoban;
    }

    public Config getMainConfig()
    {
        return this.Config;
    }

    public List<Check> getChecks()
    {
        return new ArrayList<Check>(this.Checks);
    }

    public Check getCheck(String string)
    {
        for (Check check : this.getChecks())
        {
            if (!check.getName().equalsIgnoreCase(string)) continue;
            return check;
        }
        return null;
    }

    public List<Player> getPlayers()
    {
        return new ArrayList<Player>((Collection) this.Alerts);
    }

    private void runCheckManagment()
    {
    }

    public void toggle(Player player)
    {
        List<Player> list = this.getPlayers();
        if (list.contains(player))
        {
            list.remove(player);
            return;
        }
        Alerts.add(player);
    }

    public PlayerStats getPlayerStats(UUID uUID)
    {
        if (this.PlayerStats.containsKey(uUID))
        {
            return this.PlayerStats.get(uUID);
        }
        PlayerStats playerStats = new PlayerStats(uUID);
        this.PlayerStats.put(uUID, playerStats);
        return playerStats;
    }

    public PlayerStats getPlayerStats(Player player)
    {
        return this.getPlayerStats(player.getUniqueId());
    }

    public void addBan(Player player, Check check, Violation violation)
    {
        if (check.getBans() <= 0)
        {
            return;
        }
        PlayerStats playerStats = this.getPlayerStats(player);
        playerStats.addBan(check);
        List<Player> list = this.getPlayers();

        for (Player player2 : list)
        {
            player2.sendMessage(C.Red + C.Scramble + "#" + C.White + " " + C.Red + C.Bold + "SPYCHEATER > " + C.Gray + C.Green + player.getName() + C.Gray + " failed " + C.Green + check.getDisplayName() + C.Gray + ", " + C.Green + "Level " + playerStats.getViolations(check).size() + "" + C.Gray + ".");
        }


        if (check.getBans() == playerStats.getViolations(check).size())
        {
            this.getAutoban().ban(check, player);
        }
    }

    public void addViolation(Player paramPlayer, Check paramCheck, Violation paramViolation)
    {
        PlayerStats playerStats = getPlayerStats(paramPlayer);
        playerStats.addViolation(paramCheck, paramViolation);
        String str1 = paramPlayer.getName() + " failed " + paramCheck.getDisplayName() + ".";
        byte b = 0;
        for (Violation violation : playerStats.getViolations(paramCheck))
        {
            if (violation.isUnused())
            {
                continue;
            }
            if (violation.getDiffWhenCreated() > violation.getCheck().getExpiration())
            {
                violation.setUnused(true);
                continue;
            }
            switch (violation.getPriority())
            {
                case LOW:
                    b += 1;
                    break;

                case MEDIUM:
                    b += 1;
                    break;

                case HIGH:
                    b += 1;
                    break;

                case HIGHEST:
                    b += 1;
                    break;
            }
        }

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss (a) ");

        dumpCheat(paramPlayer, String.valueOf(simpleDateFormat.format(date)) +
                paramPlayer.getName() + " is using " + paramCheck.getDisplayName() + " [" + String.valueOf(playerStats.getViolations(paramCheck).size()) + "].");

        ViolationBroadcastEvent violationBroadcastEvent = new ViolationBroadcastEvent(paramPlayer, paramCheck.getCheckType());
        Bukkit.getPluginManager().callEvent(violationBroadcastEvent);

        if (!violationBroadcastEvent.isCancelled())
        {
            for (Player player : getPlayers())
            {
                player.sendMessage(C.Red + C.Scramble + "#" + C.White + " " + C.Red + C.Bold + "SPYCHEATER > " + C.Gray + C.Green + paramPlayer.getName() + C.Gray + " failed " + C.Green + paramCheck.getDisplayName() + C.Gray + ", " + C.Green + "Level " + playerStats.getViolations(paramCheck).size() + "" + C.Gray + ".");
            }


            if (paramCheck.getBans() == playerStats.getViolations(paramCheck).size())
            {
                addBan(paramPlayer, paramCheck, paramViolation);


                if (playerStats.getViolations(paramCheck) != null)
                {
                    for (Violation violation : playerStats.getViolations(paramCheck))
                    {
                        violation.setUnused(true);
                    }
                }
            }


            Bukkit.getPluginManager().callEvent(new PlayerViolationEvent(paramPlayer, paramCheck.getCheckType(), C.Red + C.Scramble + "#" + C.White + " " + C.Red + C.Bold + "SPYCHEATER > " + C.Gray + C.Green + paramPlayer.getName() + C.Gray + " failed " + C.Green + paramCheck.getDisplayName() + C.Gray + ", " + C.Green + "Level " + playerStats.getViolations(paramCheck).size() + "" + C.Gray + "."));
            log(str1);
        }
    }

    public void RegisterListener(Listener listener)
    {
        this.getServer().getPluginManager().registerEvents(listener, (Plugin) this);
    }

    public Map<UUID, PlayerStats> getPlayerStats()
    {
        return this.PlayerStats;
    }

    public int getCurrentTick()
    {
        return this.currentTick;
    }

    public int getTicksPassed()
    {
        return this.ticksPassed;
    }

    private void dumpCheat(Player player, String string)
    {
        File file = new File(this.getDataFolder() + "/logs/" + player.getUniqueId() + ".txt");
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException iOException)
            {
                iOException.printStackTrace();
                System.out.println("[Anticheat] Failed trying to create a logs file.");
            }
        }
        try
        {
            FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println(string);
            printWriter.close();
            return;
        }
        catch (IOException iOException)
        {
            iOException.printStackTrace();
        }
    }

    public void attemptToRead(Player player, String string)
    {
        File file = new File(this.getDataFolder() + "/logs/" + string + ".txt");
        if (!file.exists())
        {
            player.sendMessage(ChatUtil.colorize("&7[&9SpyCheater&7] " + C.Gray + "Failed to find " + string + "'s logs."));
        }
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;
        try
        {
            try
            {
                String string2;
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                bufferedReader = new BufferedReader(new FileReader(file));
                player.sendMessage(ChatUtil.colorize("&7[&9SpyCheater&7] " + C.Gray + "Listing " + string + "'s cheating logs."));
                while ((string2 = bufferedReader.readLine()) != null)
                {
                    player.sendMessage(C.Gray + string2);
                }
                return;
            }
            catch (IOException iOException)
            {
                player.sendMessage(ChatUtil.colorize("&7[&9SpyCheater&7] " + C.Gray + "Failed to find " + string + "'s logs."));
                try
                {
                    if (bufferedReader != null)
                    {
                        bufferedReader.close();
                    }
                    if (fileReader == null) return;
                    fileReader.close();
                    return;
                }
                catch (IOException iOException2)
                {
                    player.sendMessage(ChatUtil.colorize("&7[&9SpyCheater&7] " + C.Gray + "Failed to find " + string + "'s logs."));
                    return;
                }
            }
        }
        finally
        {
            try
            {
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
                if (fileReader != null)
                {
                    fileReader.close();
                }
            }
            catch (IOException iOException)
            {
                player.sendMessage(ChatUtil.colorize("&7[&9SpyCheater&7] " + C.Gray + "Failed to find " + string + "'s logs."));
            }
        }
    }

}

