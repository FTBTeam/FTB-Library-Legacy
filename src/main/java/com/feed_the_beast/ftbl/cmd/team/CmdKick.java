package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdKick extends CommandLM
{
    public CmdKick()
    {
        super("kick");
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
        else if(!team.getStatus(p).isOwner())
        {
            throw FTBLibLang.team_not_owner.commandError();
        }

        checkArgs(args, 1, "<player>");

        ForgePlayer p1 = getForgePlayer(args[0]);

        if(!p1.isMemberOf(team))
        {
            throw FTBLibLang.team_not_member.commandError(p1.getProfile().getName());
        }

        if(!p1.equalsPlayer(p))
        {
            team.removePlayer(p);
            FTBLibLang.team_member_left.printChat(sender, p.getProfile().getName());
        }
        else
        {
            throw FTBLibLang.team_must_transfer_ownership.commandError();
        }
    }
}
