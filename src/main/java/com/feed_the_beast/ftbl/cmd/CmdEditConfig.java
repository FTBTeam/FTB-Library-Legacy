package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.cmd.CmdTreeBase;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.FileUtils;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.io.File;

/**
 * @author LatvianModder
 */
public class CmdEditConfig extends CmdTreeBase
{
	public static class CmdEditConfigFile extends CmdEditConfigBase implements IConfigCallback
	{
		private final File file;
		private final ITextComponent title;
		private final ConfigTree tree;

		private static String getId(File f)
		{
			String s = f.getAbsolutePath().replace(CommonUtils.folderConfig.getAbsolutePath(), "").replace(".cfg", "");
			return (s.startsWith("/") || s.startsWith("\\")) ? s.substring(1) : s;
		}

		public CmdEditConfigFile(File f)
		{
			super(getId(f), Level.OP);
			file = f;
			title = new TextComponentString(getName());
			tree = new ConfigTree();
		}

		@Override
		public ITextComponent getTitle(ICommandSender sender) throws CommandException
		{
			return title;
		}

		@Override
		public ConfigTree getTree(ICommandSender sender) throws CommandException
		{
			return tree;
		}

		@Override
		public IConfigCallback getCallback(ICommandSender sender) throws CommandException
		{
			return this;
		}

		@Override
		public void saveConfig(ConfigTree tree, ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
		{
			tree.fromJson(json);
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