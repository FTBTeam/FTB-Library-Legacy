package com.feed_the_beast.ftbl.api.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class CommandLM extends CommandBase
{
    public final String commandName;

    public CommandLM(String s)
    {
        if(s == null || s.isEmpty() || s.indexOf(' ') != -1)
        {
            throw new NullPointerException("Command ID can't be null!");
        }

        commandName = s;
    }

    public static void checkArgs(String[] args, int i, String desc) throws CommandException
    {
        if(args == null || args.length < i)
        {
            if(desc == null || desc.isEmpty())
            {
                throw FTBLibLang.missing_args_num.commandError(Integer.toString(i - (args == null ? 0 : args.length)));
            }
            else
            {
                throw FTBLibLang.missing_args.commandError(desc);
            }
        }
    }

    public static ForgePlayer getForgePlayer(Object o) throws CommandException
    {
        ForgePlayer p = FTBLibAPI_Impl.get().getWorld().getPlayer(o);

        if(p == null || p.isFake())
        {
            throw new PlayerNotFoundException();
        }

        return p;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender ics)
    {
        return getRequiredPermissionLevel() == 0 || !FTBLib.isDedicatedServer() || super.checkPermission(server, ics);
    }

    @Nonnull
    @Override
    public final String getCommandName()
    {
        return commandName;
    }

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender ics)
    {
        return '/' + commandName;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if(args.length == 0)
        {
            return null;
        }
        else if(isUsernameIndex(args, args.length - 1))
        {
            return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return false;
    }
}