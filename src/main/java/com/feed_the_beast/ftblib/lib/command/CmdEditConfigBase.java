package com.feed_the_beast.ftblib.lib.command;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.ConfigValueInstance;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.text_components.Notification;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class CmdEditConfigBase extends CmdBase
{
	public CmdEditConfigBase(String n, Level l)
	{
		super(n, l);
	}

	public abstract ConfigGroup getGroup(ICommandSender sender) throws CommandException;

	public IConfigCallback getCallback(ICommandSender sender) throws CommandException
	{
		return IConfigCallback.DEFAULT;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		try
		{
			ConfigGroup group = getGroup(sender);

			if (args.length == 1)
			{
				List<String> keys = getListOfStringsMatchingLastWord(args, group.getValueKeyTree());

				if (keys.size() > 1)
				{
					keys.sort(StringUtils.ID_COMPARATOR);
				}

				return keys;
			}
			else if (args.length == 2)
			{
				ConfigValue value = group.getValue(args[0]);

				if (!value.isNull())
				{
					List<String> variants = value.getVariants();

					if (!variants.isEmpty())
					{
						return getListOfStringsMatchingLastWord(args, variants);
					}
				}

				return Collections.emptyList();
			}
		}
		catch (CommandException ex)
		{
			//ITextComponent c = new TextComponentTranslation(ex.getMessage(), ex.getErrorObjects());
			//c.getStyle().setColor(TextFormatting.DARK_RED);
			//sender.addChatMessage(c);
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length == 0 && sender instanceof EntityPlayerMP)
		{
			FTBLibAPI.editServerConfig(getCommandSenderAsPlayer(sender), getGroup(sender), getCallback(sender));
			return;
		}

		checkArgs(sender, args, 1);
		ConfigGroup group = getGroup(sender);
		ConfigValueInstance instance = group.getValueInstance(args[0]);

		if (instance == null)
		{
			throw FTBLib.error(sender, "ftblib.lang.config_command.invalid_key", args[0]);
		}

		if (args.length >= 2)
		{
			String valueString = String.valueOf(StringUtils.joinSpaceUntilEnd(1, args));

			if (!instance.getValue().setValueFromString(sender, valueString, true))
			{
				return;
			}

			if (FTBLibConfig.debugging.log_config_editing)
			{
				FTBLib.LOGGER.info("Setting " + instance.getPath() + " to " + valueString);
			}

			instance.getValue().setValueFromString(sender, valueString, false);
			getCallback(sender).onConfigSaved(group, sender);
			Notification.of(Notification.VANILLA_STATUS, FTBLib.lang(sender, "ftblib.lang.config_command.set", instance.getDisplayName(), group.getValue(args[0]).toString())).send(server, getCommandSenderAsPlayer(sender));
		}

		sender.sendMessage(instance.getValue().getStringForGUI());
	}
}