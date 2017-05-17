package com.feed_the_beast.ftbl.cmd.team;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeHooks;

/**
 * @author LatvianModder
 */
public class CmdMsg extends CmdBase
{
    public CmdMsg()
    {
        super("msg", Level.ALL);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        checkArgs(args, 1, "<message...>");
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        IForgePlayer p = getForgePlayer(player);
        IForgeTeam team = p.getTeam();

        if(team == null)
        {
            throw FTBLibLang.TEAM_NO_TEAM.commandError();
        }

        String m = StringUtils.joinSpaceUntilEnd(0, args);

        if(m == null || m.isEmpty())
        {
            throw new IllegalArgumentException(m);
        }

        team.printMessage(new ForgeTeam.Message(p.getId(), System.currentTimeMillis(), ForgeHooks.newChatWithLinks(m)));
    }
}