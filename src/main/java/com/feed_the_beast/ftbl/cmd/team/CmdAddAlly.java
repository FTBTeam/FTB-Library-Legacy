package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdAddAlly extends CommandLM
{
    @Override
    public String getCommandName()
    {
        return "add_ally";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Universe.INSTANCE.teams.keySet());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        IForgePlayer p = FTBLibIntegrationInternal.API.getForgePlayer(ep);
        IForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.TEAM_NO_TEAM.commandError();
        }
        else if(!team.hasStatus(p, EnumTeamStatus.OWNER))
        {
            throw FTBLibLang.TEAM_NOT_OWNER.commandError();
        }

        checkArgs(args, 1, "<teamID>");

        if(Universe.INSTANCE.getTeam(args[0]) == null)
        {
            throw FTBLibLang.TEAM_NOT_FOUND.commandError();
        }

        if(team.addAllyTeam(args[0]))
        {
            //TODO: Lang
        }

        ep.addChatMessage(new TextComponentString("Added ally team: " + args[0])); // TODO: Lang
    }
}
