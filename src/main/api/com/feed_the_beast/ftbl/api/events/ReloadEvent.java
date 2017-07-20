package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

/**
 * @author LatvianModder
 */
public class ReloadEvent extends FTBLibEvent
{
	private final Side side;
	private final ICommandSender sender;
	private final EnumReloadType type;

	public ReloadEvent(Side s, ICommandSender c, EnumReloadType t)
	{
		side = s;
		sender = c;
		type = t;
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

	public File getPackModeFile(String path)
	{
		return new File(FTBLibAPI.API.getSidedData(getSide()).getPackMode().getFolder(), path);
	}
}