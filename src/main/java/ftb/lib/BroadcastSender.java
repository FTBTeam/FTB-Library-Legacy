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
		@Override
		public void addChatMessage(ITextComponent ics) { }
	};
	
	@Override
	public String getName()
	{ return "[Server]"; }
	
	@Override
	public ITextComponent getDisplayName()
	{ return new TextComponentString(getName()); }
	
	@Override
	public void addChatMessage(ITextComponent component)
	{ FTBLib.getServer().getPlayerList().sendChatMsgImpl(component, true); }
	
	@Override
	public boolean canCommandSenderUseCommand(int permLevel, String commandName)
	{ return true; }
	
	@Override
	public BlockPos getPosition()
	{ return FTBLib.getServerWorld().getSpawnCoordinate(); }
	
	@Override
	public Vec3d getPositionVector()
	{ return new Vec3d(getPosition()); }
	
	@Override
	public World getEntityWorld()
	{ return FTBLib.getServerWorld(); }
	
	@Override
	public Entity getCommandSenderEntity()
	{ return null; }
	
	@Override
	public boolean sendCommandFeedback()
	{ return false; }
	
	@Override
	public void setCommandStat(CommandResultStats.Type type, int amount)
	{
	}
	
	@Override
	public MinecraftServer getServer()
	{ return FTBLib.getServer(); }
}