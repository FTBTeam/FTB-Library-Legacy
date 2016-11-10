package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdCreate extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "create";
    }

    private static boolean isValidTeamID(String s)
    {
        if(s != null && !s.isEmpty())
        {
            for(int i = 0; i < s.length(); i++)
            {
                if(!isValidChar(s.charAt(i)))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    private static boolean isValidChar(char c)
    {
        return c == '_' || c == '|' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
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
        IForgePlayer p = getForgePlayer(ep);

        if(p.getTeam() != null)
        {
            throw FTBLibLang.TEAM_MUST_LEAVE.commandError();
        }

        checkArgs(args, 1, "<ID>");

        if(!isValidTeamID(args[0]))
        {
            throw FTBLibLang.RAW.commandError("ID can only contain lowercase a-z, _ and |!");
        }

        if(Universe.INSTANCE.getTeam(args[0]) != null)
        {
            throw FTBLibLang.RAW.commandError("ID already registred!");
        }

        ForgeTeam team = new ForgeTeam(args[0]);
        team.changeOwner(p);
        Universe.INSTANCE.teams.put(team.getName(), team);

        MinecraftForge.EVENT_BUS.post(new ForgeTeamCreatedEvent(team));
        MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerJoinedEvent(team, p));

        FTBLibLang.TEAM_CREATED.printChat(sender, team.getName());
    }
}