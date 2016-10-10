package com.feed_the_beast.ftbl.cmd.team;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;

/**
 * Created by LatvianModder on 29.05.2016.
 */
public class CmdTeam extends CommandTreeBase
{
    public CmdTeam()
    {
        addSubcommand(new CmdTeamConfig());
        addSubcommand(new CmdListTeams());
        addSubcommand(new CmdCreate());
        addSubcommand(new CmdLeave());
        addSubcommand(new CmdTransferOwnership());
        addSubcommand(new CmdKick());
        addSubcommand(new CmdInvite());
        addSubcommand(new CmdJoin());
        addSubcommand(new CmdAddAlly());
        addSubcommand(new CmdRemAlly());
    }

    @Override
    public String getCommandName()
    {
        return "team";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "command.ftb.team.usage";
    }
}