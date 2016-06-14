package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.util.FTBLib;
import latmod.lib.util.LMUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.List;

public class CmdListOverride extends CommandLM
{
    public CmdListOverride()
    {
        super("list");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Nonnull
    @Override
    public String getCommandUsage(@Nonnull ICommandSender ics)
    {
        return '/' + commandName + " ['uuid']";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender ics, @Nonnull String[] args) throws CommandException
    {
        List<EntityPlayerMP> players = server.getPlayerList().getPlayerList();
        boolean printUUID = args.length > 0 && args[0].equals("uuid");

        FTBLib.printChat(ics, "Players currently online: [ " + players.size() + " ]");
        for(EntityPlayerMP ep : players)
        {
            if(printUUID)
            {
                FTBLib.printChat(ics, ep.getName() + " :: " + LMUtils.fromUUID(ep.getUniqueID()));
            }
            else
            {
                FTBLib.printChat(ics, ep.getName());
            }
        }
    }
}