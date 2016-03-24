package ftb.lib.mod.client;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.*;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.friends.ILMPlayer;
import ftb.lib.api.gui.PlayerActionRegistry;
import ftb.lib.api.item.*;
import ftb.lib.mod.client.gui.GuiPlayerActions;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.entity.player.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class FTBLibClientEventHandler
{
	public static final FTBLibClientEventHandler instance = new FTBLibClientEventHandler();
	
	@SubscribeEvent
	public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
		ServerData sd = FTBLibClient.mc.func_147104_D();
		EventFTBWorldClient event = new EventFTBWorldClient(null);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onFTBWorldClient(event);
		FTBWorld.client = null;
		event.post();
	}
	
	@SubscribeEvent
	public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
	{
		EventFTBWorldClient event = new EventFTBWorldClient(null);
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
			e.toolTip.add(LMInvUtils.getRegName(e.itemStack).toString());
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
		
		if(FTBLib.ftbu != null) FTBLib.ftbu.onTooltip(e);
	}
	
	@SubscribeEvent
	public void onDrawDebugText(RenderGameOverlayEvent.Text e)
	{
		if(!FTBLibClient.mc.gameSettings.showDebugInfo)
		{
			if(FTBLib.DEV_ENV)
			{
				e.left.add("[MC " + EnumChatFormatting.GOLD + Loader.MC_VERSION + EnumChatFormatting.WHITE + " DevEnv]");
			}
		}
		else
		{
			//if(DevConsole.enabled()) e.left.add("r: " + MathHelperMC.get2DRotation(FTBLibClient.mc.thePlayer));
		}
	}
	
	@SubscribeEvent
	public void onEntityRightClick(EntityInteractEvent e)
	{
		if(e.entity.worldObj.isRemote && FTBLibClient.isIngame() && FTBLibModClient.player_options_shortcut.get() && e.entityPlayer.getGameProfile().getId().equals(FTBLibClient.mc.thePlayer.getGameProfile().getId()))
		{
			ILMPlayer self = FTBLibClient.getClientLMPlayer();
			ILMPlayer other = (FTBLib.ftbu == null) ? new TempLMPlayerFromEntity(Side.CLIENT, ((EntityPlayer) e.target)) : FTBLib.ftbu.getLMPlayer(e.target);
			if(other != null)
			{
				List<PlayerAction> a = PlayerActionRegistry.getPlayerActions(PlayerAction.Type.OTHER, self, other, true, false);
				if(!a.isEmpty()) FTBLibClient.openGui(new GuiPlayerActions(self, other, a));
			}
		}
	}
	
	@SubscribeEvent
	public void preTexturesLoaded(TextureStitchEvent.Pre e)
	{
		if(e.map.getTextureType() == 0)
		{
			FTBLibClient.blockNullIcon = e.map.registerIcon("ftbl:empty_block");
			FTBLibClient.clearCachedData();
		}
	}
}