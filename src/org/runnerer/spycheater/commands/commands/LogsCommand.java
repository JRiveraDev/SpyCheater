package org.runnerer.spycheater.commands.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.runnerer.spycheater.SpyCheater;
import org.runnerer.spycheater.common.utils.ChatUtil;
import org.runnerer.spycheater.common.utils.UUIDFetcher;

public class LogsCommand
        implements CommandExecutor
{

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring)
    {
        if (!(commandSender instanceof Player))
        {
            commandSender.sendMessage("Console cannot execute this command.");
            return true;
        }
        Player player = (Player) commandSender;
        if (!player.hasPermission("spycheater.logs"))
        {
            player.sendMessage(ChatUtil.colorize("&cYou do not have permission to do that."));
            return true;
        }
        if (arrstring.length == 0)
        {
            player.sendMessage(ChatUtil.colorize("&cUsage: /logs <name>"));
        }
        if (arrstring.length != 1) return true;
        try
        {
            String string2 = String.valueOf(UUIDFetcher.getUUIDOf(arrstring[0]));
            SpyCheater.Instance.attemptToRead(player, string2);
            return false;
        }
        catch (Exception exception)
        {
            player.sendMessage(ChatUtil.colorize("&7[&9SpyCheater&7] Error!"));
            return false;
        }
    }
}

