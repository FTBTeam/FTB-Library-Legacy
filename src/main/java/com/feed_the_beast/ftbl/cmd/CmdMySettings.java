package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.lib.LangKey;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public class CmdMySettings extends CmdEditConfigBase
{
	private static final LangKey TITLE = LangKey.of("sidebar_button.ftbl.my_server_settings");

	public CmdMySettings()
	{
		super("my_settings", Level.ALL);
	}

	@Override
	public ITextComponent getTitle(ICommandSender sender) throws CommandException
	{
		return TITLE.textComponent();
	}

	@Override
	public ConfigTree getTree(ICommandSender sender) throws CommandException
	{
		return getForgePlayer(sender).getSettings();
	}
}