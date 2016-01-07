package ftb.lib.mod;

import ftb.lib.api.*;
import latmod.lib.ByteIOStream;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public interface FTBUIntegration // FTBLIntegration
{
	public void onReloaded(EventFTBReload e);
	public void onFTBWorldServer(EventFTBWorldServer e);
	public void onFTBWorldClient(EventFTBWorldClient e);
	public void onFTBWorldServerClosed();
	public void onServerTick(World w);
	public void onPlayerJoined(EntityPlayerMP player);
	public int getPlayerID(Object player);
	public String[] getPlayerNames(boolean online);
	public void writeWorldData(ByteIOStream io, EntityPlayerMP ep);
	public void readWorldData(ByteIOStream io);
}