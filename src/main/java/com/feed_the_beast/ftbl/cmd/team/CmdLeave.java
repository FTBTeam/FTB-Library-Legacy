package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.events.ForgeTeamEvent;
import com.feed_the_beast.ftbl.net.MessageUpdateTeam;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdLeave extends CommandLM
{
    public CmdLeave()
    {
        super("leave");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        ForgePlayerMP p = ForgePlayerMP.get(ep);

        if(!p.hasTeam())
        {
            throw FTBLibLang.team_no_team.commandError();
        }

        ForgeTeam team = p.getTeam();

        if(team.getMembers().size() == 1)
        {
            String teamID = team.getID() + "";
            MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.Deleted(team));
            team.removePlayer(p);
            ForgeWorldMP.inst.teams.remove(teamID);
            new MessageUpdateTeam(teamID).sendTo(null);

            FTBLibLang.team_member_left.printChat(sender, p.getProfile().getName());
            FTBLibLang.team_deleted.printChat(sender, team.getTitle());
        }
        else
        {
            if(team.getStatus(p).isOwner())
            {
                throw FTBLibLang.team_must_transfer_ownership.commandError();
            }

            team.removePlayer(p);
            FTBLibLang.team_member_left.printChat(sender, p.getProfile().getName());
        }

        p.sendUpdate();
    }
}
