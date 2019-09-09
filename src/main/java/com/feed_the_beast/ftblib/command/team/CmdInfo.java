package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdBase;
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
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdInfo extends CmdBase
{
	public CmdInfo()
	{
		super("info", Level.ALL);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, Universe.get().getTeams());
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		checkArgs(sender, args, 1);
		ForgeTeam team = Universe.get().getTeam(args[0]);

		if (!team.isValid())
		{
			throw FTBLib.error(sender, "ftblib.lang.team.error.not_found", args[0]);
		}

		sender.sendMessage(FTBLib.lang(sender, "commands.team.info.id", StringUtils.color(new TextComponentString(team.getId()), TextFormatting.BLUE)));
		sender.sendMessage(FTBLib.lang(sender, "commands.team.info.uid", StringUtils.color(new TextComponentString(team.getUID() + " / " + String.format("%04x", team.getUID())), TextFormatting.BLUE)));
		sender.sendMessage(FTBLib.lang(sender, "commands.team.info.owner", team.owner == null ? "-" : StringUtils.color(team.owner.getDisplayName(), TextFormatting.BLUE)));

		ITextComponent component = new TextComponentString("");
		component.getStyle().setColor(TextFormatting.GOLD);
		boolean first = true;

		for (ForgePlayer player : team.getMembers())
		{
			if (first)
			{
				first = false;
			}
			else
			{
				component.appendText(", ");
			}

			ITextComponent n = player.getDisplayName();
			n.getStyle().setColor(TextFormatting.BLUE);
			component.appendSibling(n);
		}

		sender.sendMessage(FTBLib.lang(sender, "commands.team.info.members", component));
	}
}
