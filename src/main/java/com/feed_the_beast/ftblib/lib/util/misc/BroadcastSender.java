package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

/**
 * @author LatvianModder
 */
public class BroadcastSender implements ICommandSender
{
	private static final ITextComponent DISPLAY_NAME = StringUtils.color(new TextComponentString("[Server]"), TextFormatting.LIGHT_PURPLE);

	private final MinecraftServer server;

	public BroadcastSender(MinecraftServer s)
	{
		server = s;
	}

	@Override
	public String getName()
	{
		return "[Server]";
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return DISPLAY_NAME;
	}

	@Override
	public void sendMessage(ITextComponent component)
	{
		server.getPlayerList().sendMessage(component, true);
	}

	@Override
	public boolean canUseCommand(int permLevel, String commandName)
	{
		return true;
	}

	@Override
	public WorldServer getEntityWorld()
	{
		return (WorldServer) server.getEntityWorld();
	}

	@Override
	public MinecraftServer getServer()
	{
		return server;
	}
}