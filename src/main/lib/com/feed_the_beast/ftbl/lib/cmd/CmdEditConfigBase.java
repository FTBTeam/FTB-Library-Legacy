package com.feed_the_beast.ftbl.lib.cmd;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public abstract class CmdEditConfigBase extends CmdBase
{
	public CmdEditConfigBase(String n, Level l)
	{
		super(n, l);
	}

	public abstract ITextComponent getTitle(ICommandSender sender) throws CommandException;

	public abstract ConfigTree getTree(ICommandSender sender) throws CommandException;

	public IConfigCallback getCallback(ICommandSender sender) throws CommandException
	{
		return IConfigCallback.DEFAULT;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		try
		{
			Map<ConfigKey, ConfigValue> map = getTree(sender).getTree();

			if (args.length == 1)
			{
				List<ConfigKey> keys = new ArrayList<>();
				keys.addAll(map.keySet());
				keys.sort(StringUtils.ID_COMPARATOR);
				return getListOfStringsMatchingLastWord(args, keys);
			}
			else if (args.length == 2)
			{
				ConfigValue entry = map.get(new ConfigKey(args[0]));

				if (entry != null)
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
			FTBLibAPI.API.editServerConfig(getCommandSenderAsPlayer(sender), getTree(sender), getTitle(sender), null, getCallback(sender));
			return;
		}

		checkArgs(args, 1, "[ID] [value]");

		ConfigTree tree = getTree(sender);
		ConfigKey key = tree.getKey(args[0]);

		if (key == null)
		{
			throw FTBLibLang.RAW.commandError("Can't find config entry '" + args[0] + "'!"); //LANG
		}

		ConfigValue entry = tree.get(key);

		if (args.length >= 2)
		{
			String json = String.valueOf(StringUtils.joinSpaceUntilEnd(1, args));
			FTBLibFinals.LOGGER.info("Setting " + args[0] + " to " + json); //LANG

			try
			{
				JsonElement value = JsonUtils.fromJson(JsonUtils.fixJsonString(json));
				JsonObject json1 = new JsonObject();
				json1.add(args[0], value);
				getCallback(sender).saveConfig(tree, sender, null, json1);
				sender.sendMessage(new TextComponentString("'" + key.getDisplayName() + "' set to " + tree.get(new ConfigKey(args[0])))); //LANG
				return;
			}
			catch (Exception ex)
			{
				throw FTBLibLang.RAW.commandError(ex.toString());
			}
		}

		sender.sendMessage(new TextComponentString(String.valueOf(entry.getSerializableElement())));
	}
}