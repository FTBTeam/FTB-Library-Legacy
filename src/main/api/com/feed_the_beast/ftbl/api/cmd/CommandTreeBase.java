package com.feed_the_beast.ftbl.api.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.latmod.lib.util.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandTreeBase extends CommandLM implements ITreeCommand
{
    private final Map<String, ICommand> subCommands;

    public CommandTreeBase(String s)
    {
        super(s);
        subCommands = new HashMap<>();
    }

    public void add(ICommand c)
    {
        subCommands.put(c.getCommandName(), c);
    }

    @Override
    public Collection<ICommand> getSubCommands()
    {
        return subCommands.values();
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return '/' + commandName + " [subcommand]";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if(args.length == 1)
        {
            List<String> keys = new ArrayList<>();

            for(ICommand c : subCommands.values())
            {
                if(c.checkPermission(server, sender))
                {
                    keys.add(c.getCommandName());
                }
            }

            Collections.sort(keys, null);
            return getListOfStringsMatchingLastWord(args, keys);
        }

        ICommand cmd = subCommands.get(args[0]);

        if(cmd != null)
        {
            return cmd.getTabCompletionOptions(server, sender, LMStringUtils.shiftArray(args), pos);
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        if(i > 0 && args.length > 1)
        {
            ICommand cmd = subCommands.get(args[0]);
            if(cmd != null)
            {
                return cmd.isUsernameIndex(LMStringUtils.shiftArray(args), i - 1);
            }
        }

        return false;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
    {
        if(args.length < 1)
        {
            throw FTBLibLang.RAW.commandError(LMStringUtils.strip(subCommands.keySet()));
        }
        else
        {
            ICommand cmd = subCommands.get(args[0]);

            if(cmd == null)
            {
                throw FTBLibLang.INVALID_SUBCMD.commandError(args[0]);
            }
            else if(!cmd.checkPermission(server, ics))
            {
                throw new CommandException("commands.generic.permission");
            }
            else
            {
                cmd.execute(server, ics, LMStringUtils.shiftArray(args));
            }
        }
    }
}