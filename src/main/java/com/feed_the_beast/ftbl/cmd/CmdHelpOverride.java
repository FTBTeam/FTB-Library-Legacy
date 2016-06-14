package com.feed_the_beast.ftbl.cmd;

import net.minecraft.command.CommandHelp;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class CmdHelpOverride extends CommandHelp
{
    public CmdHelpOverride()
    {
    }

    @Nonnull
    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return '/' + getCommandName() + " [command]";
    }

    @Nonnull
    @Override
    protected List<ICommand> getSortedPossibleCommands(@Nonnull ICommandSender sender, MinecraftServer server)
    {
        List<ICommand> list = server.getCommandManager().getPossibleCommands(sender);
        try
        {
            Collections.sort(list);
        }
        catch(Exception e)
        {
        }
        return list;
    }
}