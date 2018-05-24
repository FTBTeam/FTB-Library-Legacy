package com.feed_the_beast.ftblib.command.client;

import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class CmdSimulateButton extends CmdBase
{
	public CmdSimulateButton()
	{
		super("ftblib_simulate_button", Level.ALL);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		GuiHelper.BLANK_GUI.handleClick(StringUtils.joinSpaceUntilEnd(0, args));
	}
}