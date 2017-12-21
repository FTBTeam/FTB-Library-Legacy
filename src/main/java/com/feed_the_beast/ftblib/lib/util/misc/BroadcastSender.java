package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;

/**
 * @author LatvianModder
 */
public enum BroadcastSender implements ICommandSender
{
	INSTANCE;
	private static final ITextComponent DISPLAY_NAME = StringUtils.color(new TextComponentString("[Server]"), TextFormatting.LIGHT_PURPLE);

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
		ServerUtils.getServer().getPlayerList().sendMessage(component, true);
	}

	@Override
	public boolean canUseCommand(int permLevel, String commandName)
	{
		return true;
	}

	@Override
	public BlockPos getPosition()
	{
		BlockPos pos = getEntityWorld().getSpawnCoordinate();
		return pos == null ? BlockPos.ORIGIN : pos;
	}

	@Override
	public Vec3d getPositionVector()
	{
		return new Vec3d(getPosition());
	}

	@Override
	public WorldServer getEntityWorld()
	{
		return ServerUtils.getOverworld();
	}

	@Override
	public Entity getCommandSenderEntity()
	{
		return null;
	}

	@Override
	public boolean sendCommandFeedback()
	{
		return false;
	}

	@Override
	public void setCommandStat(CommandResultStats.Type type, int amount)
	{
	}

	@Override
	public MinecraftServer getServer()
	{
		return ServerUtils.getServer();
	}
}