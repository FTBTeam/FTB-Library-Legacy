package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeTeam;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by LatvianModder on 20.06.2016.
 */
public class CmdJoin extends CommandLM
{
    public CmdJoin()
    {
        super("join");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Nonnull
    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if(args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, ForgeWorldMP.inst.teams.keySet());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
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

        checkArgs(args, 1, "<ID>");

        ForgeTeam team = CmdTeam.getTeam(args[0]);

        if(team.addPlayer(p))
        {
            p.sendUpdate();

            for(ForgePlayer p1 : team.getMembers())
            {
                if(p1.isOnline())
                {
                    FTBLibLang.team_member_joined.printChat(p1.getPlayer(), p.getProfile().getName());
                }
            }
        }
        else
        {
            throw FTBLibLang.team_not_member.commandError();
        }

        p.sendUpdate();
    }
}
