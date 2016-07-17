package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
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

        checkArgs(args, 1, "<teamID>");

        if(!ForgeWorldMP.inst.teams.containsKey(args[0]))
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
