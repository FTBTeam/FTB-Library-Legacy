package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CmdNotify extends CommandLM
{
    public CmdNotify()
    {
        super("notify");
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return "/" + commandName + " <player> <id>";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, FTBLibRegistries.INSTANCE.NOTIFICATIONS.getMap().keySet());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
    {
        checkArgs(args, 2, "<player> <id>");
        EntityPlayerMP player = getPlayer(server, ics, args[0]);

        if(args[1].startsWith("{") && args[1].endsWith("}"))
        {
            //TODO: Custom notification support
        }
        else
        {
            INotification n = FTBLibRegistries.INSTANCE.NOTIFICATIONS.get(args[1]);

            if(n == null)
            {
                throw new IllegalArgumentException("Notification '" + args[1] + "' not found!");
            }

            FTBLibAPI_Impl.INSTANCE.sendNotification(player, n);
        }
    }
}