package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
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
        ForgePlayer p = getForgePlayer(ep);
        ForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.team_no_team.commandError();
        }
        else if(team.getMembers().size() == 1)
        {
            MinecraftForge.EVENT_BUS.post(new ForgeTeamDeletedEvent(team));
            team.removePlayer(p);
            p.getWorld().teams.remove(team.getName());

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
    }
}
