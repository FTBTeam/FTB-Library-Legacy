package com.feed_the_beast.ftblib.command.team;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.command.CmdEditConfigBase;
import com.feed_the_beast.ftblib.lib.command.CommandUtils;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdSettings extends CmdEditConfigBase
{
	public CmdSettings()
	{
		super("settings", Level.ALL);
	}

	@Override
	public List<String> getAliases()
	{
		return Collections.singletonList("config");
	}

	@Override
	public ConfigGroup getGroup(ICommandSender sender) throws CommandException
	{
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		ForgePlayer p = CommandUtils.getForgePlayer(player);

		if (!p.hasTeam())
		{
			FTBLibAPI.sendCloseGuiPacket(player);
			throw FTBLib.error(sender, "ftblib.lang.team.error.no_team");
		}
		else if (!p.team.isModerator(p))
		{
			FTBLibAPI.sendCloseGuiPacket(player);
			throw new CommandException("commands.generic.permission");
		}

		return p.team.getSettings();
	}

	@Override
	public IConfigCallback getCallback(ICommandSender sender) throws CommandException
	{
		return CommandUtils.getForgePlayer(sender).team.getConfigCallback();
	}
}