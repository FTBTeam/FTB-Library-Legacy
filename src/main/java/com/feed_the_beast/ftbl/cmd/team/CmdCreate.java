package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api.events.ForgeTeamEvent;
import com.feed_the_beast.ftbl.net.MessageUpdateTeam;
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
        ForgePlayerMP p = ForgePlayerMP.get(ep);

        if(p.hasTeam())
        {
            throw FTBLibLang.team_must_leave.commandError();
        }

        checkArgs(args, 1);

        if(!isValidTeamID(args[0]))
        {
            throw FTBLibLang.raw.commandError("ID can only contain lowercase a-z, _ and |!");
        }

        ForgeTeam team = new ForgeTeam(ForgeWorldMP.inst, args[0]);
        team.changeOwner(p);
        ForgeWorldMP.inst.teams.put(team.getID(), team);

        MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.Created(team));
        MinecraftForge.EVENT_BUS.post(new ForgeTeamEvent.PlayerJoined(team, p));

        new MessageUpdateTeam(p, team).sendTo(null);

        FTBLibLang.team_created.printChat(sender, team.getID());
    }
}