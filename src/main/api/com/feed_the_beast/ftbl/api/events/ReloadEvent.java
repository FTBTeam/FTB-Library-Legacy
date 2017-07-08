package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

/**
 * @author LatvianModder
 */
public class ReloadEvent extends Event
{
	private final Side side;
	private final ICommandSender sender;
	private final EnumReloadType type;
	private final FTBLibAPI api;

	public ReloadEvent(Side s, ICommandSender c, EnumReloadType t, FTBLibAPI a)
	{
		side = s;
		sender = c;
		type = t;
		api = a;
	}

	public Side getSide()
	{
		return side;
	}

	public ICommandSender getSender()
	{
		return sender;
	}

	public EnumReloadType getType()
	{
		return type;
	}

	public FTBLibAPI getAPI()
	{
		return api;
	}

	public File getPackModeFile(String path)
	{
		return new File(getAPI().getSidedData(getSide()).getPackMode().getFolder(), path);
	}
}