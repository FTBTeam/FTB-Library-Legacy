package ftb.lib;

import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class BroadcastSender implements ICommandSender
{
	public static final BroadcastSender inst = new BroadcastSender();
	
	public static final BroadcastSender mute = new BroadcastSender()
	{
		public void addChatMessage(IChatComponent ics) { }
	};
	
	public String getName()
	{ return "[Server]"; }
	
	public IChatComponent getDisplayName()
	{ return new ChatComponentText(getName()); }
	
	public void addChatMessage(IChatComponent component)
	{ FTBLib.getServer().getConfigurationManager().sendChatMsgImpl(component, true); }
	
	public boolean canCommandSenderUseCommand(int permLevel, String commandName)
	{ return true; }
	
	public BlockPos getPosition()
	{ return FTBLib.getServerWorld().getSpawnCoordinate(); }
	
	public Vec3 getPositionVector()
	{ return new Vec3(getPosition()); }
	
	public World getEntityWorld()
	{ return FTBLib.getServerWorld(); }
	
	public Entity getCommandSenderEntity()
	{ return null; }
	
	public boolean sendCommandFeedback()
	{ return false; }
	
	public void setCommandStat(CommandResultStats.Type type, int amount)
	{
	}
}