package ftb.lib.mod;

import java.util.UUID;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.*;
import ftb.lib.FTBWorld;
import ftb.lib.api.EventFTBWorldClient;
import ftb.lib.client.FTBLibClient;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.client.event.TextureStitchEvent;

@SideOnly(Side.CLIENT)
public class FTBLibClientEventHandler
{
	@SubscribeEvent
	public void preTexturesLoaded(TextureStitchEvent.Pre e)
	{
		if(e.map.getTextureType() == 0)
		{
			FTBLibClient.blockNullIcon = e.map.registerIcon("ftbl:empty_block");
			FTBLibClient.clearCachedData();
		}
		else if(e.map.getTextureType() == 1)
			FTBLibClient.unknownItemIcon = e.map.registerIcon("ftbl:unknown");
	}
	
	@SubscribeEvent
	public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
		ServerData sd = FTBLibClient.mc.func_147104_D();
		String s = (sd == null || sd.serverIP.isEmpty()) ? "localhost" : sd.serverIP.replace('.', '_');
		FTBWorld.client = new FTBWorld(new UUID(0L, 0L), s);
		
		EventFTBWorldClient event = new EventFTBWorldClient(FTBWorld.client, true);
		if(FTBUIntegration.instance != null) FTBUIntegration.instance.onFTBWorldClient(event);
		event.post();
	}
	
	@SubscribeEvent
	public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
	{
		EventFTBWorldClient event = new EventFTBWorldClient(null, false);
		if(FTBUIntegration.instance != null) FTBUIntegration.instance.onFTBWorldClient(event);
		event.post();
		FTBWorld.client = null;
	}
}