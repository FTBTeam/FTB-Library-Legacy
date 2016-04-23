package ftb.lib;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class BroadcastSender implements ICommandSender
{
	public static final BroadcastSender inst = new BroadcastSender();
	
	@Override
	public String getCommandSenderName()
	{ return "[Server]"; }
	
	@Override
	public IChatComponent func_145748_c_()
	{ return null; }
	
	@Override
	public void addChatMessage(IChatComponent component)
	{ FTBLib.getServer().getConfigurationManager().sendChatMsgImpl(component, true); }
	
	@Override
	public boolean canCommandSenderUseCommand(int permLevel, String commandName)
	{ return true; }
	
	@Override
	public ChunkCoordinates getPlayerCoordinates()
	{ return FTBLib.getServerWorld().getSpawnPoint(); }
	
	@Override
	public World getEntityWorld()
	{ return FTBLib.getServerWorld(); }
}