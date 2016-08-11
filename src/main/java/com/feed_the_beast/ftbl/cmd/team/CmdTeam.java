package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandSubBase;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import net.minecraft.command.CommandException;

/**
 * Created by LatvianModder on 29.05.2016.
 */
public class CmdTeam extends CommandSubBase
{
    public CmdTeam()
    {
        super("team");

        add(new CmdTeamConfig());
        add(new CmdListTeams());
        add(new CmdCreate());
        add(new CmdLeave());
        add(new CmdTransferOwnership());
        add(new CmdKick());
        add(new CmdInvite());
        add(new CmdJoin());
        add(new CmdAddAlly());
        add(new CmdRemAlly());
    }

    public static ForgeTeam getTeam(String s) throws CommandException
    {
        ForgeTeam team = FTBLibAPI_Impl.INSTANCE.getWorld().getTeam(s);

        if(team != null)
        {
            return team;
        }

        throw FTBLibLang.team_not_found.commandError();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
}