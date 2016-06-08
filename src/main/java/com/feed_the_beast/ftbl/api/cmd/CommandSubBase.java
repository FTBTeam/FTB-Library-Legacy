package com.feed_the_beast.ftbl.api.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import latmod.lib.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandSubBase extends CommandLM implements ICustomCommandInfo
{
    public final Map<String, ICommand> subCommands;

    public CommandSubBase(String s)
    {
        super(s);
        subCommands = new HashMap<>();
    }

    public void add(ICommand c)
    {
        subCommands.put(c.getCommandName(), c);
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return '/' + commandName + " [subcommand]";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender ics, String[] args, BlockPos pos)
    {
        if(args.length == 1)
        {
            List<String> keys = new ArrayList<>();

            for(ICommand c : subCommands.values())
            {
                if(c.checkPermission(server, ics))
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
            return cmd.getTabCompletionOptions(server, ics, LMStringUtils.shiftArray(args), pos);
        }

        return super.getTabCompletionOptions(server, ics, args, pos);
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
            throw FTBLibLang.raw.commandError(LMStringUtils.strip(subCommands.keySet()));
        }
        else
        {
            ICommand cmd = subCommands.get(args[0]);

            if(cmd == null)
            {
                throw FTBLibLang.invalid_subcmd.commandError(args[0]);
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

    @Override
    public void addInfo(MinecraftServer server, ICommandSender sender, List<ITextComponent> list)
    {
        list.add(new TextComponentString('/' + commandName));
        list.add(null);
        addCommandUsage(server, sender, list, 0);
    }

    private void addCommandUsage(MinecraftServer server, ICommandSender sender, List<ITextComponent> list, int level)
    {
        for(ICommand c : subCommands.values())
        {
            if(c instanceof CommandSubBase)
            {
                list.add(tree(new TextComponentString('/' + c.getCommandName()), level));
                ((CommandSubBase) c).addCommandUsage(server, sender, list, level + 1);
            }
            else
            {
                String usage = c.getCommandUsage(sender);
                if(usage.indexOf('/') != -1 || usage.indexOf('%') != -1)
                {
                    list.add(tree(new TextComponentString(usage), level));
                }
                else
                {
                    list.add(tree(new TextComponentTranslation(usage), level));
                }
            }
        }
    }

    private ITextComponent tree(ITextComponent sibling, int level)
    {
        if(level == 0)
        {
            return sibling;
        }
        char[] chars = new char[level * 2];
        Arrays.fill(chars, ' ');
        return new TextComponentString(new String(chars)).appendSibling(sibling);
    }
}