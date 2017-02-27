package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdJoin extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "join";
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
            return getListOfStringsMatchingLastWord(args, Universe.INSTANCE.teams.keySet());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        IForgePlayer p = getForgePlayer(ep);

        if(p.getTeam() != null)
        {
            throw FTBLibLang.TEAM_MUST_LEAVE.commandError();
        }

        checkArgs(args, 1, "<ID>");

        IForgeTeam team = getTeam(args[0]);

        if(team.addPlayer(p))
        {
            for(IForgePlayer p1 : team.getPlayersWithStatus(new ArrayList<>(), EnumTeamStatus.MEMBER))
            {
                if(p1.isOnline())
                {
                    FTBLibLang.TEAM_MEMBER_JOINED.printChat(p1.getPlayer(), p.getName());
                }
            }
        }
        else
        {
            throw FTBLibLang.TEAM_NOT_MEMBER.commandError(p.getName());
        }
    }
}
