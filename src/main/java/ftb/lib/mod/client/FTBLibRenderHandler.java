package ftb.lib.mod.client;

import ftb.lib.*;
import ftb.lib.api.client.LMFrustrumUtils;
import ftb.lib.api.gui.callback.ClientTickCallback;
import ftb.lib.notification.ClientNotifications;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

@SideOnly(Side.CLIENT)
public class FTBLibRenderHandler
{
	public static final FTBLibRenderHandler instance = new FTBLibRenderHandler();
	public static final List<ClientTickCallback> callbacks = new ArrayList<>();
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void renderTick(TickEvent.RenderTickEvent e)
	{
		GlStateManager.pushMatrix();
		
		if(e.phase == TickEvent.Phase.START)
		{
			ScaledResolution sr = new ScaledResolution(FTBLibClient.mc);
			FTBLibClient.displayW = sr.getScaledWidth();
			FTBLibClient.displayH = sr.getScaledHeight();
		}
		
		if(e.phase == TickEvent.Phase.END && FTBLibClient.isIngame()) ClientNotifications.renderTemp();
		
		GlStateManager.popMatrix();
	}
	
	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent e)
	{
		if(e.phase == TickEvent.Phase.END && !callbacks.isEmpty())
		{
			for(int i = 0; i < callbacks.size(); i++)
				callbacks.get(i).onCallback();
			callbacks.clear();
		}
	}
	
	@SubscribeEvent
	public void renderWorld(RenderWorldLastEvent e)
	{
		LMFrustrumUtils.update();
		if(FTBLib.ftbu != null) FTBLib.ftbu.renderWorld(e.partialTicks);
	}
}