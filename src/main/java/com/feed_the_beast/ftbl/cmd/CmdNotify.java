package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.NotificationId;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CmdNotify extends CmdBase
{
    @Override
    public String getName()
    {
        return "notify";
    }

    @Override
    public String getUsage(ICommandSender ics)
    {
        return "/" + getName() + " <player> <id>";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, SharedServerData.INSTANCE.notifications.keySet());
        }

        return super.getTabCompletions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        checkArgs(args, 2, "<player> <id>");
        EntityPlayerMP player = getPlayer(server, sender, args[0]);

        if(args[1].startsWith("{") && args[1].endsWith("}"))
        {
            //TODO: Custom notification support
        }
        else
        {
            INotification n = SharedServerData.INSTANCE.notifications.get(new NotificationId(new ResourceLocation(args[1].substring(0, args[1].indexOf('@'))), Integer.parseInt(args[1].substring(args[1].indexOf('@') + 1, args[1].length()))));

            if(n == null)
            {
                throw new IllegalArgumentException("Notification '" + args[1] + "' not found!");
            }

            FTBLibIntegrationInternal.API.sendNotification(player, n);
        }
    }
}