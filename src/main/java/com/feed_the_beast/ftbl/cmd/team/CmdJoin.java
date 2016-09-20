package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdJoin extends CommandLM
{
    public CmdJoin()
    {
        super("join");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, FTBLibAPI_Impl.INSTANCE.getUniverse().teams.keySet());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        ForgePlayer p = getForgePlayer(ep);

        if(p.getTeam() != null)
        {
            throw FTBLibLang.TEAM_MUST_LEAVE.commandError();
        }

        checkArgs(args, 1, "<ID>");

        ForgeTeam team = CmdTeam.getTeam(args[0]);

        if(team.addPlayer(p))
        {
            for(IForgePlayer p1 : team.getMembers())
            {
                if(p1.isOnline())
                {
                    FTBLibLang.TEAM_MEMBER_JOINED.printChat(p1.getPlayer(), p.getProfile().getName());
                }
            }
        }
        else
        {
            throw FTBLibLang.TEAM_NOT_MEMBER.commandError(p.getProfile().getName());
        }
    }
}
