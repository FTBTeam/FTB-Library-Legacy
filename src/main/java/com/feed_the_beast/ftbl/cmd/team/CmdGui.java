package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.net.MessageMyTeamGuiResponse;
import com.feed_the_beast.ftbl.net.MessageSelectTeamGuiResponse;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class CmdGui extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "gui";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        IUniverse universe = FTBLibIntegrationInternal.API.getUniverse();

        if(universe == null)
        {
            return;
        }

        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        IForgePlayer p = getForgePlayer(player);
        IForgeTeam team = p.getTeam();

        if(team != null)
        {
            new MessageMyTeamGuiResponse(universe, team, p).sendTo(player);
        }
        else
        {
            new MessageSelectTeamGuiResponse(universe, p).sendTo(player);
        }
    }
}