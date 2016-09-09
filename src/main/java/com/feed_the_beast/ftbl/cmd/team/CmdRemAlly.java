package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.latmod.lib.util.LMServerUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdRemAlly extends CommandLM
{
    public CmdRemAlly()
    {
        super("rem_ally");
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

        checkArgs(args, 1, "<teamID>");
        ForgeTeam team1 = CmdTeam.getTeam(args[0]);

        if(team.removeAllyTeam(team1.getName()))
        {
            LMServerUtils.printChat(ep, "Removed ally team: " + team1.getName());
        }
    }
}
