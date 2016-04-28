package ftb.lib.mod.client;

import ftb.lib.FTBLib;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.item.LMInvUtils;
import ftb.lib.api.item.ODItems;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.List;

@SideOnly(Side.CLIENT)
public class FTBLibClientEventHandler
{
	public static final FTBLibClientEventHandler instance = new FTBLibClientEventHandler();
	
	/*@SubscribeEvent
	public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent e)
	{
	}*/
	
	@SubscribeEvent
	public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent e)
	{
		if(ForgeWorldSP.inst != null)
		{
			ForgeWorldSP.inst.onClosed();
			ForgeWorldSP.inst = null;
		}
	}
	
	@SubscribeEvent
	public void onTooltip(ItemTooltipEvent e)
	{
		if(e.getItemStack() == null || e.getItemStack().getItem() == null) return;
		
		if(FTBLibModClient.item_reg_names.getAsBoolean())
		{
			e.getToolTip().add(LMInvUtils.getRegName(e.getItemStack()).toString());
		}
		
		if(FTBLibModClient.item_ore_names.getAsBoolean())
		{
			List<String> ores = ODItems.getOreNames(e.getItemStack());
			
			if(ores != null && !ores.isEmpty())
			{
				e.getToolTip().add("Ore Dictionary names:");
				
				for(String or : ores)
				{
					e.getToolTip().add("> " + or);
				}
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
				e.getLeft().add("[MC " + TextFormatting.GOLD + Loader.MC_VERSION + TextFormatting.WHITE + " DevEnv]");
			}
		}
	}
	
	@SubscribeEvent
	public void onKeyEvent(InputEvent.KeyInputEvent e)
	{
		if(Keyboard.getEventKeyState())
		{
			Shortcuts.onKeyPressed(Keyboard.getEventKey());
		}
	}
}