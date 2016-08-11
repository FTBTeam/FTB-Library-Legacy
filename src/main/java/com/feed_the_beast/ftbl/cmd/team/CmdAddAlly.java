package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdAddAlly extends CommandLM
{
    public CmdAddAlly()
    {
        super("add_ally");
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

        checkArgs(args, 1, "<teamID>");

        if(p.getWorld().getTeam(args[0]) == null)
        {
            throw FTBLibLang.team_not_found.commandError();
        }

        if(team.addAllyTeam(args[0]))
        {
            //TODO: Lang
        }

        FTBLib.printChat(ep, "Added ally team: " + args[0]);
    }
}
