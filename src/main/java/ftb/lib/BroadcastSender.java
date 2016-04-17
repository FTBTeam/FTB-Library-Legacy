package ftb.lib;

import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

public class BroadcastSender implements ICommandSender
{
	public static final BroadcastSender inst = new BroadcastSender();
	
	public static final BroadcastSender mute = new BroadcastSender()
	{
		public void addChatMessage(ITextComponent ics) { }
	};
	
	public String getName()
	{ return "[Server]"; }
	
	public ITextComponent getDisplayName()
	{ return new TextComponentString(getName()); }
	
	public void addChatMessage(ITextComponent component)
	{ FTBLib.getServer().getPlayerList().sendChatMsgImpl(component, true); }
	
	public boolean canCommandSenderUseCommand(int permLevel, String commandName)
	{ return true; }
	
	public BlockPos getPosition()
	{ return FTBLib.getServerWorld().getSpawnCoordinate(); }
	
	public Vec3d getPositionVector()
	{ return new Vec3d(getPosition()); }
	
	public World getEntityWorld()
	{ return FTBLib.getServerWorld(); }
	
	public Entity getCommandSenderEntity()
	{ return null; }
	
	public boolean sendCommandFeedback()
	{ return false; }
	
	public void setCommandStat(CommandResultStats.Type type, int amount)
	{
	}
	
	public MinecraftServer getServer()
	{ return FTBLib.getServer(); }
}