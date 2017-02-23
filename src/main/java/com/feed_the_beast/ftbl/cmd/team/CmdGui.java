package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.net.MessageMyTeamGuiResponse;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
            sender.addChatMessage(new TextComponentString("Create or join a team with '/ftb team create <name>' or '/ftb team join <name>'! Gui coming soon"));
        }
    }
}