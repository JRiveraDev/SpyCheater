package org.runnerer.spycheater.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.runnerer.spycheater.commands.exception.CommandAlreadyRegisteredException;
import org.runnerer.spycheater.common.utils.ChatUtil;

import java.util.HashMap;
import java.util.Map;

public class AntiCheatCommand
        implements CommandExecutor
{

    private Map<String, CommandInterface> registeredSubCommands = new HashMap<String, CommandInterface>();

    public void registerSubCommand(String string, CommandInterface commandInterface)
    {
        if (string.equals("")) return;
        if (commandInterface == null) return;
        if (this.registeredSubCommands.containsKey(string = string.toLowerCase()))
            throw new CommandAlreadyRegisteredException();
        this.registeredSubCommands.put(string, commandInterface);
    }

    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] arrstring)
    {
        if (!(commandSender instanceof Player))
        {
            commandSender.sendMessage(ChatUtil.colorize("&cSorry, only players can execute this command!"));
            return false;
        }
        Player player = (Player) commandSender;
        if (arrstring.length < 1) return false;
        CommandInterface commandInterface = this.registeredSubCommands.get(arrstring[0].toLowerCase());
        if (commandInterface == null) return false;
        Permission permission = commandInterface.getClass().getAnnotation(Permission.class);
        if (permission != null && !player.hasPermission(permission.permission()))
        {
            player.sendMessage(ChatUtil.colorize("&cSorry, but you don't have permission for that command!"));
            return false;
        }
        String[] arrstring2 = new String[arrstring.length - 1];
        System.arraycopy(arrstring, 1, arrstring2, 0, arrstring2.length);
        return commandInterface.onCommand(player, arrstring2);
    }
}

