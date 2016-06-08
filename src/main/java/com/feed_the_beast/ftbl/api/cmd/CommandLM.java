package com.feed_the_beast.ftbl.api.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public abstract class CommandLM extends CommandBase // CommandSubLM
{
    public final String commandName;
    public final CommandLevel level;

    public CommandLM(String s, CommandLevel l)
    {
        if(s == null || s.isEmpty() || s.indexOf(' ') != -1)
        {
            throw new NullPointerException("Command ID can't be null!");
        }

        if(l == null)
        {
            throw new NullPointerException();
        }

        commandName = s;
        level = l;
    }

    public static void checkArgs(String[] args, int i) throws CommandException
    {
        if(args == null || args.length < i)
        {
            throw FTBLibLang.missing_args.commandError();
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return level.requiredPermsLevel();
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender ics)
    {
        return level == CommandLevel.ALL || !FTBLib.isDedicatedServer() || super.checkPermission(server, ics);
    }

    @Override
    public final String getCommandName()
    {
        return commandName;
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return '/' + commandName;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
    {
        if(args.length == 0)
        {
            return null;
        }
        else if(isUsernameIndex(args, args.length - 1))
        {
            return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }

        return super.getTabCompletionOptions(server, ics, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return false;
    }
}