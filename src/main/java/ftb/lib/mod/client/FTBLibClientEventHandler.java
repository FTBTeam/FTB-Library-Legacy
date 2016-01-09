package ftb.lib.mod.client;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.*;
import ftb.lib.*;
import ftb.lib.api.EventFTBWorldClient;
import ftb.lib.client.FTBLibClient;
import ftb.lib.item.*;
import ftb.lib.mod.FTBLibFinals;
import latmod.lib.LMStringUtils;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.*;

@SideOnly(Side.CLIENT)
public class FTBLibClientEventHandler
{
	public static final FTBLibClientEventHandler instance = new FTBLibClientEventHandler();

	@SubscribeEvent
	public void preTexturesLoaded(TextureStitchEvent.Pre e)
	{
		if(e.map.getTextureType() == 0)
		{
			FTBLibClient.blockNullIcon = e.map.registerIcon("ftbl:empty_block");
			FTBLibClient.clearCachedData();
		}
		else if(e.map.getTextureType() == 1) FTBLibClient.unknownItemIcon = e.map.registerIcon("ftbl:unknown");
	}
	
	@SubscribeEvent
	public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
		ServerData sd = FTBLibClient.mc.func_147104_D();
		String s = (sd == null || sd.serverIP.isEmpty()) ? "localhost" : sd.serverIP.replace('.', '_');
		FTBWorld.client = new FTBWorld(Side.CLIENT, new UUID(0L, 0L), s);
		
		EventFTBWorldClient event = new EventFTBWorldClient(FTBWorld.client, true);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldClient(event);
		event.post();
	}
	
	@SubscribeEvent
	public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
	{
		EventFTBWorldClient event = new EventFTBWorldClient(null, false);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldClient(event);
		event.post();
		FTBWorld.client = null;
	}
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent e)
	{
		if(e.itemStack == null || e.itemStack.getItem() == null) return;
		
		if(FTBLibModClient.item_reg_names.get())
		{
			e.toolTip.add(LMInvUtils.getRegName(e.itemStack));
		}
		
		if(FTBLibModClient.item_ore_names.get())
		{
			List<String> ores = ODItems.getOreNames(e.itemStack);
			
			if(ores != null && !ores.isEmpty())
			{
				e.toolTip.add("Ore Dictionary names:");
				for(String or : ores)
					e.toolTip.add("> " + or);
			}
		}
	}
	
	@SubscribeEvent
	public void onDrawDebugText(RenderGameOverlayEvent.Text e)
	{
		if(!FTBLibClient.mc.gameSettings.showDebugInfo)
		{
			if(FTBLibModClient.debug_info.get()) e.left.add(FTBLibClient.mc.debug);
			
			if(FTBLibFinals.DEV)
				e.left.add("[MC " + EnumChatFormatting.GOLD + Loader.MC_VERSION + EnumChatFormatting.WHITE + " DevEnv]");
		}
		else
		{
			e.left.add("r: " + MathHelperMC.get2DRotation(FTBLibClient.mc.thePlayer));
			
			if(FTBLibModClient.debug_info.get())
			{
				e.right.add("r: " + MathHelperMC.get2DRotation(FTBLibClient.mc.thePlayer));
				
				MovingObjectPosition mop = FTBLibClient.mc.objectMouseOver;
				
				if(mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					e.right.add(null);
					e.right.add("Block: " + LMStringUtils.stripI(mop.blockX, mop.blockY, mop.blockZ) + ", Side: " + mop.sideHit);
					e.right.add(LMInvUtils.getRegName(FTBLibClient.mc.theWorld.getBlock(mop.blockX, mop.blockY, mop.blockZ)) + " :: " + FTBLibClient.mc.theWorld.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ));
				}
			}
		}
	}
}