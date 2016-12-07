package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.internal.FTBLibTeamPermissions;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdKick extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "kick";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        IForgePlayer p = getForgePlayer(ep);
        IForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.TEAM_NO_TEAM.commandError();
        }
        else if(!team.hasPermission(p.getProfile().getId(), FTBLibTeamPermissions.MANAGE_MEMBERS))
        {
            throw FTBLibLang.COMMAND_PERMISSION.commandError();
        }

        checkArgs(args, 1, "<player>");

        IForgePlayer p1 = getForgePlayer(args[0]);

        if(!team.hasStatus(p1, EnumTeamStatus.MEMBER))
        {
            throw FTBLibLang.TEAM_NOT_MEMBER.commandError(p1.getProfile().getName());
        }

        if(!p1.equalsPlayer(p))
        {
            team.removePlayer(p1);

            for(IForgePlayer m : team.getPlayersWithStatus(new ArrayList<>(), EnumTeamStatus.MEMBER))
            {
                if(m.isOnline())
                {
                    FTBLibLang.TEAM_MEMBER_LEFT.printChat(m.getPlayer(), p.getProfile().getName());
                }
            }
        }
        else
        {
            throw FTBLibLang.TEAM_MUST_TRANSFER_OWNERSHIP.commandError();
        }
    }
}
