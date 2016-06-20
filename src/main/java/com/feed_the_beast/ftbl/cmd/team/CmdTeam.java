package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.cmd.CommandSubBase;
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
    }

    public static ForgeTeam getTeam(String s) throws CommandException
    {
        ForgeTeam team = ForgeWorldMP.inst.teams.get(s);

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