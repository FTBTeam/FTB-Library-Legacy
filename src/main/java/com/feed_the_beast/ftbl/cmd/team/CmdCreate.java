package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdCreate extends CommandLM
{
    public CmdCreate()
    {
        super("create");
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
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        ForgePlayer p = getForgePlayer(ep);

        if(p.getTeam() != null)
        {
            throw FTBLibLang.team_must_leave.commandError();
        }

        checkArgs(args, 1, "<ID>");

        if(!isValidTeamID(args[0]))
        {
            throw FTBLibLang.raw.commandError("ID can only contain lowercase a-z, _ and |!");
        }

        if(p.getWorld().getTeam(args[0]) != null)
        {
            throw FTBLibLang.raw.commandError("ID already registred!");
        }

        ForgeTeam team = new ForgeTeam(p.getWorld(), args[0]);
        team.changeOwner(p);
        p.getWorld().teams.put(team.getID(), team);

        MinecraftForge.EVENT_BUS.post(new ForgeTeamCreatedEvent(team));
        MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerJoinedEvent(team, p));

        FTBLibLang.team_created.printChat(sender, team.getID());
    }
}