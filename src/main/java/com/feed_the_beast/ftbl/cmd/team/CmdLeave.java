package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

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
        ForgePlayer p = getForgePlayer(ep);
        ForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.TEAM_NO_TEAM.commandError();
        }
        else if(team.getMembers().size() == 1)
        {
            MinecraftForge.EVENT_BUS.post(new ForgeTeamDeletedEvent(team));
            team.removePlayer(p);
            p.getUniverse().teams.remove(team.getName());

            FTBLibLang.TEAM_MEMBER_LEFT.printChat(sender, p.getProfile().getName());
            FTBLibLang.TEAM_DELETED.printChat(sender, team.getTitle());
        }
        else
        {
            if(team.getStatus(p).isOwner())
            {
                throw FTBLibLang.TEAM_MUST_TRANSFER_OWNERSHIP.commandError();
            }

            team.removePlayer(p);
            FTBLibLang.TEAM_MEMBER_LEFT.printChat(sender, p.getProfile().getName());
        }
    }
}
