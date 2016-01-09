package ftb.lib;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class BroadcastSender implements ICommandSender
{
	public static final BroadcastSender inst = new BroadcastSender();
	
	public static final BroadcastSender mute = new BroadcastSender()
	{
		public void addChatMessage(IChatComponent ics) { }
	};
	
	public String getCommandSenderName()
	{ return "[Server]"; }
	
	public IChatComponent func_145748_c_()
	{ return null; }
	
	public void addChatMessage(IChatComponent ics)
	{ FTBLib.getServer().getConfigurationManager().sendChatMsgImpl(ics, true); }
	
	public boolean canCommandSenderUseCommand(int i, String s)
	{ return true; }
	
	public ChunkCoordinates getPlayerCoordinates()
	{ return new ChunkCoordinates(0, 66, 0); }
	
	public World getEntityWorld()
	{ return FTBLib.getServer().getEntityWorld(); }
}