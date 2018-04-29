package com.feed_the_beast.ftblib.commands;

import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.cmd.CmdBase;
import com.feed_the_beast.ftblib.lib.data.FTBLibAPI;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author LatvianModder
 */
public class CmdReload extends CmdBase
{
	private Collection<String> tab;

	public CmdReload(String id, Level l)
	{
		super(id, l);

		tab = new HashSet<>();
		tab.add("*");

		for (ResourceLocation r : FTBLibCommon.RELOAD_IDS)
		{
			tab.add(r.toString());
			tab.add(r.getResourceDomain() + ":*");
		}

		tab = new ArrayList<>(tab);
		((ArrayList<String>) tab).sort(null);
	}

	public CmdReload()
	{
		this("reload", Level.OP);
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, tab);
		}

		return super.getTabCompletions(server, sender, args, pos);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		ResourceLocation id = ServerReloadEvent.ALL;

		if (args.length >= 1)
		{
			if (args[0].indexOf(':') != -1)
			{
				id = new ResourceLocation(args[0]);
			}
			else if (!args[0].equals("*"))
			{
				id = new ResourceLocation(args[0] + ":*");
			}
		}

		FTBLibAPI.reloadServer(Universe.get(), sender, EnumReloadType.RELOAD_COMMAND, id);
	}
}