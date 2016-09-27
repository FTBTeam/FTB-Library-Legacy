package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdInvite extends CommandLM
{
    public CmdInvite()
    {
        super("invite");
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
        else if(!team.getStatus(p).isOwner())
        {
            throw FTBLibLang.TEAM_NOT_OWNER.commandError();
        }

        checkArgs(args, 1, "<player>");

        ForgePlayer p1 = getForgePlayer(args[0]);

        if(team.inviteMember(p1))
        {
            FTBLibLang.TEAM_INVITED.printChat(sender, p1.getProfile().getName(), team.getName());

            if(p1.isOnline())
            {
                FTBLibLang.TEAM_INVITED_YOU.printChat(p1.getPlayer(), team.getName(), ep.getName());
            }
        }
        else
        {
            throw FTBLibLang.TEAM_ALREADY_INVITED.commandError(p1.getProfile().getName());
        }
    }
}
