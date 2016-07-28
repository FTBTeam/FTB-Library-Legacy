package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.net.MessageUpdateTeam;
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
        ForgePlayerMP p = ForgePlayerMP.get(ep);

        if(!p.hasTeam())
        {
            throw FTBLibLang.team_no_team.commandError();
        }

        ForgeTeam team = p.getTeam();

        if(!team.getStatus(p).isOwner())
        {
            throw FTBLibLang.team_not_owner.commandError();
        }

        checkArgs(args, 1, "<player>");

        ForgePlayerMP p1 = ForgePlayerMP.get(args[0]);

        if(!p1.isMemberOf(team))
        {
            throw FTBLibLang.team_not_member.commandError(p1.getProfile().getName());
        }

        if(!p1.equalsPlayer(p))
        {
            team.removePlayer(p);
            new MessageUpdateTeam(team.getID()).sendTo(null);
            FTBLibLang.team_member_left.printChat(sender, p.getProfile().getName());

            p.sendUpdate();
            p1.sendUpdate();
        }
        else
        {
            throw FTBLibLang.team_must_transfer_ownership.commandError();
        }
    }
}
