package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api_impl.PackModes;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.cmd.CmdBase;
import com.feed_the_beast.ftbl.lib.cmd.CmdTreeBase;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdPackMode extends CmdTreeBase
{
	public static class CmdSet extends CmdBase
	{
		public CmdSet()
		{
			super("set", Level.OP);
		}

		@Override
		public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
		{
			if (args.length == 1)
			{
				return getListOfStringsMatchingLastWord(args, PackModes.INSTANCE.getModes());
			}

			return super.getTabCompletions(server, sender, args, pos);
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			checkArgs(args, 1, "<mode>");

			ITextComponent c;

			int i = SharedServerData.INSTANCE.setMode(args[0]);

			if (i == 1)
			{
				c = FTBLibLang.MODE_NOT_FOUND.textComponent();
				c.getStyle().setColor(TextFormatting.RED);
			}
			else if (i == 2)
			{
				c = FTBLibLang.MODE_ALREADY_SET.textComponent();
				c.getStyle().setColor(TextFormatting.RED);
			}
			else
			{
				c = FTBLibLang.MODE_LOADED.textComponent(args[0]);
				c.getStyle().setColor(TextFormatting.GREEN);
				FTBLibAPI.API.reload(Side.SERVER, sender, EnumReloadType.MODE_CHANGED);
			}

			sender.sendMessage(c);
		}
	}

	public static class CmdGet extends CmdBase
	{
		public CmdGet()
		{
			super("get", Level.ALL);
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			ITextComponent c = FTBLibLang.MODE_CURRENT.textComponent(SharedServerData.INSTANCE.getPackMode().getName());
			c.getStyle().setColor(TextFormatting.AQUA);
			sender.sendMessage(c);
		}
	}

	public static class CmdList extends CmdBase
	{
		public CmdList()
		{
			super("list", Level.ALL);
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			ITextComponent c = FTBLibLang.MODE_LIST.textComponent(StringUtils.strip(PackModes.INSTANCE.getModes()));
			c.getStyle().setColor(TextFormatting.AQUA);
			sender.sendMessage(c);
		}
	}

	public CmdPackMode()
	{
		super("packmode");
		addSubcommand(new CmdSet());
		addSubcommand(new CmdGet());
		addSubcommand(new CmdList());
	}
}