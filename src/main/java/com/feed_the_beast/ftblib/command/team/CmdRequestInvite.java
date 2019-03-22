package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdRequestInvite extends CmdBase
{
	public CmdRequestInvite()
	{
		super("request_invite", Level.ALL);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, EnumTeamStatus.VALID_VALUES);
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		ForgePlayer p = CommandUtils.getForgePlayer(getCommandSenderAsPlayer(sender));

		if (p.hasTeam())
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.must_leave");
		}

		checkArgs(sender, args, 1);

		ForgeTeam team = Universe.get().getTeam(args[0]);

		if (!team.isValid())
		{
			throw FTBLib.error(sender, "error", args[0]);
		}

		team.setRequestingInvite(p, true);

		ITextComponent component = new TextComponentString("");
		component.appendSibling(new TextComponentTranslation("ftblib.lang.team.gui.members.requesting_invite"));
		component.appendText(": ");
		component.appendSibling(StringUtils.color(p.getDisplayName(), TextFormatting.BLUE));
		component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/team status " + p.getName() + " member"));

		for (ForgePlayer player : team.getPlayersWithStatus(EnumTeamStatus.MOD))
		{
			if (player.isOnline())
			{
				player.getPlayer().sendMessage(new TextComponentTranslation("ftblib.lang.team.gui.members.requesting_invite"));
			}
		}
	}
}