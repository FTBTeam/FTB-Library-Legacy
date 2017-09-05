package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.cmd.CmdTreeBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

/**
 * @author LatvianModder
 */
public class CmdEditConfig extends CmdTreeBase
{
	public static class CmdEditConfigFile extends CmdEditConfigBase
	{
		private IConfigContainer configContainer;

		public CmdEditConfigFile(String id, IConfigContainer c)
		{
			super(id, Level.OP);
			configContainer = c;
		}

		@Override
		public IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
		{
			return configContainer;
		}
	}

	public CmdEditConfig()
	{
		super("edit_config");
		FTBLibModCommon.CONFIG_FILES.forEach((key, value) -> addSubcommand(new CmdEditConfigFile(key, value)));
	}
}