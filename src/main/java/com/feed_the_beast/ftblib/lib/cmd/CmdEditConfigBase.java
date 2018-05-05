package com.feed_the_beast.ftblib.lib.cmd;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.ConfigValueInstance;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.io.DataReader;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.feed_the_beast.ftblib.lib.util.text_components.Notification;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.TextComponentHelper;

import javax.annotation.Nullable;
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
				List<String> keys = getListOfStringsMatchingLastWord(args, group.getMap().keySet());

				if (keys.size() > 1)
				{
					keys.sort(StringUtils.ID_COMPARATOR);
				}

				return keys;
			}
			else if (args.length == 2)
			{
				ConfigValue entry = group.get(Node.get(args[0]));

				if (!entry.isNull())
				{
					List<String> variants = entry.getVariants();

					if (!variants.isEmpty())
					{
						return getListOfStringsMatchingLastWord(args, variants);
					}
				}
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
		Node node = Node.get(args[0]);
		ConfigGroup group = getGroup(sender);
		ConfigValue entry = group.get(node);

		if (entry.isNull())
		{
			throw new CommandException("ftblib.lang.config_command.invalid_key", node.toString());
		}

		if (args.length >= 2)
		{
			String json = String.valueOf(StringUtils.joinSpaceUntilEnd(1, args));
			FTBLib.LOGGER.info("Setting " + node + " to " + json);

			try
			{
				JsonElement value = DataReader.get(JsonUtils.fixJsonString(json)).json();
				JsonObject json1 = new JsonObject();
				json1.add(node.toString(), value);
				getCallback(sender).saveConfig(group, sender, json1);
				ConfigValueInstance instance = group.getMap().get(node);
				Notification.of(Notification.VANILLA_STATUS, TextComponentHelper.createComponentTranslation(sender, "ftblib.lang.config_command.set", group.getDisplayName(instance.info), group.get(node).toString())).send(server, getCommandSenderAsPlayer(sender));
				return;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				throw new CommandException("error", ex.toString());
			}
		}

		sender.sendMessage(new TextComponentString(String.valueOf(entry.getSerializableElement())));
	}
}