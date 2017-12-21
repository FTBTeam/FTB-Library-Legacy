package com.feed_the_beast.ftblib.cmd;

import com.feed_the_beast.ftblib.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftblib.lib.cmd.CmdTreeBase;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.FileUtils;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;

import java.io.File;

/**
 * @author LatvianModder
 */
public class CmdEditConfig extends CmdTreeBase
{
	public static class CmdEditConfigFile extends CmdEditConfigBase implements IConfigCallback
	{
		private final File file;
		private final ConfigGroup group;

		private static String getId(File f)
		{
			String s = f.getAbsolutePath().replace(CommonUtils.folderConfig.getAbsolutePath(), "").replace(".cfg", "");
			return (s.startsWith("/") || s.startsWith("\\")) ? s.substring(1) : s;
		}

		public CmdEditConfigFile(File f)
		{
			super(getId(f), Level.OP);
			file = f;
			group = new ConfigGroup(new TextComponentString(getName()));
		}

		@Override
		public ConfigGroup getGroup(ICommandSender sender) throws CommandException
		{
			return group;
		}

		@Override
		public IConfigCallback getCallback(ICommandSender sender) throws CommandException
		{
			return this;
		}

		@Override
		public void saveConfig(ConfigGroup group, ICommandSender sender, JsonObject json)
		{
			group.fromJson(json);
			//save file
		}
	}

	public CmdEditConfig()
	{
		super("edit_config");

		for (File file : FileUtils.listAll(CommonUtils.folderConfig))
		{
			if (file.getName().endsWith(".cfg"))
			{
				addSubcommand(new CmdEditConfigFile(file));
			}
		}
	}
}