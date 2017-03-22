package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CommandLM;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 11.11.2016.
 */
public class CmdSetStatus extends CommandLM
{
    @Override
    public String getName()
    {
        return "set_status";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/ftb team set_status <player> <status>";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, EnumTeamStatus.VALID_VALUES);
        }

        return super.getTabCompletions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
        IForgePlayer p = getForgePlayer(ep);
        IForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.TEAM_NO_TEAM.commandError();
        }
        else if(!team.hasStatus(p, EnumTeamStatus.MOD))
        {
            throw FTBLibLang.COMMAND_PERMISSION.commandError();
        }

        checkArgs(args, 2, "<player> <status>");
        IForgePlayer p1 = getForgePlayer(args[0]);

        if(p1.equals(team.getOwner()))
        {
            throw FTBLibLang.TEAM_PERMISSION_OWNER.commandError();
        }
        else if(!team.hasStatus(p, EnumTeamStatus.MOD))
        {
            throw FTBLibLang.COMMAND_PERMISSION.commandError();
        }

        EnumTeamStatus status = EnumTeamStatus.NAME_MAP.get(args[1].toLowerCase());

        if(status == null)
        {
            throw new IllegalArgumentException(args[1]);
        }

        team.setStatus(p1.getId(), status);
        //TODO: Display notification
    }
}
