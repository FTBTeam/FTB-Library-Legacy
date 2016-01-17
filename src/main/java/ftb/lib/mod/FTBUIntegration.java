package ftb.lib.mod;

import ftb.lib.api.*;
import ftb.lib.api.friends.ILMPlayer;
import latmod.lib.ByteIOStream;
import latmod.lib.util.Phase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.*;

public interface FTBUIntegration // FTBLIntegration
{
	void onReloaded(EventFTBReload e);
	void onFTBWorldServer(EventFTBWorldServer e);
	void onFTBWorldClient(EventFTBWorldClient e);
	void onFTBWorldServerClosed();
	void onServerTick(World w);
	void onPlayerJoined(EntityPlayerMP player, Phase phase);
	ILMPlayer getLMPlayer(Object player);
	String[] getPlayerNames(boolean online);
	void writeWorldData(ByteIOStream io, EntityPlayerMP ep);
	void readWorldData(ByteIOStream io);
	boolean hasClientWorld();
	void renderWorld(float pt);
	void onTooltip(ItemTooltipEvent e);
	void onRightClick(PlayerInteractEvent e);
}