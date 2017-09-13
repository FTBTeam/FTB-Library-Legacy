package com.feed_the_beast.ftbl.lib.cmd;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigValueInstance;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

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
				ConfigValue entry = group.get(args[0]);

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
			FTBLibAPI.API.editServerConfig(getCommandSenderAsPlayer(sender), getGroup(sender), getCallback(sender));
			return;
		}

		checkArgs(args, 1, "[ID] [value]");

		ConfigGroup group = getGroup(sender);
		ConfigValue entry = group.get(args[0]);

		if (entry.isNull())
		{
			throw FTBLibLang.CONFIG_COMMAND_INVALID_KEY.commandError(args[0]);
		}

		if (args.length >= 2)
		{
			String json = String.valueOf(StringUtils.joinSpaceUntilEnd(1, args));
			FTBLibFinals.LOGGER.info(FTBLibLang.CONFIG_COMMAND_SETTING.translate(args[0], json));

			try
			{
				JsonElement value = JsonUtils.fromJson(JsonUtils.fixJsonString(json));
				JsonObject json1 = new JsonObject();
				json1.add(args[0], value);
				getCallback(sender).saveConfig(group, sender, json1);
				ConfigValueInstance instance = group.getMap().get(args[0]);
				FTBLibLang.CONFIG_COMMAND_SET.sendMessage(sender, new TextComponentTranslation(group.getNameKey(instance.info)), group.get(args[0]));
				return;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				throw FTBLibLang.ERROR.commandError(ex.toString());
			}
		}

		sender.sendMessage(new TextComponentString(String.valueOf(entry.getSerializableElement())));
	}
}