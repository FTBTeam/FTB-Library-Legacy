package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdLeave extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "leave";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
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
        else if(team.getPlayersWithStatus(new ArrayList<>(), EnumTeamStatus.MEMBER).size() == 1)
        {
            MinecraftForge.EVENT_BUS.post(new ForgeTeamDeletedEvent(team));
            team.removePlayer(p);
            Universe.INSTANCE.teams.remove(team.getName());

            FTBLibLang.TEAM_MEMBER_LEFT.printChat(sender, p.getProfile().getName());
            FTBLibLang.TEAM_DELETED.printChat(sender, team.getTitle());
        }
        else
        {
            if(team.hasStatus(p, EnumTeamStatus.OWNER))
            {
                throw FTBLibLang.TEAM_MUST_TRANSFER_OWNERSHIP.commandError();
            }

            team.removePlayer(p);
            FTBLibLang.TEAM_MEMBER_LEFT.printChat(sender, p.getProfile().getName());
        }
    }
}
